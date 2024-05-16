package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.DepartmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmployeeController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class DepartmentControllerTests {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    DepartmentService departmentService;

    @Test
    @DisplayName("test_getAllDepartments")
    void testGetAllDepartments() throws Exception {
        List<Department> departmentsList = new ArrayList<>();
        departmentsList.add(new Department(1L, "Mern", "Mern department", new Employee()));
        departmentsList.add(new Department(2L, "Java", "Java department", new Employee()));

        when(departmentService.getAllDepartments()).thenReturn(departmentsList);
        this.mockMvc.perform(get("/api/v1/department/employee/2L")).andDo(print());

    }
}
