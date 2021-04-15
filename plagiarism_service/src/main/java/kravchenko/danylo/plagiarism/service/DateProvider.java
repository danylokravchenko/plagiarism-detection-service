package kravchenko.danylo.plagiarism.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class DateProvider {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

}