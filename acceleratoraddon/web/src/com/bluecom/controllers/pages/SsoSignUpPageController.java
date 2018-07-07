package com.bluecom.controllers.pages;

import com.bluecom.controllers.SsoaddonControllerConstants;
import com.bluecom.security.SsoAuthenticationSuccessHandler;
import com.bluecom.social.connect.utils.SsoSignupUtils;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractRegisterPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * Created by tuan.vu on 2/10/17.
 */
@Controller
@Scope("tenant")
public class SsoSignUpPageController extends AbstractRegisterPageController {

    private static final Logger LOGGER = Logger.getLogger(SsoSignUpPageController.class);

    private static final String PROVIDER_SUFFIX = ".auto.signup";

    private ProviderSignInUtils providerSignInUtils;
    private HttpSessionRequestCache httpSessionRequestCache;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "ssoAuthenticationSuccessHandler")
    private SsoAuthenticationSuccessHandler ssoAuthenticationSuccessHandler;

    @Inject
    public SsoSignUpPageController(final ConnectionFactoryLocator connectionFactoryLocator,
                                   final UsersConnectionRepository connectionRepository) {
        this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }

    @RequestMapping(value="/signup", method=RequestMethod.GET)
    public String doSignup(final Model model, final HttpServletRequest request, WebRequest webRequest,
                           final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
    {
        return getDefaultSignUpPage(model, request, webRequest, response, redirectModel);
    }

    // Helper method

    protected String getDefaultSignUpPage(final Model model, final HttpServletRequest request, WebRequest webRequest,
                                          final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
    {
        storeCmsPageInModel(model, getCmsPage());
        setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
        final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#",
                getMessageSource().getMessage("header.link.login", null, getI18nService().getCurrentLocale()), null);
        model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));

        final Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);

        Boolean autoSignUp = configurationService.getConfiguration().getBoolean(connection.getKey().getProviderId() + PROVIDER_SUFFIX, Boolean.FALSE);
        if (autoSignUp) {

            try {
                final RegisterData registerData = SsoSignupUtils.gernerateRegisterData(connection);
                this.getCustomerFacade().register(registerData);
                this.getSsoAuthenticationSuccessHandler().onAuthenticationSuccess(registerData.getLogin().toLowerCase(), request, response);
                this.providerSignInUtils.doPostSignUp(registerData.getLogin().toLowerCase(), webRequest);
                GlobalMessages.addFlashMessage(redirectModel, "accConfMsgs", "registration.confirmation.message.title");

                return "redirect:" + this.getSuccessRedirect(request, response);
            } catch (DuplicateUidException ex) {
                LOGGER.warn("registration failed: " + ex);
                model.addAttribute(new RegisterForm());
                model.addAttribute(new LoginForm());
                model.addAttribute(new GuestForm());
                GlobalMessages.addErrorMessage(model, "form.global.error");
                return this.handleRegistrationError(model);
            }
        }

        final LoginForm loginForm = new LoginForm();
        model.addAttribute(loginForm);
        model.addAttribute(new RegisterForm());

        if (connection != null) {
            model.addAttribute(buildRegisterForm(connection.fetchUserProfile()));
        } else {
            model.addAttribute(new RegisterForm());
        }

        return getView();
    }


    @Override
    protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
    {
        if (httpSessionRequestCache.getRequest(request, response) != null)
        {
            return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
        }
        return "/";
    }

    // Getter and setter

    @Override
    protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
    {
        return getContentPageForLabelOrId("login");
    }

    @Override
    protected String getView()
    {
        return SsoaddonControllerConstants.Views.Pages.Account.SsoAccountSignupPage;
    }

    private RegisterForm buildRegisterForm(final UserProfile userProfile) {
        RegisterForm form = new RegisterForm();
        form.setFirstName(userProfile.getFirstName());
        form.setLastName(userProfile.getLastName());
        form.setEmail(userProfile.getEmail());
        return form;
    }

    @Resource(name = "httpSessionRequestCache")
    public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
    {
        this.httpSessionRequestCache = accHttpSessionRequestCache;
    }

    public SsoAuthenticationSuccessHandler getSsoAuthenticationSuccessHandler() {
        return ssoAuthenticationSuccessHandler;
    }

    public void setSsoAuthenticationSuccessHandler(SsoAuthenticationSuccessHandler ssoAuthenticationSuccessHandler) {
        this.ssoAuthenticationSuccessHandler = ssoAuthenticationSuccessHandler;
    }
}
