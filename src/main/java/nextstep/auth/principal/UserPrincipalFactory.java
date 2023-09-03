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

    public UserPrincipal create(AuthorizationHeader authorizationHeader, boolean isLoginRequired) {
        if (authorizationHeader.isNull() || authorizationHeader.isNotBearerToken()) {
            checkIsLoginRequired(isLoginRequired);

            return new UnknownUserPrincipal();
        }

        String token = authorizationHeader.getToken();
        return createLoggedInUser(token);
    }

    private void checkIsLoginRequired(boolean isLoginRequired) {
        if (isLoginRequired) {
            throw new AuthenticationException();
        }
    }

    private LoggedInUserPrincipal createLoggedInUser(String token) {
        String username = tokenProvider.getPrincipal(token);
        String role = tokenProvider.getRoles(token);

        return new LoggedInUserPrincipal(username, role);
    }
}
