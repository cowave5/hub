package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：attachment 备注：附件表
 *
 * @author tanghc
 */
@Data
public class Attachment {

    /**  */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** doc_id */
    private Long docId;

    /** 文件名称 */
    private String filename;

    /** 本地保存路径 */
    private String saveDir;

    /** 文件唯一id */
    private String fileId;

    /** 上传人 */
    private Long userId;

    /** module.id */
    private Long moduleId;

    /**  */
    private Byte isDeleted;

    /**  */
    private LocalDateTime gmtCreate;

    /**  */
    private LocalDateTime gmtModified;


}
