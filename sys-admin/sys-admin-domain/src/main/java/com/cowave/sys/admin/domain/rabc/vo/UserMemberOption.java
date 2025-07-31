package com.cowave.sys.admin.domain.rabc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMemberOption {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 成员名称
     */
    private String userName;

    /**
     * 成员角色
     */
    private int userRole;
}
