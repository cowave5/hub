/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.base.impl;

import com.cowave.commons.client.http.asserts.HttpHintException;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.es.EsHelper;
import com.cowave.sys.admin.domain.base.SysOperation;
import com.cowave.sys.admin.domain.base.request.OperationQuery;
import com.cowave.sys.admin.domain.rabc.SysScope;
import com.cowave.sys.admin.infra.rabc.dao.mapper.dto.SysScopeDtoMapper;
import com.cowave.sys.admin.service.base.SysOperationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static com.cowave.commons.client.http.constants.HttpCode.FORBIDDEN;
import static com.cowave.commons.framework.access.security.Permission.ROLE_ADMIN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysOperationServiceImpl implements SysOperationService {
    private static final String SCOPE_PERSON = "personal";
    private static final String SCOPE_DEPT = "dept";
    private final EsHelper esHelper;
    private final SysScopeDtoMapper sysScopeDtoMapper;

    @Override
    public Response.Page<SysOperation> list(String tenantId, OperationQuery query, boolean isPage) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termsQuery("access.accessTenantId", tenantId));
        if (StringUtils.isNotBlank(query.getOpModule())) {
            boolQuery.filter(QueryBuilders.termsQuery("opModule", query.getOpModule()));
        }
        if (StringUtils.isNotBlank(query.getOpType())) {
            boolQuery.filter(QueryBuilders.termsQuery("opType", query.getOpType()));
        }

        // 数据权限过滤
        String currentScope = getCurrentScope();
        if (StringUtils.isNotBlank(currentScope)) {
            if (SCOPE_PERSON.equals(currentScope)) {
                boolQuery.filter(QueryBuilders.termsQuery("access.accessUserAccount", Access.userAccount()));
            } else if (SCOPE_DEPT.equals(currentScope)) {
                Integer deptId = Access.deptId();
                boolQuery.filter(QueryBuilders.termsQuery("access.accessDeptId", List.of(deptId)));
            }
        }

        if (query.getBeginTime() != null || query.getEndTime() != null) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("opTime");
            if (query.getBeginTime() != null) {
                rangeQueryBuilder.gte(query.getBeginTime());
            }
            if (query.getEndTime() != null) {
                rangeQueryBuilder.lte(query.getEndTime().getTime());
            }
            boolQuery.filter(rangeQueryBuilder);
        }

        if (StringUtils.isNotBlank(query.getOpUser())) {
            BoolQueryBuilder orCondition = QueryBuilders.boolQuery();
            orCondition.should(QueryBuilders.wildcardQuery("access.accessUserName", query.getOpUser()));
            orCondition.should(QueryBuilders.wildcardQuery("access.accessUserAccount", query.getOpUser()));
            boolQuery.filter(orCondition);
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (boolQuery.hasClauses()) {
            searchSourceBuilder.query(boolQuery);
        }

        searchSourceBuilder.sort("opTime", SortOrder.DESC);
        if (isPage) {
            searchSourceBuilder.size(Access.pageSize());
            searchSourceBuilder.from(Access.pageOffset());
        }
        return esHelper.query(SysOperation.INDEX_NAME, searchSourceBuilder, SysOperation.class);
    }

    @Override
    public void delete(List<String> ids) {
        List<SysOperation> list = esHelper.getByIds(SysOperation.INDEX_NAME, ids, SysOperation.class);
        // 数据权限校验
        String currentScope = getCurrentScope();
        if (StringUtils.isNotBlank(currentScope)) {
            Predicate<SysOperation> predicate;
            if (SCOPE_PERSON.equals(currentScope)) {
                String userAccount = Access.userAccount();
                predicate = op -> Objects.equals(userAccount, op.getAccess().getAccessUserAccount());
                list.stream().filter(op -> !predicate.test(op)).findAny().ifPresent(op -> {
                    throw new HttpHintException(FORBIDDEN, "{frame.auth.permit.denied}");
                });
            } else if (SCOPE_DEPT.equals(currentScope)) {
                Long deptId = Access.deptId();
                predicate = op -> Objects.equals(deptId, op.getAccess().getAccessDeptId());
                list.stream().filter(op -> !predicate.test(op)).findAny().ifPresent(op -> {
                    throw new HttpHintException(FORBIDDEN, "{frame.auth.permit.denied}");
                });
            }
        }
        // 删除
        esHelper.bulkDelete(SysOperation.INDEX_NAME, ids);
    }

    @Override
    public void clean(String tenantId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termsQuery("access.accessTenantId", tenantId));
        esHelper.deleteByQuery(SysOperation.INDEX_NAME, boolQuery);
    }

    private String getCurrentScope() {
        String permit = Access.permit();
        if (StringUtils.isBlank(permit)) {
            return null;
        }

        List<String> roleCodes = Access.userRoles();
        if (CollectionUtils.isEmpty(roleCodes) || roleCodes.contains(ROLE_ADMIN)) {
            return null;
        }

        List<SysScope> list = sysScopeDtoMapper.listScopeByPermit(permit, roleCodes);
        if (list.isEmpty()) {
            return null;
        }

        // 同一个permit，应该只选一个scope
        SysScope sysScope = list.get(0);
        Map<String, Object> scopeContent = sysScope.getScopeContent();
        if (MapUtils.isEmpty(scopeContent)) {
            return null;
        }
        return (String) scopeContent.get("scope");
    }
}
