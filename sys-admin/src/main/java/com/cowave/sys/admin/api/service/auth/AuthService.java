/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.hutool.crypto.digest.DigestUtil;
import com.cowave.commons.framework.filter.security.AccessToken;
import com.cowave.commons.framework.filter.security.TokenService;
import com.cowave.commons.tools.AssertsException;
import com.cowave.sys.admin.api.caches.SysConfigCaches;
import com.cowave.sys.admin.api.entity.Route;
import com.cowave.sys.admin.api.entity.RouteMeta;
import com.cowave.sys.admin.api.entity.UserRegister;
import com.cowave.sys.admin.api.mapper.ProfileMapper;
import com.cowave.sys.admin.api.service.LdapService;
import com.cowave.sys.admin.api.service.SysMenuService;
import com.cowave.sys.model.admin.SysMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cowave.commons.framework.access.Access;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final SysMenuService sysMenuService;

    private final SysConfigCaches sysConfigCaches;

    private final ProfileMapper profileMapper;

    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    private final LdapService ldapService;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(UserRegister userRegister) {
        String initPasswd = sysConfigCaches.getValue("sys.user.initPassword");
        if(StringUtils.isBlank(initPasswd)){
            initPasswd = "123456";
        }
        userRegister.setUserPasswd(bcryptPasswordEncoder.encode(initPasswd));
        userRegister.setUserCode(DigestUtil.md5Hex(userRegister.getUserAccount()));
        profileMapper.register(userRegister);
        profileMapper.initRole(userRegister.getUserId());
        profileMapper.initHistoryNotice(userRegister.getUserId());
        profileMapper.updateNoticeStat();
        return initPasswd;
    }

    /**
     * 登录
     */
    public AccessToken login(String userAccount, String passWord) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, passWord));
        } catch (BadCredentialsException e) {
            throw new AssertsException("{auth.passwd}");
        } catch (InternalAuthenticationServiceException e) {
        	Throwable cause = e.getCause();
            if(cause != null){
                if(cause instanceof UserNotFoundException) {
                    AccessToken accessToken = ldapService.authenticate(userAccount, passWord);
                    if(accessToken == null){
                        throw new AssertsException("{user.notexist}").args(userAccount);
                    }
                    return accessToken;
                }
                if(cause instanceof AssertsException){
                    throw (AssertsException)cause;
                }
            }
        	throw new AssertsException("{auth.failed}", e);
        } catch (Exception e) {
            throw new AssertsException("{auth.failed}", e);
        }
        return (AccessToken) authentication.getPrincipal();
    }

    /**
     * 退出
     */
    public void logout(HttpServletResponse response) throws IOException {
    	 String token = Access.tokenAccessValue();
    	 if(token != null) {
    		 tokenService.deleteToken(response, token);
    	 }
    }

    /**
     * 刷新令牌
     */
    public void refresh(HttpServletResponse response, String refreshToken) throws Exception{
        if(StringUtils.isBlank(refreshToken)){
            return;
        }
        tokenService.refreshToken(response, refreshToken);
    }

    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu parent) {
        List<SysMenu> childs = new ArrayList<>();
        for (SysMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                childs.add(child);
            }
        }
        return childs;
    }

    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    private void recursionFn(List<SysMenu> list, SysMenu menu) {
        List<SysMenu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (SysMenu child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    /**
     * 路由信息
     */
    public List<Route> routes(){
        List<SysMenu> list;
        if(!Access.userIsAdmin() && AccessToken.TYPE_OAUTH.equals(Access.tokenType())){
            list = sysMenuService.oauthMenus(Access.userId());
        }else{
            list = sysMenuService.list(null, 1, 1, true);
        }

        List<SysMenu> rootMenus = new ArrayList<>();
        for(SysMenu menu : list){
            if (menu.getParentId() == 0) {
                recursionFn(list, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    public List<Route> buildRoutes(List<SysMenu> menus){
        List<Route> routes = new LinkedList<>();
        for (SysMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && "M".equals(menu.getMenuType())) {
                route.setAlwaysShow(true);
                route.setRedirect("noRedirect");
                route.setChildren(buildRoutes(cMenus));
            } else if (menu.ifMenuFrame()) {
                route.setMeta(null);
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                children.setPath(menu.getMenuPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getMenuPath()));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));
                children.setQuery(menu.getMenuParam());
                childrenList.add(children);
                route.setChildren(childrenList);
            } else if (menu.getParentId() == 0L && menu.ifInnerLink()) {
                route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon()));
                route.setPath("/");
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                String routerPath = menu.getMenuPath();
                routerPath = routerPath.replace("http://", "");
                routerPath = routerPath.replace("https://", "");
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), menu.getMenuPath()));
                childrenList.add(children);
                route.setChildren(childrenList);
            }
            routes.add(route);
        }
        return routes;
    }
}
