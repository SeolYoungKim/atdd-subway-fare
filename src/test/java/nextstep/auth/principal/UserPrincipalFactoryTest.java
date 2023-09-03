package nextstep.auth.principal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserPrincipalFactory 단위 테스트")
class UserPrincipalFactoryTest {
    private UserPrincipalFactory userPrincipalFactory;

    @BeforeEach
    void setUp() {
        userPrincipalFactory = new UserPrincipalFactory(new MockTokenProvider());
    }

    @DisplayName("로그인 유저의 UserPrincipal 생성")
    @Test
    void createLoggedInUser() {
        // given

    }
}