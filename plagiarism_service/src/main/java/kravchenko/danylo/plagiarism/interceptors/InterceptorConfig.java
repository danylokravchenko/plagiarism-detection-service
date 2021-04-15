package kravchenko.danylo.plagiarism.interceptors;

import kravchenko.danylo.plagiarism.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new UserInterceptor(userService));
    }

}