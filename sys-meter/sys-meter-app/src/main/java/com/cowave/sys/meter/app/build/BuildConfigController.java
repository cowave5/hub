package com.cowave.sys.meter.app.build;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 构建配置
 *
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/build/config")
public class BuildConfigController {

}
