package kravchenko.danylo.plagiarism.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

/**
 * This exception is thrown in case bad request parameters provided.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException implements Supplier<BadRequestException> {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public BadRequestException get() {
        return this;
    }
}