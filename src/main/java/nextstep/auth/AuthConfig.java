package nextstep.auth;

import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.principal.UserPrincipalFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserPrincipalFactory userPrincipalFactory;

    public AuthConfig(UserPrincipalFactory userPrincipalFactory) {
        this.userPrincipalFactory = userPrincipalFactory;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(userPrincipalFactory));
    }
}
