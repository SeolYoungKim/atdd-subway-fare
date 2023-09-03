package nextstep.auth.principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.auth.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UserPrincipalFactory 단위 테스트")
class UserPrincipalFactoryTest {
    private static final String AUTHORIZATION_HEADER = "bearer token";

    private UserPrincipalFactory userPrincipalFactory;

    @BeforeEach
    void setUp() {
        userPrincipalFactory = new UserPrincipalFactory(new MockTokenProvider());
    }

    @DisplayName("LoggedInUserPrincipal 생성")
    @ParameterizedTest(name = "isLoginRequired={0}")
    @ValueSource(booleans = {true, false})
    void createLoggedInUser(boolean isLoginRequired) {
        // given
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(AUTHORIZATION_HEADER);

        // when
        UserPrincipal userPrincipal = userPrincipalFactory.create(authorizationHeader, isLoginRequired);

        // then
        assertThat(userPrincipal).isInstanceOf(LoggedInUserPrincipal.class);
    }

    @DisplayName("authorization header 값이 없고, isLoginRequired=false 일 경우 UnknownUserPrincipal 생성")
    @Test
    void createUnknownUser() {
        // given
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(null);

        // when
        UserPrincipal userPrincipal = userPrincipalFactory.create(authorizationHeader, false);

        // then
        assertThat(userPrincipal).isInstanceOf(UnknownUserPrincipal.class);
    }

    @DisplayName("authorization header 값이 없고, isLoginRequired=true 일 경우 예외 발생")
    @Test
    void isLoginFalseAndThrowException() {
        // given
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(null);

        // when, then
        assertThatThrownBy(() -> userPrincipalFactory.create(authorizationHeader, true))
                .isInstanceOf(AuthenticationException.class);
    }
}