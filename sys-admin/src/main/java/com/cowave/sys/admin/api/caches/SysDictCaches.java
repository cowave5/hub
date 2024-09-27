/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.caches;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.AccessLogger;
import com.cowave.commons.framework.helper.dict.DictHelper;
import com.cowave.commons.framework.helper.dict.DictValueParser;
import com.cowave.commons.tools.AssertsException;
import com.cowave.sys.admin.api.mapper.SysDictMapper;
import com.cowave.sys.model.admin.SysDict;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class SysDictCaches implements ApplicationRunner {

    private final Map<String, DictValueParser> parserMap = new ConcurrentHashMap<>();

    private final DictHelper dictHelper;

    private final SysDictMapper sysDictMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refresh();
    }

    public void refresh() throws Exception {
        AccessLogger.info("refresh cache of dict ...");
        dictHelper.clear();
        List<SysDict> list = sysDictMapper.list(Access.page(Integer.MAX_VALUE), null, null).getRecords();
        for(SysDict sysDict : list) {
            String parserClazz = sysDict.getValueParser();
            if(StringUtils.isBlank(parserClazz)){
                dictHelper.put(sysDict);
            }else{
                DictValueParser parser = parserMap.get(parserClazz);
                if(parser == null){
                    parser = (DictValueParser)Class.forName(parserClazz).getDeclaredConstructor().newInstance();
                    parserMap.put(parserClazz, parser);
                }
                sysDict.setDictValue(parser.parse(String.valueOf(sysDict.getDictValue()), sysDict.getValueParam()));
                dictHelper.put(sysDict);
            }
        }
    }

    public void parseValue(SysDict sysDict) {
        String valueParser = sysDict.getValueParser();
        if(StringUtils.isBlank(valueParser)){
            return;
        }
        DictValueParser parser = parserMap.get(valueParser);
        try{
            if(parser == null){
                Class<?> parserClass = Class.forName(valueParser);
                parser = (DictValueParser)parserClass.getDeclaredConstructor().newInstance();
                parserMap.put(valueParser, parser);
            }
            sysDict.setDictValue(parser.parse(String.valueOf(sysDict.getDictValue()), sysDict.getValueParam()));
        }catch(Exception e){
            throw new AssertsException("{dict.parse.failed}");
        }
    }

    public void put(SysDict sysDict){
        if(sysDict.getStatus() == 1){
            dictHelper.put(sysDict);
        }
    }

    public void removeDict(String dictCode){
        dictHelper.removeDict(dictCode);
    }

    public void removeType(String typeCode){
        dictHelper.removeType(typeCode);
    }

    public void removeGroup(String groupCode){
        dictHelper.removeGroup(groupCode);
    }

    public SysDict getDictHelper(String dictCode) {
        return dictHelper.getDict(dictCode);
    }

    public List<SysDict> getType(String typeCode) {
        return dictHelper.getType(typeCode);
    }

    public List<SysDict> getGroup(String groupCode) {
        return dictHelper.getGroup(groupCode);
    }
}
