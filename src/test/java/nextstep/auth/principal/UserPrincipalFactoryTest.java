package nextstep.auth.principal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("로그인 유저의 UserPrincipal 생성")
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
}