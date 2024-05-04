package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class EmployeeService {

    private static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/profileImages";

    EmployeeRepository employeeRepository;

    EmployeeService(EmployeeRepository employeeRepository){
    this.employeeRepository = employeeRepository;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        String originalFilename = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
        Files.write(fileNameAndPath, file.getBytes());
        employee.setProfileImage(originalFilename);
        employeeRepository.save(employee);
    }

    public class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(Long employeeId) {
            super("Employee not found with ID: " + employeeId);
        }
    }
}
