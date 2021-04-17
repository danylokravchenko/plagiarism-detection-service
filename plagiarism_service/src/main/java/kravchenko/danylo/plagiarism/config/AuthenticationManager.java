package kravchenko.danylo.plagiarism.config;

import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import kravchenko.danylo.plagiarism.domain.exceptions.NotFoundException;
import kravchenko.danylo.plagiarism.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Authenticate the user and collect user's authorities
 */
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException, NotFoundException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        UserEntity user = userService.findByLogin(username).orElseThrow(() -> new NotFoundException("No user for login: " + username));

        // collect user's roles and create authorities what user could access
        List<SimpleGrantedAuthority> authorities = UserService.mapAuthorities(user.getPermissions());

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password, authorities);
        // authenticate this user
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }


}
