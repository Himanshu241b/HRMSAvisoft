package com.example.HRMSAvisoft.service;


import com.example.HRMSAvisoft.dto.CreatePerformanceDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final EmployeeRepository employeeRepository;

    PerformanceService(PerformanceRepository performanceRepository, EmployeeRepository employeeRepository) {
        this.performanceRepository = performanceRepository;
        this.employeeRepository = employeeRepository;
    }
    public List<Performance> getAllPerformanceOfEmployee(Long employeeId)throws  EmployeeNotFoundException{
        Employee employeeToFindPerformance = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
        return employeeToFindPerformance.getPerformanceList();
    }

    public Performance addPerformanceOfEmployee(Long employeeId, Long reviewerId, CreatePerformanceDTO createPerformanceDTO)throws EmployeeNotFoundException, IllegalAccessException{
        Employee employeeToAddPerformance = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
        if(employeeToAddPerformance.getDepartment().getManager().getEmployeeId() != reviewerId){
            throw new IllegalAccessException("Forbidden to give Performance Report.");
        }
        Employee reviewer = employeeRepository.findById(reviewerId).orElse(null);
        Performance newPerformanceRecord = new Performance();
        newPerformanceRecord.setComment(createPerformanceDTO.getComment());
        newPerformanceRecord.setRating(createPerformanceDTO.getRating());
        newPerformanceRecord.setReviewer(reviewer);
        LocalDateTime reviewedAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newPerformanceRecord.setReviewDate(createdAtFormatter.format(reviewedAt));
        performanceRepository.save(newPerformanceRecord);
        employeeToAddPerformance.getPerformanceList().add(newPerformanceRecord);
        employeeRepository.save(employeeToAddPerformance);

        return newPerformanceRecord;
    }

}
