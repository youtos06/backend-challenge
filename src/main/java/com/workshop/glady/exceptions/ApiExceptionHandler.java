package com.workshop.glady.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZonedDateTime;
import java.util.Objects;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BusinessApiException.class})
    public ResponseEntity<Object> handleApiRequestException(BusinessApiException ex){
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                ex.getMessage(),
                100,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse,ex.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiExceptionResponse> handleMethodArgumentTypeMismatchException (MethodArgumentTypeMismatchException ex){
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                ex.getMessage(),
                101,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError(),"Invalid parameter")
                .getDefaultMessage();
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                message,
                102,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
