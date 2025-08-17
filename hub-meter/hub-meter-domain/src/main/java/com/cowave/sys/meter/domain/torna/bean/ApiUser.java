package com.cowave.sys.meter.domain.torna.bean;

import com.cowave.sys.meter.domain.torna.enums.OperationMode;
import com.cowave.sys.meter.domain.torna.enums.UserStatusEnum;
import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class ApiUser implements User {

    private Long id = 99999L;

    private String nickname = "OpenAPI";

    @Override
    public Long getUserId() {
        return id;
    }

    @Override
    public byte getOperationModel() {
        return OperationMode.OPEN.getType();
    }

    @Override
    public boolean isSuperAdmin() {
        return false;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Byte getStatus() {
        return UserStatusEnum.ENABLE.getStatus();
    }

    @Override
    public String getToken() {
        return "";
    }
}
