package com.cowave.sys.meter.infra.torna.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.meter.domain.torna.entity.DocDiffRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表名：doc_diff_record
 * 备注：文档比较记录
 *
 * @author tanghc
 */
@Mapper
public interface DocDiffRecordMapper extends BaseMapper<DocDiffRecord> {

}
