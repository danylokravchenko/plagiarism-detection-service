package kravchenko.danylo.plagiarism.interceptors;

import kravchenko.danylo.plagiarism.service.UserService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor extends HandlerInterceptorAdapter {

    private final UserService userService;

    public UserInterceptor(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        try {
            request.setAttribute("currentUser", userService.findByLogin(request.getUserPrincipal().getName()).get());
            request.setAttribute("isLoggedIn", true);
        } catch (Exception ignore) {
            request.setAttribute("isLoggedIn", false);
        }

        return true;

    }
}