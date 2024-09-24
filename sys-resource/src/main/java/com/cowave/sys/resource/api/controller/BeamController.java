/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.resource.api.controller;

import com.cowave.sys.model.resource.Beam;
import com.cowave.sys.resource.api.service.BeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.feign.codec.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 波束
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beam")
public class BeamController {

    private final BeamService beamService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Valid @RequestBody Beam beam) {
        beamService.add(beam);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Valid @RequestBody Beam beam) {
        beamService.edit(beam);
        return Response.success();
    }

    /**
     * 删除
     * @param beamId 波束id
     */
    @GetMapping ("/remove")
    public Response<Void> remove(@NotNull(message = "beamId不能为空") Integer beamId) {
        beamService.remove(beamId);
        return Response.success();
    }

    /**
     * 详情
     * @param beamId 波束id
     */
    @GetMapping("/info")
    public Response<Beam> info(@NotNull(message = "beamId不能为空") Integer beamId) {
        return Response.success(beamService.info(beamId));
    }
}
