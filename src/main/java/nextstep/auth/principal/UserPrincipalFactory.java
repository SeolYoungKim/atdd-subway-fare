package nextstep.auth.principal;

import nextstep.auth.AuthenticationException;
import nextstep.auth.token.TokenProvider;
import org.springframework.stereotype.Component;

@Component
public class UserPrincipalFactory {
    private final TokenProvider tokenProvider;

    public UserPrincipalFactory(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
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
        String username = tokenProvider.getPrincipal(token);
        String role = tokenProvider.getRoles(token);

        return new LoggedInUserPrincipal(username, role);
    }
}
