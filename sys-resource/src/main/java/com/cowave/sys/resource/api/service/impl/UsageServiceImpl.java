/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.service.impl;

import com.cowave.commons.framework.access.AccessUser;
import com.cowave.commons.tools.Asserts;
import com.cowave.commons.tools.AssertsException;
import com.cowave.commons.tools.DateUtils;
import com.cowave.sys.model.resource.ResourcePool;
import com.cowave.sys.model.resource.ResourceUsage;
import com.cowave.sys.resource.api.entity.task.Usage;
import com.cowave.sys.resource.api.mapper.PoolMapper;
import com.cowave.sys.resource.api.mapper.UsageMapper;
import com.cowave.sys.resource.api.service.UsageService;
import com.cowave.sys.resource.doc.PoolStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class UsageServiceImpl implements UsageService {

    private final PoolMapper poolMapper;

    private final UsageMapper usageMapper;

    @Override
    public List<ResourceUsage> list(Long usageId) {
        return usageMapper.list(usageId);
    }

    @Override
    public List<ResourceUsage> conflictList(Usage usage) {
        return usageMapper.conflictList(usage, usage.getTaskId(), usage.getNetNo());
    }

    @Override
    public  ResourceUsage allocateUsage(ResourceUsage allocateUsage, Usage usage, String taskId, String netNo, AccessUser user) {
        // 将一个usage分散到多个pool片段上进行切割，然后在进行merge
        List<ResourcePool> pools = poolMapper.availablePool(usage);
        List<Usage> srcUsages = usageSplit(pools, usage);
        Asserts.notEmpty(srcUsages, "转发器资源无法满足, 转发器: " + usage.getTspdId()
                + " 频段: " + usage.getFreqBegin() + "~" + usage.getFreqEnd() + "hz"
                + " 时间: " + DateUtils.format(usage.getTimeBegin()) + "~" + DateUtils.format(usage.getTimeEnd()));

        List<Long> splitIds = new ArrayList<>();
        for (Usage srcUsage : srcUsages) {
            poolMapper.updateStatus(srcUsage.getSrcId(), PoolStatus.OCCUPY.getCode());
            List<ResourceUsage> conflicts = usageMapper.conflictList(srcUsage, taskId, netNo);
            if (!conflicts.isEmpty()) {
                ResourceUsage conflict = conflicts.get(0);
                throw new AssertsException("资源使用存在冲突, 任务: " + conflict.getTaskName() + " 子网: " + conflict.getNetNo());
            }

            allocateUsage.setSrcId(srcUsage.getSrcId());
            allocateUsage.setDownFreqBegin(srcUsage.getFreqBegin());
            allocateUsage.setDownFreqEnd(srcUsage.getFreqEnd());
            allocateUsage.setTimeBegin(srcUsage.getTimeBegin());
            allocateUsage.setTimeEnd(srcUsage.getTimeEnd());
            usageMapper.createUsage(allocateUsage, user);
            splitIds.add(allocateUsage.getId());
        }

        if (splitIds.size() > 1) {
            allocateUsage.setDownFreqBegin(usage.getFreqBegin());
            allocateUsage.setDownFreqEnd(usage.getFreqEnd());
            usageMapper.createMergeUsage(allocateUsage);
            usageMapper.updateMergeId(splitIds, allocateUsage.getId());
        }
        return usageMapper.queryById(allocateUsage.getId());
    }

    @Override
    public List<Usage> usageSplit(List<ResourcePool> pools, Usage usage) {
        List<List<Usage>> schemes = new ArrayList<>();
        for (int i = 0; i < pools.size(); i++) {
            ResourcePool pool = pools.get(i);
            if (pool.getDownFreqBegin() > usage.getFreqBegin()) {
                break;
            }

            List<Usage> srcUsages = new ArrayList<>();
            Usage srcUsage = usage.clone();
            if (pool.getDownFreqEnd() >= usage.getFreqEnd()) {
                srcUsage.setFreqBegin(usage.getFreqBegin());
                srcUsage.setFreqEnd(usage.getFreqEnd());
                srcUsage.setSrcId(pool.getSrcId());
                srcUsages.add(srcUsage);
                return srcUsages;
            } else {
                srcUsage.setFreqBegin(usage.getFreqBegin());
                srcUsage.setFreqEnd(pool.getDownFreqEnd());
                srcUsage.setSrcId(pool.getSrcId());
                srcUsages.add(srcUsage);

                Usage splitUsage = usage.clone();
                splitUsage.setFreqBegin(pool.getDownFreqEnd());
                srcUsages.addAll(usageSplit(pools.subList(i + 1, pools.size()), splitUsage));
            }
            Usage endUsage = srcUsages.get(srcUsages.size() - 1);
            if (Objects.equals(endUsage.getFreqEnd(), usage.getFreqEnd())) {
                schemes.add(srcUsages);
            }
        }
        if (schemes.isEmpty()) {
            return new ArrayList<>();
        }
        schemes.sort(Comparator.comparingInt(List::size));
        return schemes.get(0);
    }
}
