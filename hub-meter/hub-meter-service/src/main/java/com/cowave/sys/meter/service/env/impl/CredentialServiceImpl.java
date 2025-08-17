package com.cowave.sys.meter.service.env.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.sys.meter.domain.env.EnvCredential;
import com.cowave.sys.meter.domain.env.request.CredentialQuery;
import com.cowave.sys.meter.infra.env.EnvCredentialDao;
import com.cowave.sys.meter.service.env.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.commons.client.http.constants.HttpCode.BAD_REQUEST;
import static com.cowave.commons.client.http.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CredentialServiceImpl implements CredentialService {

    private final EnvCredentialDao envCredentialDao;

    @Override
    public Page<EnvCredential> list(CredentialQuery query) {
        return envCredentialDao.pageList(query);
    }

    @Override
    public EnvCredential info(Integer credentialId) {
        return envCredentialDao.getById(credentialId);
    }

    @Override
    public void create(EnvCredential credential) {
        envCredentialDao.save(credential);
    }

    @Override
    public void delete(List<Integer> credentialIds) {
        envCredentialDao.removeBatchByIds(credentialIds);
    }

    @Override
    public void edit(EnvCredential credential) {
        HttpAsserts.notNull(credential.getCredentialId(), BAD_REQUEST, "凭据id不能为空");

        EnvCredential preCredential = envCredentialDao.getById(credential.getCredentialId());
        HttpAsserts.notNull(preCredential, NOT_FOUND, "不存在的凭据");

        envCredentialDao.updateById(credential);
    }
}
