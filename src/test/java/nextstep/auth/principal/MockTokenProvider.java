package nextstep.auth.principal;

import nextstep.auth.token.TokenProvider;

public class MockTokenProvider implements TokenProvider {
    @Override
    public String createToken(String principal, String role) {
        return null;
    }

    @Override
    public String getPrincipal(String token) {
        return null;
    }

    @Override
    public String getRoles(String token) {
        return null;
    }
}
