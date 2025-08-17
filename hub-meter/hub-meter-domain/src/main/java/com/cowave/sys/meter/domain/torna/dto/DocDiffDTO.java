package com.cowave.sys.meter.domain.torna.dto;

import com.cowave.sys.meter.domain.torna.bean.User;
import com.cowave.sys.meter.domain.torna.enums.ModifySourceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author thc
 */
@Data
@AllArgsConstructor
public class DocDiffDTO {

    private String md5Old;
    private String md5New;
    private LocalDateTime modifyTime;

    private User user;

    private ModifySourceEnum modifySourceEnum;

}
