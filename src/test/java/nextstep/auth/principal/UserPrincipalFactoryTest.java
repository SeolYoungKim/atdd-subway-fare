package nextstep.auth.principal;

import static org.junit.jupiter.api.Assertions.*;

import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("UserPrincipalFactory 단위 테스트")
class UserPrincipalFactoryTest {
    private UserPrincipalFactory userPrincipalFactory;

    @BeforeEach
    void setUp() {
        userPrincipalFactory = new UserPrincipalFactory(new JwtTokenProvider());
    }

}