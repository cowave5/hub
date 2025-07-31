package com.cowave.sys.meter.app.build;

import com.cowave.sys.meter.service.build.BuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 构建
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/build")
public class BuildController {

    private final BuildService buildService;

    @GetMapping
    public void build() {
        buildService.build();
    }
}
