package com.bluecom.social.connect.utils;

import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by tuan.vu on 2/16/17.
 */
@Component
public class SsoSignupUtils {

    private static final String DEFAULT_TITLE_CODE = "mr";
    private static final String DEFAULT_FIRST_NAME = "weixin";
    private static final String DEFAULT_LAST_NAME = "qq";

    public static final RegisterData gernerateRegisterData(final Connection<?> connection) {
        final UserProfile userProfile = connection.fetchUserProfile();
        RegisterData data = new RegisterData();
        data.setFirstName(getFirstName(userProfile));
        data.setLastName(getLastName(userProfile));
        data.setLogin(getEmail(userProfile));
        data.setPassword(UUID.randomUUID().toString());
        data.setTitleCode(DEFAULT_TITLE_CODE);
        return data;
    }

    protected static String getEmail(final UserProfile userProfile) {
        if (StringUtils.isNotBlank(userProfile.getEmail())) {
            return userProfile.getEmail();
        } else {
            final String fakeEmail = UUID.randomUUID().toString().replaceAll("-", "");
            return fakeEmail + "@qq.com";
        }
    }

    protected static String getFirstName(final UserProfile userProfile) {
        if (StringUtils.isNotBlank(userProfile.getFirstName())) {
            return userProfile.getFirstName();
        } else if (StringUtils.isNotBlank(userProfile.getName())) {
            final StringTokenizer token = new StringTokenizer(userProfile.getName());
            if (token.countTokens() > 1) {
                return token.nextToken();
            } else {
                return userProfile.getName();
            }
        } else {
            return DEFAULT_FIRST_NAME;
        }
    }

    protected static String getLastName(final UserProfile userProfile) {
        if (StringUtils.isNotBlank(userProfile.getLastName())) {
            return userProfile.getLastName();
        } else if (StringUtils.isNotBlank(userProfile.getName())) {
            final StringTokenizer token = new StringTokenizer(userProfile.getName());
            if (token.countTokens() > 1) {
                token.nextToken();
                return token.nextToken();
            } else {
                return userProfile.getName();
            }
        } else {
            return DEFAULT_LAST_NAME;
        }
    }

}