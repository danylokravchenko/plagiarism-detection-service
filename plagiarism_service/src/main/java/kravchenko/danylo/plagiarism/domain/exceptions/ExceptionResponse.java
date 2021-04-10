package kravchenko.danylo.plagiarism.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Generic container of errors that will be send as the error response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String details;

}