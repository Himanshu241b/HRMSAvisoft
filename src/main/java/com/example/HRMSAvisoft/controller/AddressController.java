package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.AddressService;
import com.example.HRMSAvisoft.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private AddressService addressService;
    public AddressController(AddressService addressService)
    {
        this.addressService=addressService;
    }
    @PostMapping("/{employeeId}/addNewAddress")
    public ResponseEntity<Map<String,Object>> addAddressToEmployee(@PathVariable Long employeeId, @RequestBody AddressDTO address)throws EmployeeService.EmployeeNotFoundException , AddressService.AddressAlreadyPresentException {
        Employee updatedEmployee = addressService.addAddressToEmployee(employeeId, address);
        return ResponseEntity.ok().body(Map.of("UpdatedEmployee",updatedEmployee ,"message", "New Address Added", "Status", true));
    }
    @DeleteMapping("/{employeeId}/removeAddress/{addressId}")
    public ResponseEntity<Map<String,Object>> removeAddressFromEmployee(@PathVariable Long employeeId, @PathVariable Long addressId) throws EmployeeService.EmployeeNotFoundException{
        Employee updatedEmployee = addressService.removeAddressFromEmployee(employeeId, addressId);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee,"message", "Address Removed from Employee", "Status", true));
    }

    @ExceptionHandler({
            EmployeeService.EmployeeNotFoundException.class,
            NullPointerException.class

    })
    public ResponseEntity<Map<String,Object>> handleErrors(Exception exception){
        Map<String ,Object> responseData = new HashMap<>();
        if(exception instanceof EmployeeService.EmployeeNotFoundException) {
            responseData.put("message", exception.getMessage());
            responseData.put("status",HttpStatus.NOT_FOUND);
        } else if(exception instanceof NullPointerException) {
            responseData.put("message", exception.getMessage());
            responseData.put("status",HttpStatus.BAD_REQUEST);
        }
        else{
            responseData.put("message","Something went wrong");
            responseData.put("status",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().body(responseData);
    }
}
