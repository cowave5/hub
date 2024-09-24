/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.admin.api.entity.flow.Leave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface FlowLeaveMapper {

    /**
     * 序列id
     */
    long nextId();

    /**
     * 列表
     */
    Page<Leave> list(Page<Leave> page, @Param("leave") Leave leave);

    /**
     * 详情
     */
    Leave info(Long id);

    /**
     * 新增
     */
    int insert(Leave leave);

    /**
     * 修改
     */
    int update(Leave leave);

    /**
     * 删除
     */
    int delete(Long[] ids);

    /**
     * 修改流程状态
     */
    void changeProcessStatus(@Param("id") Long id, @Param("processStatus") Integer processStatus);
}
