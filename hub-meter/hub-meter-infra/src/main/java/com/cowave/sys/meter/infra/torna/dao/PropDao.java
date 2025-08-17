package com.cowave.sys.meter.infra.torna.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.torna.entity.Prop;
import com.cowave.sys.meter.domain.torna.enums.PropTypeEnum;
import com.cowave.sys.meter.infra.torna.dao.mapper.PropMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PropDao extends ServiceImpl<PropMapper, Prop> {

    public Map<String, String> getDocProps(Long docId) {
        if (docId == null) {
            return Collections.emptyMap();
        }
        return getProps(docId, PropTypeEnum.DOC_INFO_PROP.getType());
    }

    public Map<String, String> getProps(Long refId, byte type) {
        return this.listProps(refId, type).stream().collect(Collectors.toMap(Prop::getName, Prop::getVal));
    }

    public List<Prop> listProps(Long refId, byte type) {
        if (refId == null) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(Prop::getRefId, refId)
                .eq(Prop::getType, type)
                .list();
    }

    public void saveProps(Map<String, ?> props, Long refId, PropTypeEnum type) {
        saveProps(props, refId, type.getType());
    }

    public void saveProps(Map<String, ?> props, Long refId, byte type) {
        if (props == null || props.isEmpty()) {
            return;
        }
        List<Prop> tobeSave = props.entrySet().stream().map(
                entry -> {
                    Prop prop = new Prop();
                    prop.setRefId(refId);
                    prop.setType(type);
                    prop.setName(entry.getKey());
                    prop.setVal(String.valueOf(entry.getValue()));
                    return prop;
                }).collect(Collectors.toList());
        saveBatch(tobeSave);
    }
}
