/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.auth;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * UsernameNotFoundException在里面会被catch掉，并重新抛出BadCredentialsException，所以这里自己加个异常
 *
 * @author shanhuiming
 *
 */
public class UserNotFoundException extends AuthenticationException {

    /**
     * Constructs a <code>UsernameNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public UserNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code UsernameNotFoundException} with the specified message and root
     * cause.
     * @param msg the detail message.
     * @param cause root cause
     */
    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
