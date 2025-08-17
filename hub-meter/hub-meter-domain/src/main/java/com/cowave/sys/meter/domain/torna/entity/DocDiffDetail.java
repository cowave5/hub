package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：doc_diff_detail
 * 备注：文档比较详情
 *
 * @author tanghc
 */
@Data
public class DocDiffDetail {

    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * doc_diff_record.id
     */
    private Long recordId;


    /**
     * 变更位置，0：文档名称，1：文档描述，2：contentType，3：httpMethod，10：参数名称，11：参数类型，12：参数必填，13：参数最大长度，14：参数描述，15：参数示例值
     */
    private Byte positionType;


    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 对比内容
     */
    private String content;


    /**
     * 变更类型，0：修改，1：新增，2：删除
     */
    private Byte modifyType;

    private LocalDateTime gmtCreate;


    private LocalDateTime gmtModified;



}
