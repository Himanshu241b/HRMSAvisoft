package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.service.EmployeeService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @PostMapping("/{employeeId}/uploadImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable("employeeId") Long employeeId, @RequestParam("file") MultipartFile file) throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
        employeeService.uploadProfileImage(employeeId, file);
        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
        return ResponseEntity.ok().body(message);
    }

    @ExceptionHandler({
            EmployeeService.EmployeeNotFoundException.class,
            IOException.class,

    })
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeService.EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof IOException) {
            message = "Failed to update Profile Image";
            status = HttpStatus.BAD_REQUEST;
        }else if(exception instanceof NullPointerException) {
            message = exception.getMessage();
            status =  HttpStatus.BAD_REQUEST;
        }
        else{
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
