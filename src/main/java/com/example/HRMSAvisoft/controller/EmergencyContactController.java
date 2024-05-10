package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/emergencyContact")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> getEmergencyContactsOfEmployee(@PathVariable("employeeId") Long employeeId) throws EmployeeService.EmployeeNotFoundException {
        List<EmergencyContact> emergencyContactsOfEmployee = emergencyContactService.getEmergencyContactsOfEmployee(employeeId);
        if (emergencyContactsOfEmployee.isEmpty()){
            return ResponseEntity.ok(Map.of("success", true, "message", "No emergency contacts."));
        }
        else {
            return ResponseEntity.ok(Map.of("success", true, "message", "emergency contacts of employee " + employeeId + " fetched", "emergencyContacts", emergencyContactsOfEmployee));
        }
    }

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> addEmergencyContact(@RequestBody CreateEmergencyContactDTO createEmergencyContactDTO, @PathVariable("employeeId") Long employeeId)throws EmployeeService.EmployeeNotFoundException {
        EmergencyContact newEmergencyContact = emergencyContactService.addEmergencyContact(createEmergencyContactDTO, employeeId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Emergency contact added", "emergencyContact", newEmergencyContact));
    }

    @PatchMapping("/{emergencyContactId}")
    public ResponseEntity<Map<String, Object>> updateEmergencyContact(@RequestBody CreateEmergencyContactDTO createEmergencyContactDTO, @PathVariable("emergencyContactId") Long emergencyContactId)throws EntityNotFoundException {
        EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(createEmergencyContactDTO, emergencyContactId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Emergency contact updated", "emergencyContact", updatedEmergencyContact));
    }

    @DeleteMapping("/{emergencyContactId}/{employeeId}")
    public ResponseEntity<Map<String, Object>> deleteEmergencyContact(@PathVariable("emergencyContactId") Long emergencyContactId, @PathVariable("employeeId") Long employeeId)throws EntityNotFoundException {
        emergencyContactService.deleteEmergencyContact(emergencyContactId, employeeId);
        return ResponseEntity.ok(Map.of("success", true, "message","Emergency contact deleted"));
    }


    @ExceptionHandler({
            EmployeeService.EmployeeNotFoundException.class,
            EntityNotFoundException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeService.EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
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
