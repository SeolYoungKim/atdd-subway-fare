package nextstep.auth.principal;

public class AuthorizationHeader {
    private static final String TOKEN_TYPE_BEARER = "bearer";

    private final String authorization;

    public AuthorizationHeader(String authorizationHeader) {
        this.authorization = authorizationHeader;
    }

    public boolean isNull() {
        return authorization == null;
    }

    public boolean isNotBearerToken() {
        return !TOKEN_TYPE_BEARER.equalsIgnoreCase(authorization.split(" ")[0]);
    }

    public String getToken() {
        return authorization.split(" ")[1];
    }
}
