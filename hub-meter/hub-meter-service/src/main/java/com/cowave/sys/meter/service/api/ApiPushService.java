package com.cowave.sys.meter.service.api;

import com.cowave.sys.meter.domain.api.request.ApiFolderPush;

/**
 * @author shanhuiming
 */
public interface ApiPushService {

    void push(String apiToken, ApiFolderPush folderPush);
}
