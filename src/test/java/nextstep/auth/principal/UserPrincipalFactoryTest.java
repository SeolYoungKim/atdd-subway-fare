package nextstep.auth.principal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("UserPrincipalFactory 단위 테스트")
class UserPrincipalFactoryTest {
    private UserPrincipalFactory userPrincipalFactory;

    @BeforeEach
    void setUp() {
        userPrincipalFactory = new UserPrincipalFactory(new MockTokenProvider());
    }


}