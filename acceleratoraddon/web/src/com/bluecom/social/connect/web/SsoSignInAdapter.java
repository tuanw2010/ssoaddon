package com.bluecom.social.connect.web;

import com.bluecom.security.SsoAuthenticationSuccessHandler;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by tuan.vu on 2/9/17.
 */
public class SsoSignInAdapter implements SignInAdapter {

    private final RequestCache requestCache;

    private SsoAuthenticationSuccessHandler ssoAuthenticationSuccessHandler;

    public SsoSignInAdapter(RequestCache requestCache, SsoAuthenticationSuccessHandler ssoAuthenticationSuccessHandler) {
        this.requestCache = requestCache;
        this.ssoAuthenticationSuccessHandler = ssoAuthenticationSuccessHandler;
    }

    @Override
    public String signIn(final String localUserId, final Connection<?> connection, final NativeWebRequest request) {
        HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
        this.getSsoAuthenticationSuccessHandler().onAuthenticationSuccess(localUserId, nativeReq, nativeRes);
        return extractOriginalUrl(request);
    }

    // Helper method

    private String extractOriginalUrl(final NativeWebRequest request) {
        HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
        final SavedRequest saved = this.requestCache.getRequest(nativeReq, nativeRes);
        if (saved == null) {
            return null;
        }
        this.requestCache.removeRequest(nativeReq, nativeRes);
        removeAutheticationAttributes(nativeReq.getSession(false));
        return saved.getRedirectUrl();
    }

    private void removeAutheticationAttributes(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    // Getter and setter

    public SsoAuthenticationSuccessHandler getSsoAuthenticationSuccessHandler() {
        return ssoAuthenticationSuccessHandler;
    }

    public void setSsoAuthenticationSuccessHandler(SsoAuthenticationSuccessHandler ssoAuthenticationSuccessHandler) {
        this.ssoAuthenticationSuccessHandler = ssoAuthenticationSuccessHandler;
    }
}
