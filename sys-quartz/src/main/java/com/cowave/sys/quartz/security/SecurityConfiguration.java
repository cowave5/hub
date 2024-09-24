/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.quartz.security;

import com.cowave.commons.framework.filter.security.TokenAuthenticationFilter;
import com.cowave.commons.framework.filter.security.TokenConfiguration;
import com.cowave.commons.framework.filter.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final TokenConfiguration tokenConfiguration;

	private final TokenService tokenService;

	private static final List<String> PERMIT_ALL = new ArrayList<>();

	static {
		PERMIT_ALL.add("/actuator/**");
		PERMIT_ALL.add("/druid/**");
		PERMIT_ALL.add("/doc/**");
		PERMIT_ALL.add("/fom/**");
	}

	private String[] permitAll(){
		PERMIT_ALL.addAll(tokenConfiguration.getIgnoreUrls());
		return PERMIT_ALL.toArray(new String[0]);
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.authorizeRequests().antMatchers(permitAll()).permitAll().anyRequest().authenticated();

		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenService);
		httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer(){
		return (web) -> web.ignoring().antMatchers(permitAll());
	}
}
