/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.blog.api.controller;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.BearerTokenService;
import com.cowave.hub.admin.client.AdminOAuthClient;
import com.cowave.hub.admin.domain.auth.dto.UserProfile;
import com.cowave.hub.admin.domain.auth.request.OAuth2TokenRequest;
import com.cowave.sys.blog.api.cache.BlogCache;
import com.cowave.sys.blog.api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * 认证
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final Random rand = new Random();
    private final AdminOAuthClient adminOAuthClient;
    private final BearerTokenService bearerTokenService;
    private final BlogService blogService;
    private final BlogCache blogCache;

    @RequestMapping("/callback")
    public String authCallback(String code,
                               HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
        OAuth2TokenRequest tokenRequest = new OAuth2TokenRequest();
        tokenRequest.setCode(code);
        tokenRequest.setRedirectUri("http://10.64.4.74/auth/callback");
        tokenRequest.setClientId("6ac6519451ed4ef09431aacccbcb1f5f");
        tokenRequest.setClientSecret("4a2e671fbd074f238e80c7f5566f8f7a");
        AccessUserDetails userDetails =
                adminOAuthClient.getAuthorizeToken("http://10.64.4.74:19000", tokenRequest);
        UserProfile userProfile =
                adminOAuthClient.getUserProfile("http://10.64.4.74:19000", userDetails.getAccessToken());
        // cookie设置
        Access.setCookie("blog-userName", userProfile.getUserAccount());
        bearerTokenService.assignAccessToken(userDetails);
        // 头像
        if(StringUtils.isBlank(userProfile.getAvatar())){
            avatarGenerate(userProfile.getUserName(), userProfile.getUserAccount());
        }else{
            URL url = new URL(userProfile.getAvatar());
            IOUtils.copy(url, new File("public/avatar/" + userProfile.getUserAccount() + ".jpg"));
        }

        // 删除Html缓存
        blogCache.removeHtmlOfPrefix("blog-");
        return blogService.index(request, response, modelMap);
    }

    private void avatarGenerate(String userName, String userAccount) throws IOException {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(userAccount)){
            return;
        }
        File avatar = new File("public/avatar/" + userAccount + ".jpg");
        if(avatar.exists()){
            return;
        }
        String text = userName.substring(0, 1);
        int width = 128;
        int height = 128;
        // Create a buffered image with a specific size and type
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        // Enable antialiasing for text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw a white background
        Color backgroundColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);
        // Set the font to use for drawing the string
        double bgLuminance = 0.299 * backgroundColor.getRed() + 0.587 * backgroundColor.getGreen() + 0.114 * backgroundColor.getBlue();
        Color textColor;
        if (bgLuminance < 128) {
            textColor = Color.WHITE;
        } else {
            textColor = Color.BLACK;
        }
        Font font = new Font("宋体", Font.BOLD, 64);
        g2d.setFont(font);
        g2d.setColor(textColor);

        // Get font metrics for the string to be drawn
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(text, context);
        // Calculate the coordinates to center the text
        int x = (width - (int) bounds.getWidth()) / 2;
        int y = (height - (int) bounds.getHeight()) / 2 - (int) bounds.getY();
        // Draw the string
        g2d.drawString(text, x, y);
        // Save the image as JPG
        File avatarDir = new File("public/avatar");
        if (avatarDir.exists() || avatarDir.mkdirs()) {
            ImageIO.write(image, "jpg", avatar);
        }
        // Dispose the graphics object
        g2d.dispose();
    }
}
