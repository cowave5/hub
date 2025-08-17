package com.cowave.sys.meter.service.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.commons.tools.Converts;
import com.cowave.sys.meter.domain.api.*;
import com.cowave.sys.meter.domain.torna.bean.*;
import com.cowave.sys.meter.domain.torna.dto.*;
import com.cowave.sys.meter.domain.torna.entity.*;
import com.cowave.sys.meter.domain.torna.enums.*;
import com.cowave.sys.meter.domain.torna.message.MessageEnum;
import com.cowave.sys.meter.domain.torna.param.*;
import com.cowave.sys.meter.domain.api.request.ApiFolderPush;
import com.cowave.sys.meter.domain.api.request.ApiPush;
import com.cowave.sys.meter.domain.api.request.ApiCodePush;
import com.cowave.sys.meter.domain.torna.util.DataIdUtil;
import com.cowave.sys.meter.domain.torna.util.IdGen;
import com.cowave.sys.meter.infra.api.dao.*;
import com.cowave.sys.meter.infra.torna.DocDiffContext;
import com.cowave.sys.meter.infra.torna.DocMd5BuilderManager;
import com.cowave.sys.meter.infra.torna.ManualTransactionManager;
import com.cowave.sys.meter.infra.torna.dao.*;
import com.cowave.sys.meter.service.api.ApiPushService;
import com.gitee.easyopen.ApiContext;
import com.gitee.easyopen.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiPushServiceImpl implements ApiPushService {
    private static final String HTTP = "http:";
    private static final String HTTPS = "https:";
    private static final char SPLIT = '/';
    private static final String PREFIX = "://";
    private static final String REGEX_BR = "<br\\s*/*>";
    private static final String PUSH_ERROR_MSG = "文档【%s】推送失败，请查看日志";
    private final Object lock = new Object();

    private final ApiDefinitionDao apiDefinitionDao;
    private final ApiParamDao apiParamDao;
    private final ModuleEnvironmentDao moduleEnvironmentDao;
    private final ModuleEnvironmentParamDao moduleEnvironmentParamDao;
    private final ModuleConfigDao moduleConfigDao;
    private final PropDao propDao;
    private final ProjectDao projectDao;
    private final PushIgnoreFieldDao pushIgnoreFieldDao;
    private final ApiEnumDao apiEnumDao;
    private final ApiEnumItemDao apiEnumItemDao;
    private final ApiSnapshotDao apiSnapshotDao;
    private final ConstantInfoDao constantInfoDao;

    private final ManualTransactionManager manualTransactionManager;
    private final ApiFolderDao apiFolderDao;

    @Override
    public void push(String apiToken, ApiFolderPush folderPush) {
        long startTime = System.currentTimeMillis();
        ApiFolder apiFolder = apiFolderDao.getByToken(apiToken);
        Asserts.notNull(apiFolder, "api token not found");

        String ip = Access.accessIp();
        log.info("Api Push ==> {} {} {}", ip, folderPush.getAuthor(), apiFolder.getName());

        // 允许相同目录
        String allowSameFolder = EnvironmentKeys.TORNA_PUSH_ALLOW_SAME_FOLDER.getValue();
        if (Boolean.parseBoolean(allowSameFolder)) {
            this.mergeSameFolder(folderPush);
        } else {
            this.checkSameFolder(folderPush);
        }

        long folderId = apiFolder.getId();
        List<ApiDefinition> apiList = apiDefinitionDao.listByFolderId(folderId);

        List<DocMeta> docMetas = Collections.convertToList(apiList, DocMeta.class);

        PushContext pushContext = new PushContext(folderPush.getAuthor(), docMetas, new ArrayList<>());

        ThreadLocal<ApiPush> docPushItemParamThreadLocal = new ThreadLocal<>();
        synchronized (lock) {
            Object success = manualTransactionManager.execute("doc-push", () -> {
                // 设置调试环境
                saveDebugEnv(folderPush, folderId);
                // 替换文档
                replaceDoc(folderPush, folderId);
                // 处理文档
                executeDocs(folderPush, apiFolder, pushContext, docPushItemParamThreadLocal);
                // 设置公共错误码
                 setCommonErrorCodes(folderId, folderPush.getCommonErrorCodes());
                return true;
            },e -> {
                ApiPush apiPush = docPushItemParamThreadLocal.get();
                String paramInfo = JSON.toJSONString(apiPush);
                log.error("Api Push ==> failed, {}", apiFolder.getName(), e);
                this.sendErrorMessage(String.format(PUSH_ERROR_MSG, apiPush.getName()));
            });
            // 推送成功
            if (Objects.equals(success, true)) {
                log.info("Api Push ==> success, {} {}ms", apiFolder.getName(), System.currentTimeMillis() - startTime);
                // 发送站内消息
                sendPushMessage(folderPush, apiFolder);
                // 处理修改过的文档
                processModifiedDocs(pushContext);
                // 推送到MeterSphere
                // pushToMeterSphere(module);
                // 创建默认的mock
                createDefaultMock(apiFolder);
            }
        }
        docPushItemParamThreadLocal.remove();
    }

    private void mergeSameFolder(ApiFolderPush param) {
        List<ApiPush> apis = param.getApis();
        if (CollectionUtils.isEmpty(apis)) {
            return;
        }
        // key:分类名称， value:分类下的文档
        Map<ApiPush, List<ApiPush>> folderItems = new LinkedHashMap<>(8);
        for (ApiPush api : apis) {
            if (api.getIsFolder() == Booleans.TRUE) {
                List<ApiPush> docItemList = folderItems.computeIfAbsent(api, (k) -> new ArrayList<>());
                docItemList.addAll(api.getItems());
            }
        }
        if (folderItems.isEmpty()) {
            return;
        }
        folderItems.forEach(ApiPush::setItems);
        param.setApis(new ArrayList<>(folderItems.keySet()));
    }

    private void checkSameFolder(ApiFolderPush param) {
        List<ApiPush> apis = param.getApis();
        if (CollectionUtils.isEmpty(apis)) {
            return;
        }
        // key:分类名称，value:相同文档数量
        Map<String, AtomicInteger> folderCount = new HashMap<>(8);
        for (ApiPush api : apis) {
            if (api.getIsFolder() == Booleans.TRUE) {
                AtomicInteger count = folderCount.computeIfAbsent(api.getName(), (k) -> new AtomicInteger(0));
                count.incrementAndGet();
            }
        }
        folderCount.forEach((name, count) -> {
            if (count.get() > 1) {
                String msg = "文档名称重复【" + name + "】";
                this.sendErrorMessage(msg);
                throw new ApiException(msg);
            }
        });
    }

    private void createDefaultMock(ApiFolder apiFolder) {
        createModuleDocDefaultMock(apiFolder.getId());
    }

    public void createModuleDocDefaultMock(Long moduleId) {
        int poolSize = EnvironmentKeys.TORNA_PUSH_PROCESS_NUM.getInt();
        int executeSize = EnvironmentKeys.TORNA_PUSH_EXECUTE_SIZE.getInt();
//        ForkJoinPool forkJoinPool = new ForkJoinPool(poolSize);
//
//        LambdaQuery<DocInfo> query = docInfoService.query()
//                .eq(DocInfo::getModuleId, moduleId);
//        List<Long> docIds = docInfoService.listValue(query, DocInfo::getId);
//
//        ReduceTask<Long> reduceTask = new ReduceTask<>(docIds, 0, docIds.size(), executeSize, subList -> {
//            for (Long docId : subList) {
//                this.createDocDefaultMock(docId);
//            }
//        });
//        forkJoinPool.invoke(reduceTask);
    }

    private void sendPushMessage(ApiFolderPush param, ApiFolder apiFolder) {
        this.sendSuccessMessage("推送文档成功（" + apiFolder.getName() + "），推送人：" + param.getAuthor());
    }

    private void setCommonErrorCodes(long moduleId, List<ApiCodePush> commonErrorCodes) {
        if (CollectionUtils.isEmpty(commonErrorCodes)) {
            return;
        }
        List<ApiParam> errorCodeParams = Collections.convertToList(commonErrorCodes, ApiParam.class);
        constantInfoDao.setCommonErrorCodeList(errorCodeParams, moduleId);
    }

    private void saveDebugEnv(ApiFolderPush param, long moduleId) {
        for (DebugEnvParam debugEnv : param.getDebugEnvs()) {
            if (!StringUtils.hasText(debugEnv.getName()) || !StringUtils.hasText(debugEnv.getUrl())) {
                continue;
            }
            moduleEnvironmentDao.setDebugEnv(moduleId, debugEnv.getName(), debugEnv.getUrl());
        }
    }

    private void replaceDoc(ApiFolderPush param, long moduleId) {
        if (isOverride(moduleId) || Booleans.isTrue(param.getIsOverride(), false)) {
            return;
        }
        if (Booleans.isTrue(param.getIsReplace(), true)) {
            // 先删除之前的文档
            List<Long> docIdList = apiDefinitionDao.listDocIdByModuleId(moduleId);
            apiDefinitionDao.deleteOpenAPIModuleDocs(moduleId);
            if(!docIdList.isEmpty()){
                apiParamDao.deletePushParam(docIdList);
            }
        }
    }

    private boolean isOverride(long moduleId) {
        String value = moduleConfigDao.getCommonConfigValue(moduleId,
                EnvironmentKeys.TORNA_PUSH_OVERRIDE.getKey(),
                EnvironmentKeys.TORNA_PUSH_OVERRIDE.getDefaultValue());
        return Boolean.parseBoolean(value);
    }

    private void executeDocs(ApiFolderPush param, ApiFolder apiFolder, PushContext pushContext, ThreadLocal<ApiPush> paramThreadLocal) {
        for (ApiPush pushItem : param.getApis()) {
            paramThreadLocal.set(pushItem);
            this.pushDocItem(pushItem, apiFolder, 0L, pushContext);
        }
    }

    public void pushDocItem(ApiPush pushItem, ApiFolder apiFolder, Long parentId, PushContext pushContext) {
        long moduleId = apiFolder.getId();
        List<DocMeta> docMetas = pushContext.getDocMetas();
        // 分类
        if (Booleans.isTrue(pushItem.getIsFolder())) {
            Map<String, Object> props = null;
            DocTypeEnum docTypeEnum = pushItem.getDubboInfo() != null ? DocTypeEnum.DUBBO : DocTypeEnum.of(pushItem.getType());
            if (docTypeEnum == DocTypeEnum.DUBBO) {
                props = buildProps(pushItem);
            }

            DocFolderCreateDTO docFolderCreateDTO = new DocFolderCreateDTO();
            docFolderCreateDTO.setName(pushItem.getName());
            docFolderCreateDTO.setFolderId(moduleId);
            docFolderCreateDTO.setParentId(parentId);
            docFolderCreateDTO.setDocTypeEnum(docTypeEnum);
            docFolderCreateDTO.setProps(props);
            docFolderCreateDTO.setAuthor(pushItem.getAuthor());
            docFolderCreateDTO.setOrderIndex(pushItem.getOrderIndex());
            // 被锁住
            if (isLocked(docFolderCreateDTO.buildDataId(), docMetas)) {
                return;
            }

            ApiDefinition folder = createDocFolder(docFolderCreateDTO);

            List<ApiPush> items = pushItem.getItems();
            if (items != null) {
                for (ApiPush item : items) {
                    Long pid = folder.getId();
                    item.setType(docTypeEnum.getType());
                    // 如果接口作者为空，则使用文件夹作者
                    if (!StringUtils.hasText(item.getAuthor())) {
                        item.setAuthor(folder.getAuthor());
                    }
                    this.pushDocItem(item, apiFolder, pid, pushContext);
                }
            }
        } else {
            // 接口
            DocInfoDTO docInfoDTO = buildDocInfoDTO(pushItem);
            docInfoDTO.setFolderId(moduleId);
            docInfoDTO.setParentId(parentId);
            formatUrl(docInfoDTO);
            // 被锁住
            if (isLocked(docInfoDTO.buildDataId(), docMetas)) {
                return;
            }
            docInfoDTO.setModifierName(pushContext.getAuthor());
            doPushSaveDocInfo(docInfoDTO);
            doDocModifyProcess(docInfoDTO, pushContext);
        }
    }

    protected void doDocModifyProcess(DocInfoDTO docInfoDTO, PushContext pushContext) {
        DocInfoDTO docDetailView = getDocDetail(docInfoDTO.getId());
        Optional<String> md5Opt = getOldMd5(docDetailView.getDocKey(), pushContext.getDocMetas());
        ApiUser apiUser = new ApiUser();
        apiUser.setNickname(pushContext.getAuthor());
        String oldMd5 = md5Opt.orElse(null);
        doDocDiff(oldMd5, docDetailView, ModifySourceEnum.PUSH, apiUser);
    }

    public void doDocDiff(String oldMd5, DocInfoDTO docInfoDTO, ModifySourceEnum sourceEnum, User user) {
        doDocDiffNow(oldMd5, docInfoDTO, sourceEnum, user, DocDiffContext::addQueue);
    }

    public void doDocDiffNow(String oldMd5, DocInfoDTO docInfoDTO, ModifySourceEnum sourceEnum, User user, Consumer<DocDiffDTO> consumer) {
        String newMd5 = docInfoDTO.getMd5();
        boolean contentChanged = !Objects.equals(oldMd5, newMd5);
        // 文档内容被修改，做相关处理
        if (contentChanged) {
            // 保存新md5内容
            saveDocSnapshot(docInfoDTO);
            consumer.accept(new DocDiffDTO(oldMd5, newMd5, LocalDateTime.now(), user, sourceEnum));
        }
    }

    public void saveDocSnapshot(DocInfoDTO docInfoDTO) {
        ApiSnapshot snapshot = apiSnapshotDao.getByMd5(docInfoDTO.getMd5());
        if (snapshot != null) {
            return;
        }

        String content = JSON.toJSONString(docInfoDTO);
        ApiSnapshot apiSnapshot = new ApiSnapshot();
        apiSnapshot.setMd5(docInfoDTO.getMd5());
        apiSnapshot.setApiId(docInfoDTO.getId());
        apiSnapshot.setContent(content);
        try {
            removeSnapshotSize(docInfoDTO.getId(), EnvironmentKeys.TORNA_SNAPSHOT_SIZE.getInt());
        } catch (Exception e) {
            log.error("移除快照报错", e);
        }
        apiSnapshotDao.save(apiSnapshot);
    }

    public void removeSnapshotSize(long docId, int limitSize) {
        List<ApiSnapshot> list = apiSnapshotDao.listByDocId(docId);
        if (list.isEmpty()) {
            return;
        }

        List<String> md5List = Collections.copyToList(list, ApiSnapshot::getMd5);
        List<Long> idList = Collections.copyToList(list, ApiSnapshot::getId);
        int size = idList.size();
        if (size > limitSize) {
            int limit = size - limitSize;
            List<Long> removeIds = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                removeIds.add(idList.get(i));
            }
            apiSnapshotDao.removeBatchByIds(removeIds);
        }

//        TODO
//        for (List<String> md5s : Lists.partition(md5List, 200)) {
//            List<Long> recordIds = docDiffRecordMapper.query()
//                    .in(DocDiffRecord::getMd5New, md5s)
//                    .listValue(DocDiffRecord::getId);
//            List<Long> recordIds2 = docDiffRecordMapper.query()
//                    .in(DocDiffRecord::getMd5Old, md5s)
//                    .listValue(DocDiffRecord::getId);
//            recordIds.addAll(recordIds2);
//
//            removeRecord(recordIds);
//        }
    }

    public static Optional<String> getOldMd5(String docKey, List<DocMeta> docMetas) {
        if (CollectionUtils.isEmpty(docMetas)) {
            return Optional.empty();
        }
        return docMetas.stream()
                .filter(docMeta -> Objects.equals(docKey, docMeta.getDocKey()))
                .findFirst()
                .map(DocMeta::getMd5);
    }

    public DocInfoDTO getDocDetail(Long docId) {
        ApiDefinition apiDefinition = apiDefinitionDao.getById(docId);
        return getDocDetail(apiDefinition);
    }

    public DocInfoDTO getDocDetail(ApiDefinition apiDefinition) {
        DocInfoDTO docInfoDTO = this.getDocInfoDTO(apiDefinition);
        Long moduleId = docInfoDTO.getFolderId();
        List<ApiParam> globalHeaders = listGlobalHeaders(moduleId);
        List<ApiParam> globalParams = listGlobalParams(moduleId);
        List<ApiParam> globalReturns = listGlobalReturns(moduleId);
        docInfoDTO.setGlobalHeaders(Collections.convertToList(globalHeaders, DocParamDTO.class));
        docInfoDTO.setGlobalParams(Collections.convertToList(globalParams, DocParamDTO.class));
        docInfoDTO.setGlobalReturns(Collections.convertToList(globalReturns, DocParamDTO.class));
        docInfoDTO.getGlobalHeaders().forEach(docParamDTO -> docParamDTO.setGlobal(true));
        return docInfoDTO;
    }

    public List<ApiParam> listGlobalReturns(long moduleId) {
        return this.listGlobal(moduleId, ParamStyleEnum.RESPONSE);
    }

    public List<ApiParam> listGlobalParams(long moduleId) {
        return this.listGlobal(moduleId, ParamStyleEnum.REQUEST);
    }

    public List<ApiParam> listGlobalHeaders(long moduleId) {
        return this.listGlobal(moduleId, ParamStyleEnum.HEADER);
    }

    public List<ApiParam> listGlobal(long moduleId, ParamStyleEnum paramStyleEnum) {
        ModuleEnvironment environment = moduleEnvironmentDao.getFirst(moduleId);
        if (environment == null) {
            return java.util.Collections.emptyList();
        }
        List<ModuleEnvironmentParam> moduleEnvironmentParams = moduleEnvironmentParamDao.listByEnvironmentAndStyle(environment.getId(), paramStyleEnum.getStyle());
        // id去重，防止跟doc_param表id重复
        long offset = System.currentTimeMillis();
        for (ModuleEnvironmentParam moduleEnvironmentParam : moduleEnvironmentParams) {
            moduleEnvironmentParam.setId(moduleEnvironmentParam.getId() + offset);
            if (moduleEnvironmentParam.getParentId() > 0) {
                moduleEnvironmentParam.setParentId(moduleEnvironmentParam.getParentId() + offset);
            }
        }
        return Collections.convertToList(moduleEnvironmentParams, ApiParam.class);
    }

    public DocInfoDTO getDocInfoDTO(ApiDefinition apiDefinition) {
        Assert.notNull(apiDefinition, () -> "文档不存在");
        DocInfoDTO docInfoDTO = Converts.copyProperties(apiDefinition, DocInfoDTO.class);
        Long moduleId = apiDefinition.getFolderId();
        ApiFolder apiFolder = apiFolderDao.getById(moduleId);
        docInfoDTO.setSpaceId(getSpaceId(apiFolder.getProjectId()));
        docInfoDTO.setProjectId(apiFolder.getProjectId());
        docInfoDTO.setModuleType(apiFolder.getType());
        List<ModuleEnvironment> debugEnvs = moduleEnvironmentDao.listModuleEnvironment(moduleId);
        docInfoDTO.setDebugEnvs(Collections.convertToList(debugEnvs, ModuleEnvironmentDTO.class));
        List<ApiParam> params = apiParamDao.listByDocId(apiDefinition.getId());
        params.sort(Comparator.comparing(ApiParam::getOrderIndex));
        Map<Byte, List<ApiParam>> paramsMap = params.stream()
                .collect(Collectors.groupingBy(ApiParam::getStyle));
        List<ApiParam> pathParams = paramsMap.getOrDefault(ParamStyleEnum.PATH.getStyle(), java.util.Collections.emptyList());
        List<ApiParam> headerParams = paramsMap.getOrDefault(ParamStyleEnum.HEADER.getStyle(), java.util.Collections.emptyList());
        List<ApiParam> queryParams = paramsMap.getOrDefault(ParamStyleEnum.QUERY.getStyle(), java.util.Collections.emptyList());
        List<ApiParam> requestParams = paramsMap.getOrDefault(ParamStyleEnum.REQUEST.getStyle(), java.util.Collections.emptyList());
        List<ApiParam> responseParams = paramsMap.getOrDefault(ParamStyleEnum.RESPONSE.getStyle(), java.util.Collections.emptyList());
        List<ApiParam> errorCodeParams = paramsMap.getOrDefault(ParamStyleEnum.ERROR_CODE.getStyle(), new ArrayList<>(0));
        docInfoDTO.setPathParams(Collections.convertToList(pathParams, DocParamDTO.class));
        docInfoDTO.setHeaderParams(Collections.convertToList(headerParams, DocParamDTO.class));
        docInfoDTO.setHeaderParamsRaw(Collections.convertToList(headerParams, DocParamDTO.class));
        docInfoDTO.setQueryParams(Collections.convertToList(queryParams, DocParamDTO.class));
        docInfoDTO.setRequestParams(Collections.convertToList(requestParams, DocParamDTO.class));
        docInfoDTO.setResponseParams(Collections.convertToList(responseParams, DocParamDTO.class));
        docInfoDTO.setErrorCodeParams(Collections.convertToList(errorCodeParams, DocParamDTO.class));
        // 绑定枚举信息
        bindEnumInfo(docInfoDTO.getPathParams());
        bindEnumInfo(docInfoDTO.getQueryParams());
        bindEnumInfo(docInfoDTO.getRequestParams());
        DubboInfoDTO dubboInfoDTO = buildDubboInfoDTO(apiDefinition);
        docInfoDTO.setDubboInfo(dubboInfoDTO);
        return docInfoDTO;
    }

    public Long getSpaceId(Long projectId) {
        Project project = projectDao.getById(projectId);
        return Optional.ofNullable(project).map(Project::getSpaceId).orElse(null);
    }

    private void bindEnumInfo(List<DocParamDTO> docParamDTOS) {
        for (DocParamDTO docParamDTO : docParamDTOS) {
            Long enumId = docParamDTO.getEnumId();
            if (enumId != null && enumId > 0) {
                ApiEnum apiEnum = apiEnumDao.getById(enumId);
                if (apiEnum == null) {
                    continue;
                }
                EnumInfoDTO enumInfoDTO = Converts.copyProperties(apiEnum, EnumInfoDTO.class);
                List<EnumItemDTO> enumItemDTOS = apiEnumItemDao.listItems(enumId);
                enumInfoDTO.setItems(enumItemDTOS);
                docParamDTO.setEnumInfo(enumInfoDTO);
            } else if (DataType.ENUM.equalsIgnoreCase(docParamDTO.getType()) && StringUtils.hasText(docParamDTO.getDescription())) {
                String description = docParamDTO.getDescription();
                EnumInfoDTO enumInfoDTO = new EnumInfoDTO();
                String[] arr;
                if (description.contains("<br")) {
                    arr = description.split(REGEX_BR);
                } else if (description.contains("、")) {
                    arr = description.split("、");
                } else {
                    arr = new String[]{description};
                }
                List<EnumItemDTO> items = Arrays.stream(arr)
                        .map(val -> {
                            EnumItemDTO enumItemDTO = new EnumItemDTO();
                            enumItemDTO.setName(val);
                            enumItemDTO.setValue(val);
                            return enumItemDTO;
                        })
                        .collect(Collectors.toList());
                enumInfoDTO.setItems(items);
                docParamDTO.setEnumInfo(enumInfoDTO);
            }

        }
    }

    private DubboInfoDTO buildDubboInfoDTO(ApiDefinition apiDefinition) {
        if (apiDefinition.getType() == DocTypeEnum.DUBBO.getType()) {
            Map<String, String> docProps = propDao.getDocProps(apiDefinition.getParentId());
            DubboInfoDTO dubboInfoDTO = new DubboInfoDTO();
            dubboInfoDTO.setProtocol(docProps.get("protocol"));
            dubboInfoDTO.setDependency(docProps.get("dependency"));
            dubboInfoDTO.setAuthor(docProps.get("author"));
            dubboInfoDTO.setInterfaceName(docProps.get("interfaceName"));
            return dubboInfoDTO;
        }
        return null;
    }

    public void doPushSaveDocInfo(DocInfoDTO docInfoDTO) {
        // 修改基本信息
        ApiDefinition apiDefinition = this.insertDocInfo(docInfoDTO);
        // 删除文档对应的参数
        apiParamDao.deletePushParam(List.of(apiDefinition.getId()));
        // 修改参数
        docInfoDTO.setId(apiDefinition.getId());
        this.doUpdateParams(apiDefinition, docInfoDTO);
    }

    private void doUpdateParams(ApiDefinition apiDefinition, DocInfoDTO docInfoDTO) {
        saveParams(apiDefinition, docInfoDTO.getPathParams(), ParamStyleEnum.PATH);
        saveParams(apiDefinition, docInfoDTO.getHeaderParams(), ParamStyleEnum.HEADER);
        saveParams(apiDefinition, docInfoDTO.getQueryParams(), ParamStyleEnum.QUERY);
        saveParams(apiDefinition, docInfoDTO.getRequestParams(), ParamStyleEnum.REQUEST);
        saveParams(apiDefinition, docInfoDTO.getResponseParams(), ParamStyleEnum.RESPONSE);
        saveParams(apiDefinition, docInfoDTO.getErrorCodeParams(), ParamStyleEnum.ERROR_CODE);
    }

    public void saveParams(ApiDefinition apiDefinition, List<DocParamDTO> docParamDTOS, ParamStyleEnum paramStyleEnum) {
        // 如果参数是空的，则移除这个类型的所有参数
        if (CollectionUtils.isEmpty(docParamDTOS)) {
            return;
        }
        for (DocParamDTO docParamDTO : docParamDTOS) {
            this.doSave(docParamDTO, 0L, apiDefinition, paramStyleEnum);
        }
    }

    private void doSave(DocParamDTO docParamDTO, long parentId, ApiDefinition apiDefinition, ParamStyleEnum paramStyleEnum) {
        ApiParam apiParam = new ApiParam();
        Long docId = apiDefinition.getId();
        String dataId = DataIdUtil.getDocParamDataId(docId, parentId, paramStyleEnum.getStyle(), docParamDTO.getName());
        // 如果删除
        if (Booleans.isTrue(docParamDTO.getIsDeleted())) {
            dataId = IdGen.nextId();
        }
        docParamDTO.setParentId(parentId);
        apiParam.setId(docParamDTO.getId());
        apiParam.setDataId(dataId);
        apiParam.setName(docParamDTO.getName());
        apiParam.setType(docParamDTO.getType());
        apiParam.setRequired(docParamDTO.getRequired());
        apiParam.setMaxLength(buildMaxLength(docParamDTO));
        apiParam.setExample(docParamDTO.getExample());
        apiParam.setDescription(docParamDTO.getDescription());
        apiParam.setEnumId(buildEnumId(apiDefinition.getFolderId(), docParamDTO));
        apiParam.setApiId(docId);
        apiParam.setParentId(parentId);
        apiParam.setStyle(paramStyleEnum.getStyle());
        apiParam.setCreateMode(OperationMode.OPEN.getType());
        apiParam.setUpdateMode(OperationMode.OPEN.getType());
        apiParam.setOrderIndex(docParamDTO.getOrderIndex());
        apiParam.setIsDeleted(docParamDTO.getIsDeleted());
        if (apiParam.getDescription() == null) {
            apiParam.setDescription("");
        }
        ApiParam savedParam;
        if (apiParam.getId() == null) {
            savedParam = this.saveParam(apiParam);
        } else {
            apiParamDao.updateById(apiParam);
            savedParam = apiParam;
        }
        // 回填ID
        docParamDTO.setId(savedParam.getId());
        List<DocParamDTO> children = docParamDTO.getChildren();
        if (children != null) {
            Long pid = savedParam.getId();
            // 修复NPE问题
            if (pid == null) {
                ApiParam exist = apiParamDao.getByDataId(savedParam.getDataId());
                if (exist != null) {
                    pid = exist.getId();
                }
            }
            for (DocParamDTO child : children) {
                // 如果父节点被删除，子节点也要删除
                if (apiParam.getIsDeleted() == Booleans.TRUE) {
                    child.setIsDeleted(apiParam.getIsDeleted());
                }
                if (pid == null) {
                    continue;
                }
                this.doSave(child, pid, apiDefinition, paramStyleEnum);
            }
        }
    }

    public ApiParam saveParam(ApiParam apiParam) {
        if (apiParam.getDescription() == null) {
            apiParam.setDescription("");
        }
        if (apiParam.getExample() == null) {
            apiParam.setExample("");
        }
        apiParamDao.save(apiParam);
        return apiParam;
    }

    private static String buildMaxLength(DocParamDTO docParamDTO) {
        String maxLength = docParamDTO.getMaxLength();
        if (!StringUtils.hasText(maxLength) || "0".equals(maxLength)) {
            maxLength = "-";
        }
        return CollectionUtils.isEmpty(docParamDTO.getChildren()) ? maxLength : "";
    }

    private Long buildEnumId(long moduleId, DocParamDTO docParamDTO) {
        String desc = docParamDTO.getDescription();
        EnumInfoDTO enumInfoDTO = docParamDTO.getEnumInfo();
        if (enumInfoDTO != null) {
            if (ObjectUtils.isEmpty(enumInfoDTO.getDescription())) {
                if(desc.contains("<br/>")){
                    enumInfoDTO.setDescription(desc.substring(desc.indexOf("<br/>") + 5, desc.lastIndexOf("<br/>")));
                }else{
                    enumInfoDTO.setDescription(desc);
                }
            }

            if (ObjectUtils.isEmpty(enumInfoDTO.getName())) {
                if(desc.contains("<br/>")){
                    enumInfoDTO.setName(desc.substring(0, desc.indexOf("<br/>")));
                }else{
                    enumInfoDTO.setName(desc);
                }
            }

            enumInfoDTO.setFolderId(moduleId);
            ApiEnum apiEnum = saveEnumInfo(enumInfoDTO);
            return apiEnum.getId();
        }
        Long enumId = docParamDTO.getEnumId();
        if (enumId == null) {
            enumId = 0L;
        }
        return enumId;
    }

    public ApiEnum saveEnumInfo(EnumInfoDTO enumInfoDTO) {
        // 如果枚举名称为空则使用字段名称
//        if (!StringUtils.hasText(enumInfoDTO.getName())) {
//            enumInfoDTO.setName(enumInfoDTO.getDescription());
//        }
        String dataId = enumInfoDTO.buildDataId();
        ApiEnum apiEnum = apiEnumDao.getByDataId(dataId);
        if (apiEnum == null) {
            apiEnum = Converts.copyProperties(enumInfoDTO, ApiEnum.class);
            apiEnum.setDataId(dataId);
            apiEnumDao.save(apiEnum);
        } else {
            apiEnum.setName(enumInfoDTO.getName());
            apiEnum.setDescription(enumInfoDTO.getDescription());
            apiEnumDao.updateById(apiEnum);
        }
        List<EnumItemDTO> items = enumInfoDTO.getItems();
        this.updateItems(apiEnum, items);
        return apiEnum;
    }

    private void updateItems(ApiEnum apiEnum, List<EnumItemDTO> items) {
        if (!CollectionUtils.isEmpty(items)) {
            apiEnumItemDao.replaceEnumItem(apiEnum.getId(), items);
        }
    }

    private static void formatUrl(DocInfoDTO docInfoDTO) {
        if (docInfoDTO.getType() == DocTypeEnum.DUBBO.getType()) {
            return;
        }
        String url = docInfoDTO.getUrl();
        url = removePrefix(url);
        docInfoDTO.setUrl(url);
    }

    private static String removePrefix(String url) {
        if (!StringUtils.hasText(url)) {
            return url;
        }
        String urlLowerCase = url.toLowerCase();
        if (urlLowerCase.startsWith(HTTP) || urlLowerCase.startsWith(HTTPS)) {
            int prefixIndex = urlLowerCase.indexOf(PREFIX);
            url = url.substring(prefixIndex + PREFIX.length());
            url = StringUtils.trimLeadingCharacter(url, SPLIT);
            int index = url.indexOf(SPLIT);
            if (index > 0) {
                url = url.substring(index);
            }
        }
        return SPLIT + StringUtils.trimLeadingCharacter(url, SPLIT);
    }

    private static DocInfoDTO buildDocInfoDTO(ApiPush param) {
        String json = JSON.toJSONString(param, SerializerFeature.WriteDateUseDateFormat);
        DocInfoDTO docInfoDTO = JSON.parseObject(json, DocInfoDTO.class);
        String version = param.getVersion();
        if ("-".equals(version)) {
            version = "";
        }
        docInfoDTO.setVersion(version);

        String url = param.getUrl();
        String[] split = url.split("#");
        if (split.length == 2) {
            // 接口+版本号,格式:url##version, 如:listUser#1.0
            docInfoDTO.setUrl(split[0]);
            docInfoDTO.setVersion(split[1]);
        }

        List<ApiCodePush> errorCodeParams = param.getErrorCodeParams();
        if (!CollectionUtils.isEmpty(errorCodeParams)) {
            List<DocParamDTO> errorParams = Collections.convertToList(errorCodeParams, DocParamDTO.class);
            docInfoDTO.setErrorCodeParams(errorParams);
        }
        if (StringUtils.hasText(param.getDefinition())) {
            docInfoDTO.setUrl(param.getDefinition());
        }
        String md5 = DocMd5BuilderManager.getBuilder().buildMd5(docInfoDTO);
        docInfoDTO.setMd5(md5);
        return docInfoDTO;
    }

    public ApiDefinition createDocFolder(DocFolderCreateDTO docFolderCreateDTO) {
        DocInfoDTO docInfoDTO = new DocInfoDTO();
        docInfoDTO.setName(docFolderCreateDTO.getName());
        docInfoDTO.setFolderId(docFolderCreateDTO.getFolderId());
        docInfoDTO.setParentId(docFolderCreateDTO.getParentId());
        if (docFolderCreateDTO.getDocTypeEnum() != null) {
            docInfoDTO.setType(docFolderCreateDTO.getDocTypeEnum().getType());
        }
        docInfoDTO.setIsGroup(Booleans.TRUE);
        docInfoDTO.setAuthor(docFolderCreateDTO.getAuthor());
        docInfoDTO.setOrderIndex(docFolderCreateDTO.getOrderIndex());
        ApiDefinition apiDefinition = insertDocInfo(docInfoDTO);
        Map<String, ?> props = docFolderCreateDTO.getProps();
        propDao.saveProps(props, apiDefinition.getId(), PropTypeEnum.DOC_INFO_PROP);
        return apiDefinition;
    }

    public static boolean isLocked(String dataId, List<DocMeta> docMetas) {
        if (CollectionUtils.isEmpty(docMetas)) {
            return false;
        }
        for (DocMeta docMeta : docMetas) {
            if (Objects.equals(dataId, docMeta.getDataId()) && docMeta.getIsLocked() == Booleans.TRUE) {
                return true;
            }
        }
        return false;
    }

    private ApiDefinition insertDocInfo(DocInfoDTO docInfoDTO) {
        ApiDefinition apiDefinition = buildDocInfo(docInfoDTO);
        String docMd5 = getDocMd5(docInfoDTO);
        apiDefinition.setMd5(docMd5);
        apiDefinitionDao.save(apiDefinition);
        return apiDefinition;
    }

    public static String getDocMd5(DocInfoDTO docInfoDTO) {
        return DocMd5BuilderManager.getBuilder().buildMd5(docInfoDTO);
    }

    private ApiDefinition buildDocInfo(DocInfoDTO docInfoDTO) {
        ApiDefinition apiDefinition = Converts.copyProperties(docInfoDTO, ApiDefinition.class);
        // 手动赋值
        apiDefinition.setCreateMode(OperationMode.OPEN.getType());
        apiDefinition.setUpdateMode(OperationMode.OPEN.getType());
        apiDefinition.setDataId(docInfoDTO.buildDataId());
        apiDefinition.setDocKey(docInfoDTO.buildDocKey());
        if (apiDefinition.getDescription() == null) {
            apiDefinition.setDescription("");
        }
        if (apiDefinition.getDeprecated() == null) {
            apiDefinition.setDeprecated("$false$");
        }
        // 描述字段忽略
        if (pushIgnoreFieldDao.isPushIgnore(docInfoDTO.getFolderId(), docInfoDTO.buildDataId(), "description")) {
            apiDefinition.setDescription(null);
        }
        return apiDefinition;
    }

    private Map<String, Object> buildProps(ApiPush param) {
        DubboParam dubboInfo = param.getDubboInfo();
        if (dubboInfo == null) {
            return java.util.Collections.emptyMap();
        }
        String json = JSON.toJSONString(dubboInfo);
        return JSON.parseObject(json);
    }

    private void sendSuccessMessage(String msg) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageEnum(MessageEnum.PUSH_DOC_SUCCESS);
        messageDTO.setType(UserSubscribeTypeEnum.PUSH_DOC);
        messageDTO.setLocale(ApiContext.getLocal());
        messageDTO.setSourceId(0L);
