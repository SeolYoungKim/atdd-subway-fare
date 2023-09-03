package nextstep.auth.principal;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class UserPrincipalFactory {
    private final JwtTokenProvider jwtTokenProvider;

    public UserPrincipalFactory(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserPrincipal create(AuthorizationHeader authorizationHeader, AuthenticationPrincipal authenticationPrincipal) {
        if (authorizationHeader.isNull() || authorizationHeader.isNotBearerToken()) {
            checkIsLoginRequired(authenticationPrincipal);  // AuthenticationPrincipal 애노테이션의 required 옵션을 확인한다.

            return new UnknownUserPrincipal();  // 언노운 유저를 만든다.
        }

        String token = authorizationHeader.getToken();  // 토큰을 얻는다.
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
