package com.jt.sms.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleException {

    // Handling Exception in Spring Boot before Spring Boot 3.0

    // @ExceptionHandler(NoSuchElementException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public Map<String, String> handleNoSuchElementException(NoSuchElementException e) {
    //     var map = new HashMap<String, String>();
    //     map.put("title", "Not found");
    //     map.put("detail", e.getMessage());
    //     map.put("timestamp",LocalDateTime.now().toString());
    //     return map;
    // }



    // @ExceptionHandler(StudentNotFoundException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public Map<String, String> handleNoSuchElementException(StudentNotFoundException e) {
    //     var map = new HashMap<String, String>();
    //     map.put("title", "Not found");
    //     map.put("detail", e.getMessage());
    //     map.put("timestamp",LocalDateTime.now().toString());
    //     return map;
    // }


    // NEW WAY OF HANDLING EXCEPTION AFTER SPRING BOOT 3.0

    /*
     
        ProblemDetail Class was introduced in Spring Boot 3.0


     */

    @ExceptionHandler(StudentNotFoundException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST) --> Not required if we use ProblemDetail
    public ProblemDetail handleNoSuchElementException(StudentNotFoundException e) {
        // ProblemDetail problemDetail = ProblemDetail.forStatus(400); --> NEVER HARDCODE REQUEST CODE
        // ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST); // Recommended way
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }


    /* 
    
        THIS METHOD HANDLES THE EXCEPTIONS WHERE USER TRIES TO ACCESS GETMAPPING MAPPED ENDPOINTS WITH POST OR ANY OTHER REQUESTS AS A RESULT OF WHICH THE REQUEST IS NOT HANDLED AT ALL

        This method is a custom exception handler that specifically deals with cases where a user sends an HTTP request using the wrong method (like sending a POST request to a @GetMapping endpoint).

        This method is used to handle exceptions when the HTTP request method is not supported by the endpoint. For example, if an endpoint is mapped with @GetMapping, but the client sends a POST or DELETE request to it, Spring throws an HttpRequestMethodNotSupportedException.
    
    */ 


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        var details = new StringJoiner(",");

        // Multiple constraints/validations may have failed. getAllErrors() method returns all those errors

        e.getAllErrors().forEach(error ->{
            var errorMessage = error.getDefaultMessage();
            var fieldName = ((FieldError) error).getField();
            details.add(fieldName + " : "+errorMessage); 
            
            //Used StringJoiner to generate a single String containing all the errors in this format (fieldName  -> errorMessage)
        }); 

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, details.toString());

        problemDetail.setTitle("Invalid data");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;


    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
