package kravchenko.danylo.plagiarism.controller;

import kravchenko.danylo.plagiarism.domain.dto.PlagiarismRequestItem;
import kravchenko.danylo.plagiarism.domain.dto.PlagiarismReview;
import kravchenko.danylo.plagiarism.domain.entities.UserEntity;
import kravchenko.danylo.plagiarism.domain.exceptions.NotFoundException;
import kravchenko.danylo.plagiarism.domain.helpers.RequestHelper;
import kravchenko.danylo.plagiarism.service.PlagiarismService;
import kravchenko.danylo.plagiarism.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PlagiarismController {

    private final PlagiarismService plagiarismService;
    private final UserService userService;
    private final RequestHelper requestHelper;

    @ResponseBody
    @PostMapping("/plagiarism")
    public PlagiarismReview plagiarism(HttpServletRequest request, @RequestBody final PlagiarismRequestItem item) {

        UserEntity currentUser;

        if (requestHelper.isUserLoggedId(request)) {
            currentUser = requestHelper.getCurrentUser(request);
        } else {
            currentUser = userService.getVisitor()
                    .orElseThrow(() -> new NotFoundException("No permission found"));
        }

        final PlagiarismReview review = plagiarismService.analyzePlagiarism(item);

        userService.addUserHistory(item, review, currentUser.getId());

        return review;
    }

}
