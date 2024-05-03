package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class EmployeeService {

    EmployeeRepository employeeRepository;

    EmployeeService(EmployeeRepository employeeRepository){
    this.employeeRepository = employeeRepository;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException{
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        employee.setProfileImage(file.getBytes());
        employeeRepository.save(employee);
    }

    public class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(Long employeeId) {
            super("Employee not found with ID: " + employeeId);
        }
    }
}
