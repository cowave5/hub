/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.utils;

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
public class LdapUserNameMapper implements AttributesMapper<String> {

    @Override
    public String mapFromAttributes(Attributes attributes) throws NamingException {
        NamingEnumeration<? extends Attribute> attributeEnum = attributes.getAll();
        while (attributeEnum.hasMore()) {
            Attribute attribute = attributeEnum.next();
            if("displayName".equals(attribute.getID())){
               return attribute.get().toString();
            }
        }
        return "";
    }
}
