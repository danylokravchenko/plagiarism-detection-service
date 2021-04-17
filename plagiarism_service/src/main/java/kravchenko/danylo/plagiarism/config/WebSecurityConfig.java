package kravchenko.danylo.plagiarism.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * Configure Spring Security using already created authentication helpers
 * and create some useful beans to use with it
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final JwtAccessDeniedHandler accessDeniedHandler;

    /*
     * What authentication manager to use
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
     * What user details service to use
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    /*
     * What authentication token filter to use
     */
    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    /*
     * What password encoder to use
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] patterns = new String[]{"/auth/**", "/plagiarism" , "/"};

        // disable authentication for cors
        http.cors()
                // disable csrf policy
                .and()
                .csrf().disable()

                // disable login form
                .formLogin().disable()

                .authorizeRequests()
                // exclude some routes from security
                .antMatchers(patterns).permitAll()
                // other have to be authenticated
                .anyRequest().authenticated()

                // custom exception handlers
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler)

                // make session not persistent
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Collections.singletonList("*"));
//        config.setAllowedMethods(Collections.singletonList("*"));
//        config.setAllowedHeaders(Collections.singletonList("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }


}