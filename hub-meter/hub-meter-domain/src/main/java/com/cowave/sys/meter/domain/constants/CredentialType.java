package com.cowave.sys.meter.domain.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.commons.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum CredentialType implements EnumVal<Integer> {

    /**
     * 用户名/密码
     */
    USERNAME_PASSWD(0),

    /**
     * 密文
     */
    SECRET_TEXT(1);

    @EnumValue
    @JsonValue
    private final Integer val;
}