//        userMessageService.sendMessageToAdmin(messageDTO, msg);
//        if (!(user instanceof ApiUser)) {
//            userMessageService.sendMessageToUser(messageDTO, msg, user.getUserId());
//        }
    }

    public void sendErrorMessage(String msg) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageEnum(MessageEnum.PUSH_ERROR);
        messageDTO.setType(UserSubscribeTypeEnum.PUSH_DOC);
        messageDTO.setLocale(ApiContext.getLocal());
        messageDTO.setSourceId(0L);
        // userMessageService.sendMessageToAdmin(messageDTO, msg);
    }

    private void processModifiedDocs(PushContext pushContext) {
        String url = EnvironmentKeys.PUSH_DINGDING_WEBHOOK_URL.getValue();
        List<DocInfoDTO> contentChangedDocs = pushContext.getContentChangedDocs();
        if (StringUtils.hasText(url) && !CollectionUtils.isEmpty(contentChangedDocs)) {
            String names = contentChangedDocs.stream()
                    .map(DocInfoDTO::getName)
                    .collect(Collectors.joining("、"));
            String content = String.format(EnvironmentKeys.PUSH_DINGDING_WEBHOOK_CONTENT.getValue(), names);
//            DingdingWebHookBody dingdingWebHookBody = DingdingWebHookBody.create(content);
//            try {
//                // 推送钉钉机器人
//                String result = HttpHelper.postJson(url, JSON.toJSONString(dingdingWebHookBody))
//                        .execute()
//                        .asString();
//                log.info("文档变更，推送钉钉机器人, url:{}, content:{}, 推送结果:{}", url, content, result);
//            } catch (Exception e) {
//                log.error("推送钉钉失败, url:{}", url, e);
//            }
        }
    }
}
