package nextstep.auth.principal;

import nextstep.auth.token.TokenProvider;

public class MockTokenProvider implements TokenProvider {
    @Override
    public String createToken(String principal, String role) {
        return "token";
    }

    @Override
    public String getPrincipal(String token) {
        return "principal";
    }

    @Override
    public String getRoles(String token) {
        return "role";
    }
}
