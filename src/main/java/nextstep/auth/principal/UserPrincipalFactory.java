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
            checkIsLoginRequired(authenticationPrincipal);

            return new UnknownUserPrincipal();
        }

        String token = authorizationHeader.getToken();
        return createLoggedInUser(token);
    }

    private void checkIsLoginRequired(AuthenticationPrincipal authenticationPrincipal) {
        if (authenticationPrincipal.required()) {
            throw new AuthenticationException();
        }
    }

    private LoggedInUserPrincipal createLoggedInUser(String token) {
        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return new LoggedInUserPrincipal(username, role);
    }
}
