package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：doc_comment
 * 备注：文档评论表
 *
 * @author tanghc
 */
@Data
public class DocComment {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * doc_info.id
     */
    private Long docId;

    /**
     * 评论人
     */
    private Long userId;

    /**
     * 评论人昵称
     */
    private String nickname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 回复id，即：parent_id
     */
    private Long replyId;

    private Byte isDeleted;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
