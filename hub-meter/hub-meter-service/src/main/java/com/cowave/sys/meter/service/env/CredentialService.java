package com.cowave.sys.meter.service.env;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.meter.domain.env.EnvCredential;
import com.cowave.sys.meter.domain.env.request.CredentialQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface CredentialService {

    /**
     * 列表
     */
    Page<EnvCredential> list(CredentialQuery query);

    /**
     * 详情
     */
    EnvCredential info(Integer credentialId);

    /**
     * 新增
     */
    void create(EnvCredential credential);

    /**
     * 删除
     */
    void delete(List<Integer> credentialIds);

    /**
     * 修改
     */
    void edit(EnvCredential credential);
}
