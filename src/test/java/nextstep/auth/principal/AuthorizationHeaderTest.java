package nextstep.auth.principal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthorizationHeader 단위 테스트")
class AuthorizationHeaderTest {
    private static final String TOKEN_TYPE_BASIC = "basic ";
    private static final String TOKEN_TYPE_BEARER = "bearer ";

    @DisplayName("토큰 유형이 bearer이면 false를 반환")
    @Test
    void isNotBearerTokenFalse() {
        // given
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(TOKEN_TYPE_BEARER + "token");

        // when
        boolean notBearerToken = authorizationHeader.isNotBearerToken();

        // then
        assertThat(notBearerToken).isFalse();
    }

    @DisplayName("토큰 유형이 bearer가 아니면 true를 반환")
    @Test
    void isNotBearerTokenTrue() {
        // given
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(TOKEN_TYPE_BASIC + "token");

        // when
        boolean notBearerToken = authorizationHeader.isNotBearerToken();

        // then
        assertThat(notBearerToken).isTrue();
    }
}