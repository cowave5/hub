package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：doc_diff_record
 * 备注：文档比较记录
 *
 * @author tanghc
 */
@Data
public class DocDiffRecord {

    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * doc_info.id
     */
    private Long docId;

    /**
     * 一个接口对应一个key
     */
    private String docKey;


    /**
     * 旧MD5
     */
    private String md5Old;


    /**
     * 新MD5
     */
    private String md5New;


    /**
     * 修改方式，0：推送，1：表单编辑
     */
    private Byte modifySource;


    /**
     * 修改人id
     */
    private Long modifyUserId;


    /**
     * 修改人
     */
    private String modifyNickname;


    /**
     * 变更类型，0：修改，1：新增，2：删除
     */
    private Byte modifyType;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;


    private LocalDateTime gmtCreate;


    private LocalDateTime gmtModified;



}
