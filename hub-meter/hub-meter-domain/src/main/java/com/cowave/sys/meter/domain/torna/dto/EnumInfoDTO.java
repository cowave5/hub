package com.cowave.sys.meter.domain.torna.dto;

import com.cowave.sys.meter.domain.torna.bean.EnumInfoDataId;
import lombok.Data;

import java.util.List;

/**
 * @author tanghc
 */
@Data
public class EnumInfoDTO implements EnumInfoDataId {

    private Long id;

    /**
     * 枚举名称, 数据库字段：name
     */
    private String name;

    /**
     * 枚举说明, 数据库字段：description
     */
    private String description;

    private Long folderId;

    private List<EnumItemDTO> items;

}
