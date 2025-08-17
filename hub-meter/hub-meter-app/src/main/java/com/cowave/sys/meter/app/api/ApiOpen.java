package com.cowave.sys.meter.app.api;

import com.cowave.commons.client.http.asserts.Asserts;
import com.cowave.sys.meter.domain.api.request.ApiFolderPush;
import com.cowave.sys.meter.service.api.ApiPushService;
import com.gitee.easyopen.ApiContext;
import com.gitee.easyopen.annotation.Api;
import com.gitee.easyopen.annotation.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shanhuiming
 */
@Slf4j
@ApiService
@RequiredArgsConstructor
public class ApiOpen {

    private final ApiPushService apiPushService;

    @Api(name = "doc.push")
    public void push(@RequestBody ApiFolderPush folderPush) {
        String apiToken = ApiContext.getAccessToken();
        Asserts.notBlank(apiToken, "api token can't be empty");
        apiPushService.push(apiToken, folderPush);
    }
}
