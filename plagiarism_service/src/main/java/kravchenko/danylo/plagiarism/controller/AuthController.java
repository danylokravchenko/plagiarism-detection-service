package kravchenko.danylo.plagiarism.controller;

import kravchenko.danylo.plagiarism.config.TokenProvider;
import kravchenko.danylo.plagiarism.domain.dto.UserDto;
import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import kravchenko.danylo.plagiarism.domain.exceptions.BadRequestException;
import kravchenko.danylo.plagiarism.domain.exceptions.NotFoundException;
import kravchenko.danylo.plagiarism.domain.helpers.RequestHelper;
import kravchenko.danylo.plagiarism.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final RequestHelper requestHelper;

    private final UserService userService;

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";


    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@Valid @RequestBody final UserDto userLoginData, HttpServletResponse response) {
        log.info(userLoginData.getLogin(), userLoginData.getPassword());

        UserEntity user = userService.findByLogin(userLoginData.getLogin())
                .orElseThrow(() -> new NotFoundException("No user with login: " + userLoginData.getLogin()));

        if (passwordEncoder.matches(userLoginData.getPassword(), user.getPassword())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(),
                            user.getPassword()
                    )
            );

            log.info("User #" + user.getId() + " signed in");

            String jwt = tokenProvider.generateToken(authentication, false);
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);

            return ResponseEntity.ok(user);

        } else {
            throw new NotFoundException("Invalid credentials were provided");
        }
    }

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@Valid @RequestBody final UserDto user) {
        log.info(user.getLogin(), user.getPassword());

        if (userService.findByLogin(user.getLogin()).isPresent()) {
            throw new BadRequestException("User with this login already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity newUser = userService.addNewUser(user);

        log.info("User #" + newUser.getId() + " signed up");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(),
                        user.getPassword()
                )
        );

        String jwt = tokenProvider.generateToken(authentication, false);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_STRING, TOKEN_PREFIX + jwt);

        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).build();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        UserEntity currentUser = requestHelper.getCurrentUser(request);
        log.info("User #" + currentUser.getId() + " logged out");

        request.getSession().invalidate();
    }
}