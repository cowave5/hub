package com.cowave.sys.job.domain.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * @author xuxueli/shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientRegistry {

    /**
     * 客户端名称
     */
    @NotBlank(message = "clientName can't be empty")
    private String clientName;

    /**
     * 客户端地址
     */
    @NotBlank(message = "clientAddress can't be empty")
    private String clientAddress;

    /**
     * 执行器列表
     */
    private Collection<String> handlerList;
}
