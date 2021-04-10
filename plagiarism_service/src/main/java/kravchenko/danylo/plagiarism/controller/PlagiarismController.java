package kravchenko.danylo.plagiarism.controller;

import kravchenko.danylo.plagiarism.domain.dto.PlagiarismRequestItem;
import kravchenko.danylo.plagiarism.domain.dto.PlagiarismReview;
import kravchenko.danylo.plagiarism.service.PlagiarismService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PlagiarismController {

    private final PlagiarismService plagiarismService;

    @ResponseBody
    @PostMapping("/plagiarism")
    public PlagiarismReview plagiarism(@RequestBody final PlagiarismRequestItem item) {
        return plagiarismService.analyzePlagiarism(item);
    }
}
