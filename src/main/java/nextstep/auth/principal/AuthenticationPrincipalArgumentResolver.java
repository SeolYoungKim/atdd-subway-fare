package nextstep.auth.principal;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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

    /**
     * 해당 메서드의 역할
     * 1. NativeWebRequest, AuthenticationPrincipal 을 이용하여, 토큰이 있는지 확인한다.
     *   - 있을 경우, 토큰을 파싱해서 유저 정보를 얻어낸다.
     *   - 없을 경우, 그에 따른 로직을 수행한다.
     */
    private UserPrincipal createUserPrincipal(NativeWebRequest webRequest, AuthenticationPrincipal authenticationPrincipal) {
        String authorization = webRequest.getHeader("Authorization");  // 헤더를 얻어낸다
        if (authorization == null || !"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {  // 토큰이 있는지 확인한다.
            checkIsLoginRequired(authenticationPrincipal);  // AuthenticationPrincipal 애노테이션의 required 옵션을 확인한다.

            return new UnknownUserPrincipal();  // 언노운 유저를 만든다.
        }

        String token = authorization.split(" ")[1];  // 토큰을 얻는다.
        return createLoggedInUser(token);
    }

    private void checkIsLoginRequired(AuthenticationPrincipal authenticationPrincipal) {
        if (authenticationPrincipal.required()) {
            throw new AuthenticationException();
        }
    }

    private LoggedInUserPrincipal createLoggedInUser(String token) {
        String username = jwtTokenProvider.getPrincipal(token);  // 토큰에서 username을 얻는다.
        String role = jwtTokenProvider.getRoles(token);  // 토큰에서 role을 얻는다.

        return new LoggedInUserPrincipal(username, role);  // 로그인된 유저 객체를 만든다.
    }
}
