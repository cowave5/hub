package com.cowave.sys.meter.domain.torna.dto;

import com.cowave.sys.meter.domain.torna.enums.UserSubscribeTypeEnum;
import com.cowave.sys.meter.domain.torna.message.MessageEnum;
import lombok.Data;

import java.util.Locale;

/**
 * @author tanghc
 */
@Data
public class MessageDTO {
    private MessageEnum messageEnum;
    private UserSubscribeTypeEnum type;
    private Long sourceId;
    private Locale locale;
}
