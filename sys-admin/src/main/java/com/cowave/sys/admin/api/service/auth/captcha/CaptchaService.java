/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.auth.captcha;

import cn.hutool.core.util.IdUtil;
import com.cowave.commons.framework.support.redis.RedisHelper;
import com.cowave.commons.tools.Asserts;
import com.cowave.commons.tools.Messages;
import com.cowave.sys.admin.api.caches.SysConfigCaches;
import com.cowave.sys.admin.api.entity.UserRegister;
import com.cowave.sys.admin.api.entity.oauth.OAuthConfig;
import com.cowave.sys.admin.api.service.OAuthService;
import com.cowave.sys.model.admin.Login;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class CaptchaService {
    private static final int     LOOKUPLENGTH         = 64;
    private static final int     TWENTYFOURBITGROUP   = 24;
    private static final int     EIGHTBIT             = 8;
    private static final int     SIXTEENBIT           = 16;
    private static final int     SIGN                 = -128;
    private static final char    PAD                  = '=';
    private static final char[] LOOKUP_BASE64_ALPHABET = new char[LOOKUPLENGTH];
    private static final String CAPTCHA_KEY = "captcha:";
    private static final Integer CAPTCHA_EXPIRATION = 3;

    static {
        for (int i = 0; i <= 25; i++) {
            LOOKUP_BASE64_ALPHABET[i] = (char) ('A' + i);
        }
        for (int i = 26, j = 0; i <= 51; i++, j++) {
            LOOKUP_BASE64_ALPHABET[i] = (char) ('a' + j);
        }
        for (int i = 52, j = 0; i <= 61; i++, j++) {
            LOOKUP_BASE64_ALPHABET[i] = (char) ('0' + j);
        }
        LOOKUP_BASE64_ALPHABET[62] = '+';
        LOOKUP_BASE64_ALPHABET[63] = '/';
    }

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final SysConfigCaches sysConfigCaches;

    private final RedisHelper redisHelper;

    private final JavaMailSender mail;

    private final OAuthService oAuthService;

    private final SecureRandom random = new SecureRandom();

    public CaptchaInfo captcha() throws IOException {
        List<OAuthConfig> oauthList = oAuthService.listConfig();
        String gitlabOauthUrl = null;
        if(CollectionUtils.isNotEmpty(oauthList)){ // 目前只有一个授权服务
            gitlabOauthUrl = oauthList.get(0).oauthUrl();
        }
        boolean registerOnOff = sysConfigCaches.getValue("sys.account.registerOnOff");
        boolean captchaOnOff = sysConfigCaches.getValue("sys.account.captchaOnOff");
        if (!captchaOnOff) {
            return new CaptchaInfo(registerOnOff, gitlabOauthUrl);
        }

        String uuid = IdUtil.randomUUID();
        String capStr, code = null;
        BufferedImage image = null;
        // 生成验证码
        String captchaType = sysConfigCaches.getValue("sys.account.captchaType");
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisHelper.putExpireValue(CAPTCHA_KEY + uuid, code, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        assert image != null;
        ImageIO.write(image, "jpg", os);
        return new CaptchaInfo(uuid, encode(os.toByteArray()), true, registerOnOff, gitlabOauthUrl);
    }

    public void captchaEmail(String email) {
        int code = (random.nextInt() * 9 + 1) * 100000;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("cowaveAdmin@163.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(Messages.msg("captcha.title"));
        mailMessage.setText(Messages.msg("captcha.msg", String.valueOf(code), CAPTCHA_EXPIRATION));
        mail.send(mailMessage);
        redisHelper.putExpireValue(CAPTCHA_KEY + code, email, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
    }

    public void valid(Login login){
        boolean captchaOnOff = sysConfigCaches.getValue("sys.account.captchaOnOff");
        if(captchaOnOff){
            String captcha = redisHelper.getValue(CAPTCHA_KEY + login.getCaptchaId());
            Asserts.notNull(captcha, "{captcha.expired}");
            Asserts.equals(captcha, login.getCaptcha(), "{captcha.failed}");
        }
    }

    public void validEmail(UserRegister userRegister){
        boolean registerOnOff = sysConfigCaches.getValue("sys.account.registerOnOff");
        Asserts.isTrue(registerOnOff, "{register.disable}");
        String email = redisHelper.getValue(CAPTCHA_KEY + userRegister.getCaptcha());
        Asserts.notNull(email, "{captcha.expired}");
        Asserts.equals(email, userRegister.getUserEmail(), "{register.failed}");
    }

    private static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = new char[numberQuartet * 4];

        byte k, l, b1, b2, b3;
        int encodedIndex = 0;
        int dataIndex = 0;

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(val2 & 0xFF) | (k << 4)];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(l << 2) | (val3 & 0xFF)];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(val2 & 0xFF) | (k << 4)];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[l << 2];
            encodedData[encodedIndex++] = PAD;
        }
        return new String(encodedData);
    }
}
