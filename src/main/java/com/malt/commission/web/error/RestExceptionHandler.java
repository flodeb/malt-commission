package com.malt.commission.web.error;

import com.malt.commission.web.error.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleRestApiException(ApiException ex) {
        log.debug("Rest API error", ex);

        return constructExceptionResponse(BAD_REQUEST, ex.getCode().getCode(), ex.getMessage(), null);
    }

    @ExceptionHandler({ServletRequestBindingException.class, IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity handleRequestBindingException(Exception ex) {
        log.debug("Request binding error", ex);

        return constructExceptionResponse(BAD_REQUEST, ErrorCode.BAD_REQUEST.getCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("Method validation error", ex);

        List<ApiErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(new ApiErrorResponse.FieldError(error.getField(), error.getDefaultMessage()));
        }

        return constructExceptionResponse(BAD_REQUEST, ErrorCode.BAD_REQUEST.getCode(), "Validation error", fieldErrors);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleThrowable(Throwable ex) {
        log.error("Uncaught error", ex);

        return constructExceptionResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
    }

    private ResponseEntity constructExceptionResponse(
            HttpStatus httpStatus,
            Integer code,
            String message,
            List<ApiErrorResponse.FieldError> fieldErrors) {

        return new ResponseEntity<>(new ApiErrorResponse(code, message, fieldErrors), httpStatus);
    }
}
