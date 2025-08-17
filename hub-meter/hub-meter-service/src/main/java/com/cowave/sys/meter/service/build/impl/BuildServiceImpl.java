package com.cowave.sys.meter.service.build.impl;

import com.cowave.sys.meter.core.build.BuildTask;
import com.cowave.sys.meter.infra.build.mapper.dto.BuildDtoMapper;
import com.cowave.sys.meter.service.build.BuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BuildServiceImpl implements BuildService {

    private final BuildDtoMapper buildDtoMapper;

    @Override
    public void build() {
        String buildName = "test";
        Long buildIndex = buildDtoMapper.nextIndexOfBuild(1);
        File workHome = new File(System.getProperty("user.dir")
                + File.separator + "workHome" + File.separator + buildName + "@" + buildIndex);
        if(!workHome.mkdirs()){
            throw new RuntimeException("dir failed");
        }

        new BuildTask(workHome).run();
    }
}
