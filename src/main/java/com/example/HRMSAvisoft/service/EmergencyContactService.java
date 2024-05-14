package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmergencyContactRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EmergencyContactService {

    private final EmployeeRepository employeeRepository;

    private final EmergencyContactRepository emergencyContactRepository;

    EmergencyContactService(EmployeeRepository employeeRepository,EmergencyContactRepository emergencyContactRepository){
        this.employeeRepository = employeeRepository;
        this.emergencyContactRepository = emergencyContactRepository;
    }

    public List<EmergencyContact> getEmergencyContactsOfEmployee(Long employeeId) throws EmployeeService.EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if(employee == null){
            throw new EmployeeService.EmployeeNotFoundException(employeeId);
        }
        return employee.getEmergencyContacts();
    }

    public EmergencyContact addEmergencyContact(CreateEmergencyContactDTO createEmergencyContactDTO, Long employeeId) throws EmployeeService.EmployeeNotFoundException, ValidationException {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if(employee == null){
            throw new EmployeeService.EmployeeNotFoundException(employeeId);
        }

        if((createEmergencyContactDTO.getRelationship() == null || createEmergencyContactDTO.getRelationship() == "") && (createEmergencyContactDTO.getContact() == null || createEmergencyContactDTO.getContact() == "")){
            throw new ValidationException("All fields are required");
        }
        if(createEmergencyContactDTO.getContact() == null || createEmergencyContactDTO.getContact() == ""){
            throw new ValidationException("Contact field cannot be empty.");
        }
        if(createEmergencyContactDTO.getRelationship() == null || createEmergencyContactDTO.getRelationship() == ""){
            throw new ValidationException("Relationship field cannot be empty");
        }
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setContact(createEmergencyContactDTO.getContact());
        emergencyContact.setRelationship(createEmergencyContactDTO.getRelationship());
        EmergencyContact newEmergencyContact = emergencyContactRepository.save(emergencyContact);

        employee.getEmergencyContacts().add(newEmergencyContact);
        employeeRepository.save(employee);

        return newEmergencyContact;
    }

    public EmergencyContact updateEmergencyContact(CreateEmergencyContactDTO createEmergencyContactDTO, Long emergencyContactId)throws EntityNotFoundException{
        EmergencyContact emergencyContactToUpdate = emergencyContactRepository.findById(emergencyContactId).orElseThrow(()-> new EntityNotFoundException("No emergency contact found"));
        if(createEmergencyContactDTO.getContact() != null){
            emergencyContactToUpdate.setContact(createEmergencyContactDTO.getContact());
        }
        if(createEmergencyContactDTO.getRelationship() != null){
            emergencyContactToUpdate.setRelationship(createEmergencyContactDTO.getRelationship());
        }
        return emergencyContactRepository.save(emergencyContactToUpdate);
    }

    public void deleteEmergencyContact(Long emergencyContactId, Long employeeId)throws  EntityNotFoundException{

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new EntityNotFoundException("Employee not found."));

        EmergencyContact emergencyContactToDelete = emergencyContactRepository.findById(emergencyContactId).orElseThrow(()-> new EntityNotFoundException("Emergency contact not found"));

        employee.getEmergencyContacts().remove(emergencyContactToDelete);
        emergencyContactRepository.delete(emergencyContactToDelete);

    }

    public static class ValidationException extends RuntimeException{
        public ValidationException(String message){
            super(message);
        }

    }
}
