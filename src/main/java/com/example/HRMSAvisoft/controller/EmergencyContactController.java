package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.exception.EmergencyContactNotFoundException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    public ResponseEntity<Map<String, Object>> getEmergencyContactsOfEmployee(@PathVariable("employeeId") Long employeeId) throws EmployeeNotFoundException {
        List<EmergencyContact> emergencyContactsOfEmployee = emergencyContactService.getEmergencyContactsOfEmployee(employeeId);
        if (emergencyContactsOfEmployee.isEmpty()){
            return ResponseEntity.ok(Map.of("success", true, "message", "No emergency contacts."));
        }
        else {
            return ResponseEntity.ok(Map.of("success", true, "message", "emergency contacts of employee " + employeeId + " fetched", "emergencyContacts", emergencyContactsOfEmployee));
        }
    }

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> addEmergencyContact(@RequestBody @Valid CreateEmergencyContactDTO createEmergencyContactDTO, @PathVariable("employeeId") Long employeeId)throws EmployeeNotFoundException {
        EmergencyContact newEmergencyContact = emergencyContactService.addEmergencyContact(createEmergencyContactDTO, employeeId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Emergency contact added", "emergencyContact", newEmergencyContact));
    }

    @PatchMapping("/{emergencyContactId}")
    public ResponseEntity<Map<String, Object>> updateEmergencyContact(@RequestBody @Valid CreateEmergencyContactDTO createEmergencyContactDTO, @PathVariable("emergencyContactId") Long emergencyContactId)throws EmergencyContactNotFoundException {
        EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(createEmergencyContactDTO, emergencyContactId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Emergency contact updated", "emergencyContact", updatedEmergencyContact));
    }

    @DeleteMapping("/{emergencyContactId}/{employeeId}")
    public ResponseEntity<Map<String, Object>> deleteEmergencyContact(@PathVariable("emergencyContactId") Long emergencyContactId, @PathVariable("employeeId") Long employeeId)throws EmergencyContactNotFoundException {
        emergencyContactService.deleteEmergencyContact(emergencyContactId, employeeId);
        return ResponseEntity.ok(Map.of("success", true, "message","Emergency contact deleted"));
    }

}
