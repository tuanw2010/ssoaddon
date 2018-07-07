package com.bluecom.security.impl;

import com.bluecom.security.SsoAuthenticationSuccessHandler;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.acceleratorstorefrontcommons.strategy.CartRestorationStrategy;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by tuan.vu on 2/10/17.
 */
public class DefaultSsoAuthenticationSuccessHandler implements SsoAuthenticationSuccessHandler {

    private static final Logger LOG = Logger.getLogger(DefaultSsoAuthenticationSuccessHandler.class);

    private RequestCache requestCache;
    private CustomerFacade customerFacade;
    private MessageSourceAccessor messages;
    private RememberMeServices rememberMeServices;
    private UserDetailsService userDetailsService;
    private GUIDCookieStrategy guidCookieStrategy;
    private CartRestorationStrategy cartRestorationStrategy;
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    public DefaultSsoAuthenticationSuccessHandler() {
        requestCache = new HttpSessionRequestCache();
        messages = SpringSecurityMessageSource.getAccessor();
    }

    @Override
    public void onAuthenticationSuccess(final String username, final HttpServletRequest request,
                                        final HttpServletResponse response)
    {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
        authentication.setDetails(new WebAuthenticationDetails(request));

        try
        {
            if (Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized()) {
                UserDetails userDetails = null;
                try {
                    userDetails = this.retrieveUser(username);
                } catch (final UsernameNotFoundException e) {
                    throw new BadCredentialsException(this.messages.getMessage("CoreAuthenticationProvider.badCredentials",
                            "Bad credentials"), e);
                }
                final User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
                JaloSession.getCurrentSession().setUser(user);
                final Authentication authResult = this.createSuccessAuthentication(authentication, userDetails);
                SecurityContextHolder.getContext().setAuthentication(authResult);
                getSessionAuthenticationStrategy().onAuthentication(authResult, request, response);
                getRememberMeServices().loginSuccess(request, response, authResult);
                getGuidCookieStrategy().setCookie(request, response);

                restoreCustomerCart(request);

                final SavedRequest savedRequest = this.requestCache.getRequest(request, response);
                if(savedRequest == null) {
                    clearAuthenticationAttributes(request);
                }

            }

        }
        catch (final Exception e)
        {
            SecurityContextHolder.getContext().setAuthentication(null);
            LOG.error("Failure during autoLogin", e);
        }
    }

    // Helper method

    protected void restoreCustomerCart(HttpServletRequest request) {
        getCustomerFacade().loginSuccess();
        request.setAttribute(WebConstants.CART_MERGED, Boolean.FALSE);
        getCartRestorationStrategy().restoreCart(request);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
    }

    protected final UserDetails retrieveUser(String username) throws AuthenticationException {
        final UserDetails loadedUser;
        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        } catch (final DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        if (loadedUser == null) {
            throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        } else {
            return loadedUser;
        }
    }

    // Getter and setter

    protected RememberMeServices getRememberMeServices()
    {
        return rememberMeServices;
    }

    @Required
    public void setRememberMeServices(final RememberMeServices rememberMeServices)
    {
        this.rememberMeServices = rememberMeServices;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    @Required
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        return sessionAuthenticationStrategy;
    }

    @Required
    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    public GUIDCookieStrategy getGuidCookieStrategy() {
        return guidCookieStrategy;
    }

    @Required
    public void setGuidCookieStrategy(GUIDCookieStrategy guidCookieStrategy) {
        this.guidCookieStrategy = guidCookieStrategy;
    }

    public CartRestorationStrategy getCartRestorationStrategy() {
        return cartRestorationStrategy;
    }

    @Required
    public void setCartRestorationStrategy(CartRestorationStrategy cartRestorationStrategy) {
        this.cartRestorationStrategy = cartRestorationStrategy;
    }

    public CustomerFacade getCustomerFacade() {
        return customerFacade;
    }

    @Required
    public void setCustomerFacade(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

}
