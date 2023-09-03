package nextstep.auth.token;

public interface TokenProvider {
    String createToken(String principal, String role);

    String getPrincipal(String token);

    String getRoles(String token);
}
