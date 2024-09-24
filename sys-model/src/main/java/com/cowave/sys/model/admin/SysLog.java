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
import com.alibaba.fastjson.annotation.JSONField;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.helper.operation.OperationLog;
import com.cowave.commons.framework.support.excel.DateConverter;
import com.cowave.commons.framework.support.excel.JsonConverter;
import com.cowave.commons.framework.support.excel.YesNoConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Data
public class SysLog implements OperationLog {

    public static final int SUCCESS = 1;

    public static final int FAIL = 0;

    /**
     * 日志id
     */
    @ExcelProperty("日志id")
    private Long id;

    /**
     * 模块编码
     */
    private String groupCode;

    /**
     * 日志模块
     */
    @ExcelProperty("日志模块")
    private String groupName;

    /**
     * 日志模块
     */
    private String groupEn;

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 日志类型
     */
    @ExcelProperty("日志类型")
    private String typeName;

    /**
     * 日志类型
     */
    private String typeEn;

    /**
     * 动作编码
     */
    private String actionCode;

    /**
     * 日志动作
     */
    @ExcelProperty("日志动作")
    private String actionName;

    /**
     * 日志动作
     */
    private String actionEn;

    /**
     * 表单key
     */
    private String viewKey;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
    private String deptName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    @ExcelProperty("用户名称")
    private String userName;

    /**
     * 访问ip
     */
    @ExcelProperty("访问ip")
    private String ip;

    /**
     * 访问url
     */
    @ColumnWidth(50)
    @ExcelProperty("访问url")
    private String url;

    /**
     * 日志时间
     */
    @ExcelProperty(value = "日志时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;

    /**
     * 日志状态
     */
    @ExcelProperty(value = "是否成功", converter = YesNoConverter.class)
    private Integer logStatus;

    /**
     * 日志描述
     */
    @ColumnWidth(60)
    @ExcelProperty("日志描述")
    private String logDesc;

    /**
     * 日志详情
     */
    @ColumnWidth(80)
    @ExcelProperty(value = "日志详情", converter = JsonConverter.class)
    private Map<String, Object> logContent;

    /**
     * 请求
     */
    private Map<String, Object> request;

    /**
     * 响应
     */
    private Object response;

    /**
     * 开始时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public void putContent(String key, Object obj) {
        if(logContent == null){
            logContent = new HashMap<>();
        }
        logContent.put(key, obj);
    }

    public void recordOperation(String type, String action, String desc){
        this.typeCode = type;
        this.actionCode = action;
        this.logDesc = desc;
    }

    public void initialize(){
        this.logTime = new Date();
        this.logStatus = SUCCESS;
        this.ip = Access.accessIp();
        this.url = Access.accessUrl();
        this.userId = Access.userId();
        this.deptId = Access.deptId();
    }

    public String getViewKey(){
        if(StringUtils.isBlank(viewKey)){
            return "log-detail";
        }
        return viewKey;
    }

    public String getGroupName() {
        if(StringUtils.isBlank(groupName)){
            return groupCode;
        }
        return groupName;
    }

    public String getGroupEn(){
        if(StringUtils.isBlank(groupEn)){
            return groupCode;
        }
        return groupEn;
    }

    public String getTypeName() {
        if(StringUtils.isBlank(typeName)){
            return typeCode;
        }
        return typeName;
    }

    public String getTypeEn() {
        if(StringUtils.isBlank(typeEn)){
            return typeCode;
        }
        return typeEn;
    }

    public String getActionName() {
        if(StringUtils.isBlank(actionName)){
            return actionCode;
        }
        return actionName;
    }

    public String getActionEn() {
        if(StringUtils.isBlank(actionEn)){
            return actionCode;
        }
        return actionEn;
    }
}
