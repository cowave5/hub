package com.cowave.sys.job.client;

import com.cowave.commons.client.http.constants.HttpCode;
import com.cowave.commons.client.http.response.Response;
import com.cowave.sys.job.domain.client.ClientRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xuxueli/shanhuiming
 */
@RequiredArgsConstructor
@Slf4j
public class ClientRegister {

    private final ServerService serverService;

    private final  List<String> serverList;

    private volatile boolean toStop = false;

    private Thread registryThread;

    public void start(String clientName, String clientIp, int clientPort, Collection<String> handlerList){
        if(serverList == null || serverList.isEmpty()){
            return;
        }
        String clientAddress = "http://" + clientIp + ":"  + clientPort;
        ClientRegistry clientRegistry = new ClientRegistry(clientName, clientAddress, handlerList);
        registryThread = new Thread(() -> {
            while (!toStop) {
                for (String serverAddress : serverList) {
                    try {
                        Response<Void> response = serverService.registry("http://" + serverAddress, clientRegistry);
                        if(!HttpCode.SUCCESS.getCode().equals(response.getCode())){
                            log.error("job client registry error, {}", response.getMsg());
                        }
                    } catch (Throwable e) {
                        log.info("job client registry error", e);
                    }
                }

                try {
                    if (!toStop) {
                        TimeUnit.SECONDS.sleep(30);
                    }
                } catch (Throwable e) {
                    if (!toStop) {
                        log.warn(e.getMessage());
                    }
                }
            }

            try {
                for (String serverUrl : serverList) {
                    try {
                        Response<Void> response = serverService.unRegistry("http://" + serverUrl, clientRegistry);
                        if(!HttpCode.SUCCESS.getCode().equals(response.getCode())){
                            log.error("job client unRegistry error, {}", response.getMsg());
                        }
                    } catch (Throwable e) {
                        if (!toStop) {
                            log.info("job client unRegistry error", e);
                        }
                    }
                }
            } catch (Throwable e) {
                if (!toStop) {
                    log.error("", e);
                }
            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("job-client-register");
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        if (registryThread != null) {
            registryThread.interrupt();
            try {
                registryThread.join();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
