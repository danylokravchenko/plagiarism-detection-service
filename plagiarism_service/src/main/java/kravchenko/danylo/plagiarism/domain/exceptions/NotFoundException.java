package kravchenko.danylo.plagiarism.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

/**
 * This exception is thrown in case of non existing entity.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException implements Supplier<NotFoundException> {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public NotFoundException get() {
        return this;
    }
}