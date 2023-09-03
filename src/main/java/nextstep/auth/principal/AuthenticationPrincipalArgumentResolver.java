package nextstep.auth.principal;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "Authorization";

    private final UserPrincipalFactory userPrincipalFactory;

    public AuthenticationPrincipalArgumentResolver(UserPrincipalFactory userPrincipalFactory) {
        this.userPrincipalFactory = userPrincipalFactory;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthenticationPrincipal authenticationPrincipal = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class));
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(webRequest.getHeader(AUTHORIZATION));

        return userPrincipalFactory.create(authorizationHeader, authenticationPrincipal);
    }

}
