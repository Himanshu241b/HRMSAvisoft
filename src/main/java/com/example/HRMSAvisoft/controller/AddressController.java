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
    public ResponseEntity<Map<String,Object>> addAddressToEmployee(@PathVariable Long employeeId, @RequestBody AddressDTO address)throws EmployeeService.EmployeeNotFoundException  {
        Employee updatedEmployee = addressService.addAddressToEmployee(employeeId, address);
        return ResponseEntity.ok().body(Map.of("UpdatedEmployee",updatedEmployee ,"message", "New Address Added", "Status", true));
    }
    @DeleteMapping("/{employeeId}/removeAddress/{addressId}")
    public ResponseEntity<Map<String,Object>> removeAddressFromEmployee(@PathVariable Long employeeId, @PathVariable Long addressId) throws EmployeeService.EmployeeNotFoundException{
        Employee updatedEmployee = addressService.removeAddressFromEmployee(employeeId, addressId);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee,"message", "Address Removed from Employee", "Status", true));
    }
    @PutMapping("/{employeeId}/editAddress/{addressId}")
    public ResponseEntity<Map<String,Object>>editAddress(@PathVariable Long employeeId,@PathVariable Long addressId,@RequestBody AddressDTO addressDTO)throws EmployeeService.EmployeeNotFoundException,AddressService.AddressNotFoundException
    {
        Employee updatedEmployee = addressService.editAddress(employeeId, addressId,addressDTO);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee,"message", "Address edited", "Status", true));

    }

    @ExceptionHandler({
            EmployeeService.EmployeeNotFoundException.class,
            NullPointerException.class,
            AddressService.AddressNotFoundException.class

    })
    public ResponseEntity<Map<String,Object>> handleErrors(Exception exception){
        Map<String ,Object> responseData = new HashMap<>();
        HttpStatus status;
        if(exception instanceof EmployeeService.EmployeeNotFoundException) {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.NOT_FOUND;
            responseData.put("Success",false);
        } else if(exception instanceof NullPointerException) {
            responseData.put("message", exception.getMessage());
            status=HttpStatus.BAD_REQUEST;
            responseData.put("Success",false);
        }
        else{
            responseData.put("message","Something went wrong");
            status=HttpStatus.INTERNAL_SERVER_ERROR;
            responseData.put("Success",false);
        }

        return ResponseEntity.status(status).body(responseData);
    }
}
