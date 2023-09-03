package nextstep.auth.principal;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
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
        return createUserPrincipal(webRequest, authenticationPrincipal);
    }

    private UserPrincipal createUserPrincipal(NativeWebRequest webRequest, AuthenticationPrincipal authenticationPrincipal) {
        AuthorizationHeader authorizationHeader = AuthorizationHeader.of(webRequest);
        return userPrincipalFactory.create(authorizationHeader, authenticationPrincipal);
    }
}
