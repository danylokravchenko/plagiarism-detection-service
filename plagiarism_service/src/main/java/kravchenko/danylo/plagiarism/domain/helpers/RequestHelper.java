package kravchenko.danylo.plagiarism.domain.helpers;

import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Some common methods to work with request
 */
@Component
public class RequestHelper {

    /*
     * Get current user from the request
     */
    public UserEntity getCurrentUser(HttpServletRequest request) {
        return (UserEntity) request.getAttribute("currentUser");
    }

    /*
     * Check whether the user is logged in
     */
    public boolean isUserLoggedId(HttpServletRequest request) {
        return (boolean) request.getAttribute("isLoggedIn");
    }
}