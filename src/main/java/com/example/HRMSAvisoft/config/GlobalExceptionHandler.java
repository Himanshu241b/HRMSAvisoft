package com.example.HRMSAvisoft.config;

import com.example.HRMSAvisoft.exception.EmergencyContactNotFoundException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    HttpStatus status;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>>handlesValidationErrors(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        Map<String,Object>responseData=new HashMap<>();
        responseData.put("message",errors);
        status= HttpStatus.BAD_REQUEST;
        responseData.put("Success",false);
        return ResponseEntity.status(status).body(responseData);
    }

    @ExceptionHandler({
           EmployeeNotFoundException.class,
            NullPointerException.class,
            AddressService.AddressNotFoundException.class,
            EmergencyContactNotFoundException.class

    })
    public ResponseEntity<Map<String,Object>> handleErrors(Exception exception){
        Map<String ,Object> responseData = new HashMap<>();
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.NOT_FOUND;
        }
       else if (exception instanceof EntityNotFoundException) {
            responseData.put("message",exception.getMessage());
            status = HttpStatus.NOT_FOUND;

        }else if(exception instanceof NullPointerException) {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.BAD_REQUEST;
        }else if(exception instanceof EmergencyContactNotFoundException)
        {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.BAD_REQUEST;
        }
        else{
            responseData.put("message","Something went wrong");
            status=HttpStatus.INTERNAL_SERVER_ERROR;
        }
        responseData.put("Success",false);
        return ResponseEntity.status(status).body(responseData);
    }



}
