package com.cowave.sys.job.domain;

import com.cowave.sys.job.domain.client.ClientRegistry;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuxueli/shanhuiming
 */
@NoArgsConstructor
@Data
public class JobClient {

    private Integer id;

    private String clientName;

    private String clientAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public JobClient(ClientRegistry clientRegistry){
        this.clientName = clientRegistry.getClientName();
        this.clientAddress = clientRegistry.getClientAddress();
        this.updateTime = new Date();
    }
}
