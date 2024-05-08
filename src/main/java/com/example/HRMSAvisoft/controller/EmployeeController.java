package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.dto.UpdatePersonalDetailsDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.EmployeeService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<String> uploadProfileImage(@PathVariable("employeeId") Long employeeId, @RequestParam("file") MultipartFile file) throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
        employeeService.uploadProfileImage(employeeId, file);
        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
        return ResponseEntity.ok().body(message);
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
    public ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable Long employeeId)throws NullPointerException,EmployeeService.EmployeeNotFoundException,DataAccessException
    {
       Employee employee= employeeService.getEmployeeById(employeeId);
        Map<String, Object> responseData = new HashMap<>();
        return ResponseEntity.ok().body(Map.of("Employee", employee, "message", "Employee retrieved Successfully", "Status", true));

    }
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @PutMapping("/updatePersonalDetails/{employeeId}")
    public ResponseEntity<Map<String ,Object>> updatePersonalDetails(@PathVariable Long employeeId, @RequestBody UpdatePersonalDetailsDTO updatePersonalDetails)throws NullPointerException,EmployeeService.EmployeeNotFoundException{

            Employee existingEmployee = employeeService.getEmployeeById(employeeId);
            existingEmployee.setFirstName(updatePersonalDetails.getFirstName());
            existingEmployee.setLastName(updatePersonalDetails.getLastName());
            existingEmployee.setGender(updatePersonalDetails.getGender());
            existingEmployee.setContact(updatePersonalDetails.getContact());
            existingEmployee.setDateOfBirth(updatePersonalDetails.getDateOfBirth());
            Employee savedEmployee = employeeService.updateEmployee(existingEmployee);

            return ResponseEntity.ok().body(Map.of("UpdatedEmployee",savedEmployee , "message", "Personal Details Updated", "Status", true));


    }
    @PostMapping("/{employeeId}/addNewAddress")
    public ResponseEntity<Map<String,Employee>> addAddressToEmployee(@PathVariable Long employeeId, @RequestBody Address address)throws EmployeeService.EmployeeNotFoundException {
        Employee updatedEmployee = employeeService.addAddressToEmployee(employeeId, address);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee));
    }
    @DeleteMapping("/{employeeId}/removeAddress/{addressId}")
    public ResponseEntity<Map<String,Employee>> removeAddressFromEmployee(@PathVariable Long employeeId, @PathVariable Long addressId) {
        Employee updatedEmployee = employeeService.removeAddressFromEmployee(employeeId, addressId);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee));
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
        else if (exception instanceof DataAccessException)
        {
            message = exception.getMessage();
            status =  HttpStatus.INTERNAL_SERVER_ERROR;
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
