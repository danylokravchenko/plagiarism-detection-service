package kravchenko.danylo.plagiarism.controller;

import kravchenko.danylo.plagiarism.domain.dto.HistoryDto;
import kravchenko.danylo.plagiarism.domain.dto.UserDto;
import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import kravchenko.danylo.plagiarism.domain.exceptions.BadRequestException;
import kravchenko.danylo.plagiarism.domain.exceptions.NotFoundException;
import kravchenko.danylo.plagiarism.domain.helpers.RequestHelper;
import kravchenko.danylo.plagiarism.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final RequestHelper requestHelper;

    @SneakyThrows
    @GetMapping("/{id}")
    public UserDto user(@PathVariable("id") final Integer id, final HttpServletRequest request) {
        return checkAccess(id, "VIEW_USERS", request).toDto();
    }

    @PreAuthorize("hasAuthority('VIEW_HISTORY')")
    @SneakyThrows
    @GetMapping("/{id}/history")
    public List<HistoryDto> history(
            @PathVariable("id") final Integer id,
            @RequestParam(name = "page", defaultValue = "0") final Integer page,
            @RequestParam(name = "limit", defaultValue = "10") final Integer limit,
            final HttpServletRequest request
    ) {
        log.info("AAAA");
        UserEntity user = checkAccess(id, "VIEW_USERS", request);

        return userService.getUserHistory(user.getId(), page, limit);
    }

    @SneakyThrows
    /*
     * Check access to the section
     * if the user has needed permission - skip the validation
     */
    private UserEntity checkAccess(final Integer id, final String permission, final HttpServletRequest request) {

        UserEntity currentUser = requestHelper.getCurrentUser(request);

        UserEntity user = userService.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("No user found"));

        if (currentUser.hasPermission(permission)) {
            return user;
        }

        if (!currentUser.getId().equals(user.getId())) {
            throw new BadRequestException("Wrong user");
        }

        return user;

    }

}
