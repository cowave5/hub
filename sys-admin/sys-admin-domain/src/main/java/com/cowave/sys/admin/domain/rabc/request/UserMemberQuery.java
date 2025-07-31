package com.cowave.sys.admin.domain.rabc.request;

import lombok.Data;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class UserMemberQuery {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户编码列表
     */
    private List<String> userCodes;
}
