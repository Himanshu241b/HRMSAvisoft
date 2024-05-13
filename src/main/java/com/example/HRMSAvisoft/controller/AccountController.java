package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.AccountService;
import com.example.HRMSAvisoft.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private AccountService accountService;
    public AccountController(AccountService accountService){
        this.accountService=accountService;
    }
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @PostMapping("/{employeeId}/AddBankAccount")
    public ResponseEntity<Map<String,Object>>addAccount(@RequestBody AddAccountDTO accountDTO, @PathVariable Long employeeId)throws EmployeeService.EmployeeNotFoundException{
        Employee updatedEmployee=accountService.addAccountToEmployee(employeeId,accountDTO);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "Account Added");
        response.put("success", true);
        response.put("updatedUser", updatedEmployee);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    @DeleteMapping("/{employeeId}/removeAccount")
    public ResponseEntity<Map<String,Object>> removeAccountFromEmployee(@PathVariable Long employeeId) {
        Map<String, Object> response = new HashMap<String, Object>();
        boolean removed = accountService.removeAccountFromEmployee(employeeId);
        if (removed) {
            response.put("message", "Account Removed");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Account Field is empty");
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }

    @ExceptionHandler({
            EmployeeService.EmployeeNotFoundException.class,
            NullPointerException.class

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


