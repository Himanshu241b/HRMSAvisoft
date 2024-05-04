package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Transactional
public class EmployeeService {

    EmployeeRepository employeeRepository;

    EmployeeService(EmployeeRepository employeeRepository){
    this.employeeRepository = employeeRepository;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
            if(file == null) {
                throw new NullPointerException("File not found");
            }
            employee.setProfileImage(file.getBytes());
            employeeRepository.save(employee);
    }

    public class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(Long employeeId) {
            super("Employee not found with ID: " + employeeId);
        }
    }
}
