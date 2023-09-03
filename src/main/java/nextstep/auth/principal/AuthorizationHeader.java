package nextstep.auth.principal;

import org.springframework.web.context.request.NativeWebRequest;

public class AuthorizationHeader {
    private static final String AUTHORIZATION = "Authorization";

    private final String authorization;

    public static AuthorizationHeader of(NativeWebRequest webRequest) {
        return new AuthorizationHeader(webRequest.getHeader(AUTHORIZATION));
    }

    private AuthorizationHeader(String authorization) {
        this.authorization = authorization;
    }


    public boolean isNull() {
        return authorization == null;
    }

    public boolean isNotBearerToken() {
        return !"bearer".equalsIgnoreCase(authorization.split(" ")[0]);
    }

    public String getToken() {
        return authorization.split(" ")[1];
    }
}
