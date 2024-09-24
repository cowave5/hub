/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.sys.blog.api.entity.NavigationInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface NavigationMapper extends BaseMapper<NavigationInfo> {

    /**
     * 查询导航栏
     */
    List<NavigationInfo> queryNavigations(NavigationInfo navigation);
}
