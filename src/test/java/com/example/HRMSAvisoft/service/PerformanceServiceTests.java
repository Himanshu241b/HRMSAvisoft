package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.PerformanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTests {

    @InjectMocks
    PerformanceService performanceService;

    @Mock
    PerformanceRepository performanceRepository;

    @Mock
    EmployeeRepository employeeRepository;
    @Test
    public void test_returns_performance_list_for_valid_id() throws EmployeeNotFoundException {
        
        EmployeeRepository mockEmployeeRepository = Mockito.mock(EmployeeRepository.class);
        PerformanceRepository mockPerformanceRepository = Mockito.mock(PerformanceRepository.class);
        PerformanceService performanceService = new PerformanceService(mockPerformanceRepository, mockEmployeeRepository);
        Long validEmployeeId = 1L;
        Employee employee = new Employee();
        employee.setPerformanceList(Arrays.asList(new Performance(), new Performance()));
        Mockito.when(mockEmployeeRepository.findById(validEmployeeId)).thenReturn(Optional.of(employee));


        List<Performance> performances = performanceService.getAllPerformanceOfEmployee(validEmployeeId);

        assertNotNull(performances);
        assertEquals(2, performances.size());
    }
}
