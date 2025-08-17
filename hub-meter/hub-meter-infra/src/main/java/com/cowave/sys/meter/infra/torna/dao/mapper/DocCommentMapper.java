package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.DocComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：doc_comment
 * 备注：文档评论表
 *
 * @author tanghc
 */
@Mapper
public interface DocCommentMapper extends BaseMapper<DocComment> {

}
