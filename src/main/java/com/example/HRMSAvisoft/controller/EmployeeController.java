package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.Utils.MyResponseGenerator;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.EmployeeService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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


    @GetMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    @GetMapping("{employeeId}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable Long employeeId)
    {
       Employee employee= employeeService.getEmployeeById(employeeId);
       if(employee!=null){
           return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Employee Retrieved",employee);
       }else {
           return MyResponseGenerator.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,false,"Something Went Wrong",employee);
       }

    }
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee updatedEmployee) {
        try {
            Employee existingEmployee = employeeService.getEmployeeById(employeeId);
            if (existingEmployee == null) {
                return ResponseEntity.notFound().build();
            }
            updatedEmployee.setEmployeeId(employeeId);
            Employee savedEmployee = employeeService.updateEmployee(updatedEmployee);

            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
