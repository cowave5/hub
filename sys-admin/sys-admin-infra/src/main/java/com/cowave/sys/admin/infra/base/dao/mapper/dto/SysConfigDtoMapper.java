package com.cowave.sys.admin.infra.base.dao.mapper.dto;

import org.apache.ibatis.annotations.Mapper;

/**
 * 配置
 *
 * @author shanhuiming
 */
@Mapper
public interface SysConfigDtoMapper {

    void resetTenantConfig(String tenantId);
}
