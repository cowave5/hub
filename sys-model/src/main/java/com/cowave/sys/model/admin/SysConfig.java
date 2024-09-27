/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.model.admin;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.commons.framework.access.AccessUser;
import com.cowave.commons.framework.helper.dict.DictValueParser;
import com.cowave.commons.framework.support.excel.YesNoConverter;
import com.cowave.commons.tools.AssertsException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 系统配置
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
@EqualsAndHashCode(callSuper=false)
public class SysConfig extends AccessUser {

    /**
     * 参数主键
     */
    @ExcelProperty(value = "配置id")
    private Integer configId;

    /**
     * 参数名称
     */
    @ExcelProperty(value = "配置名称")
    @NotBlank(message = "{config.notnull.name}")
    private String configName;

    /**
     * 参数键名
     */
    @ColumnWidth(40)
    @ExcelProperty(value = "配置key")
    @NotBlank(message = "{config.notnull.key}")
    private String configKey;

    /**
     * 参数键值
     */
    @ExcelProperty(value = "配置值")
    @NotBlank(message = "{config.notnull.value}")
    private String configValue;

    /**
     * 值转换器
     */
    @ColumnWidth(60)
    @ExcelProperty(value = "值转换器")
    private String valueParser;

    /**
     * 转换参数
     */
    @ExcelProperty(value = "转换参数")
    private String valueParam;

    /**
     * 是否默认
     */
    @ExcelProperty(value = "系统内置", converter = YesNoConverter.class)
    private Integer isDefault;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新部门
     */
    private Long updateDept;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @ColumnWidth(80)
    @ExcelProperty(value = "备注")
    private String remark;

    public void validParser(){
        if(StringUtils.isBlank(valueParser)){
            return;
        }
        try{
            Class<?> parserClass = Class.forName(valueParser);
            if(!DictValueParser.class.isAssignableFrom(parserClass)){
                throw new AssertsException("config.invalid.parser");
            }
        }catch (ClassNotFoundException e){
            throw new AssertsException("config.notexist.parser");
        }
    }
}
