<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>


<div class="register__section">
	<c:url value="/signin" var="ssoSigninActionUrl" />
	<user:register actionNameKey="register.submit"
		action="${ssoSigninActionUrl}" />
</div>
