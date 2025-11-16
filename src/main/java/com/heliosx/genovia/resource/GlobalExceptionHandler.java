package com.heliosx.genovia.resource;

import com.heliosx.genovia.dto.ErrorResponse;
import com.heliosx.genovia.exception.BadDataException;
import com.heliosx.genovia.exception.MissingAnswerException;
import com.heliosx.genovia.exception.NoAnswersException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoAnswersException.class)
    public ResponseEntity<ErrorResponse> handleNoAnswers(NoAnswersException ex) {
        ErrorResponse body = new ErrorResponse("NO_ANSWERS", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadDataException.class)
    public ResponseEntity<ErrorResponse> handleBadData(BadDataException ex) {
        ErrorResponse body = new ErrorResponse("BAD_DATA", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingAnswerException.class)
    public ResponseEntity<ErrorResponse> handleMissingAnswer(MissingAnswerException ex) {
        ErrorResponse body = new ErrorResponse("MISSING_ANSWER", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception in controller", ex);

        ErrorResponse body = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}