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
import com.cowave.sys.admin.api.entity.flow.Meeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface FlowMeetingMapper {

    /**
     * 序列id
     */
    long nextId();

    /**
     * 列表
     */
    Page<Meeting> list(Page<Meeting> page, @Param("meeting") Meeting meeting);

    /**
     * 详情
     */
    Meeting info(Long id);

    /**
     * 新增
     */
    void insert(Meeting meeting);

    /**
     * 修改
     */
    void update(Meeting meeting);

    /**
     * 删除
     */
    int delete(Long[] ids);

    /**
     * 修改流程状态
     */
    void changeProcessStatus(@Param("id") Long id, @Param("processStatus") Integer processStatus);
}
