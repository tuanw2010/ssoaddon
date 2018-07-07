package com.bluecom.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tuanw on 2/14/17.
 */
public interface SsoAuthenticationSuccessHandler {

    void onAuthenticationSuccess(String username, HttpServletRequest request, HttpServletResponse response);
}
