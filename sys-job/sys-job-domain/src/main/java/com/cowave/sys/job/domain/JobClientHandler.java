package com.cowave.sys.job.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuxueli/shanhuiming
 */
@NoArgsConstructor
@Data
public class JobClientHandler {

    private Integer id;

    private Integer clientId;

    private String clientHandler;

    public JobClientHandler(Integer clientId, String clientHandler){
        this.clientId = clientId;
        this.clientHandler = clientHandler;
    }
}
