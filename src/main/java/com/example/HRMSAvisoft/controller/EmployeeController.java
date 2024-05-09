package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.dto.UpdateEmployeeDetailsDTO;
import com.example.HRMSAvisoft.dto.UpdatePersonalDetailsDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.EmployeeService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){

        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @PostMapping("/{employeeId}/uploadImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable("employeeId") Long employeeId, @RequestParam("file") MultipartFile file) throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException, RuntimeException {
        employeeService.uploadProfileImage(employeeId, file);
        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/searchEmployee")
    public ResponseEntity<List<Employee>> searchEmployeesByName(@RequestParam("name") String name)throws IllegalArgumentException{
        List<Employee> searchedEmployees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(searchedEmployees);
    }


    @GetMapping("/getAllEmployees")
    public ResponseEntity<Map<String,Object>>getAllEmployees() {

        Map<String, Object> responseData = new HashMap<>();
        List<Employee> listOfEmployees=employeeService.getAllEmployees();
        if(listOfEmployees!=null){
            responseData.put("Employees",listOfEmployees);
            responseData.put("message","Employees Retrieved Successfully");
            responseData.put("Success",true);
        }else {
            responseData.put("Employees", null);
            responseData.put("message", "Employee List is Empty");
            responseData.put("Success",true);
        }
        return ResponseEntity.ok().body(responseData);
    }
    @GetMapping("{employeeId}")
    public ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable Long employeeId)throws NullPointerException,EmployeeService.EmployeeNotFoundException, DataAccessException
    {
        Employee employee= employeeService.getEmployeeById(employeeId);
        Map<String, Object> responseData = new HashMap<>();
        return ResponseEntity.ok().body(Map.of("Employee", employee, "message", "Employee retrieved Successfully", "Status", true));

    }
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @PutMapping("/updatePersonalDetails/{employeeId}")
    public ResponseEntity<Map<String ,Object>> updatePersonalDetails(@PathVariable Long employeeId, @RequestBody UpdatePersonalDetailsDTO updatePersonalDetails)throws NullPointerException,EmployeeService.EmployeeNotFoundException
        {

        Employee existingEmployee = employeeService.getEmployeeById(employeeId);
        existingEmployee.setFirstName(updatePersonalDetails.getFirstName());
        existingEmployee.setLastName(updatePersonalDetails.getLastName());
        existingEmployee.setGender(updatePersonalDetails.getGender());
        existingEmployee.setContact(updatePersonalDetails.getContact());
        existingEmployee.setDateOfBirth(updatePersonalDetails.getDateOfBirth());
        Employee savedEmployee = employeeService.updateEmployee(existingEmployee);

        return ResponseEntity.ok().body(Map.of("UpdatedEmployee",savedEmployee , "message", "Personal Details Updated", "Status", true));
    }
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @PutMapping("/updateEmployeeDetails/{employeeId}")
    public ResponseEntity<Map<String,Object>>updateEmployeeDetails(@PathVariable Long employeeId, @RequestBody UpdateEmployeeDetailsDTO updateEmployeeDetailsDTO)throws NullPointerException,EmployeeService.EmployeeNotFoundException
    {
        Employee existingEmployee = employeeService.getEmployeeById(employeeId);
        if(updateEmployeeDetailsDTO.getFirstName()!=null) existingEmployee.setFirstName(updateEmployeeDetailsDTO.getFirstName());
        if(updateEmployeeDetailsDTO.getLastName()!=null) existingEmployee.setLastName(updateEmployeeDetailsDTO.getLastName());
        if(updateEmployeeDetailsDTO.getContact()!=null)existingEmployee.setContact(updateEmployeeDetailsDTO.getContact());
        if(updateEmployeeDetailsDTO.getGender()!=null)existingEmployee.setGender(updateEmployeeDetailsDTO.getGender());
        if(updateEmployeeDetailsDTO.getDateOfBirth()!=null)existingEmployee.setDateOfBirth(updateEmployeeDetailsDTO.getDateOfBirth());
        if(updateEmployeeDetailsDTO.getJoinDate()!=null)existingEmployee.setJoinDate(updateEmployeeDetailsDTO.getJoinDate());
        if(updateEmployeeDetailsDTO.getPosition()!=null)existingEmployee.setPosition(updateEmployeeDetailsDTO.getPosition());
        if(updateEmployeeDetailsDTO.getSalary()!=0)existingEmployee.setSalary(  BigDecimal.valueOf(updateEmployeeDetailsDTO.getSalary()));
        Employee savedEmployee = employeeService.updateEmployee(existingEmployee);
        return ResponseEntity.ok().body(Map.of("UpdatedEmployee",savedEmployee , "message", "Personal Details Updated", "Status", true));

    }


    @ExceptionHandler({
            EmployeeService.EmployeeNotFoundException.class,
            IOException.class,
            RuntimeException.class,
            IllegalArgumentException.class

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
        else if(exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof RuntimeException) {
            message = "Invalid image file";
            status = HttpStatus.BAD_REQUEST;
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
