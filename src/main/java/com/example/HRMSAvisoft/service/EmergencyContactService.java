package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmergencyContactRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
