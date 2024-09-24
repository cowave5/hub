/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
public class LdapAttributesMapper implements AttributesMapper<LdapUser> {

    private final LdapConfig ldapConfig;

    @Override
    public LdapUser mapFromAttributes(Attributes attributes) throws NamingException {
        LdapUser ldapUser = new LdapUser();
        NamingEnumeration<? extends Attribute> attributeEnum = attributes.getAll();
        while (attributeEnum.hasMore()) {
            Attribute attribute = attributeEnum.next();
            ldapUser.valueUserAccount(attribute, ldapConfig);
            ldapUser.valueUserName(attribute, ldapConfig);
            ldapUser.valueUserEmail(attribute, ldapConfig);
            ldapUser.valueUserPhone(attribute, ldapConfig);
            ldapUser.valueUserPost(attribute, ldapConfig);
            ldapUser.valueDepartment(attribute, ldapConfig);
            ldapUser.valueUserLeader(attribute, ldapConfig);
            ldapUser.valueUserInfo(attribute, ldapConfig);
        }
        return ldapUser;
    }
}
