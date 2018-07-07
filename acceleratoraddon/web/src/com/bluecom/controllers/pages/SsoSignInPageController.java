package com.bluecom.controllers.pages;

import com.bluecom.controllers.SsoaddonControllerConstants;
import com.bluecom.security.SsoAuthenticationSuccessHandler;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractRegisterPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tuan.vu on 2/10/17.
 */
@Controller
@Scope("tenant")
public class SsoSignInPageController extends AbstractRegisterPageController {

    private static final Logger LOGGER = Logger.getLogger(SsoSignInPageController.class);

    private HttpSessionRequestCache httpSessionRequestCache;

    @Resource(name = "ssoAuthenticationSuccessHandler")
    private SsoAuthenticationSuccessHandler ssoAuthenticationSuccessHandler;

    private final ProviderSignInUtils providerSignInUtils;

    @Inject
    public SsoSignInPageController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository) {
        this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }

    @RequestMapping(value="/signin", method= RequestMethod.POST)
    public String doSignIn(final RegisterForm form, final BindingResult bindingResult, final Model model,
                             final HttpServletRequest request, WebRequest webRequest,
                             final HttpServletResponse response, final RedirectAttributes redirectModel)
            throws CMSItemNotFoundException {

        getRegistrationValidator().validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(form);
            model.addAttribute(new LoginForm());
            model.addAttribute(new GuestForm());
            GlobalMessages.addErrorMessage(model, "form.global.error");
            return this.handleRegistrationError(model);
        } else {
            RegisterData data = new RegisterData();
            data.setFirstName(form.getFirstName());
            data.setLastName(form.getLastName());
            data.setLogin(form.getEmail());
            data.setPassword(form.getPwd());
            data.setTitleCode(form.getTitleCode());

            try {
                this.getCustomerFacade().register(data);
                this.getSsoAuthenticationSuccessHandler().onAuthenticationSuccess(form.getEmail().toLowerCase(), request, response);
                this.providerSignInUtils.doPostSignUp(form.getEmail().toLowerCase(), webRequest);
                GlobalMessages.addFlashMessage(redirectModel, "accConfMsgs", "registration.confirmation.message.title");
            } catch (DuplicateUidException ex) {
                LOGGER.warn("registration failed: " + ex);
                model.addAttribute(form);
                model.addAttribute(new LoginForm());
                model.addAttribute(new GuestForm());
                bindingResult.rejectValue("email", "registration.error.account.exists.title");
                GlobalMessages.addErrorMessage(model, "form.global.error");
                return this.handleRegistrationError(model);
            }

            return "redirect:" + this.getSuccessRedirect(request, response);
        }
    }

    // Helper method

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
