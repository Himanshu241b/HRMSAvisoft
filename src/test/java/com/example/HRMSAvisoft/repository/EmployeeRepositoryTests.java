package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("test_save_employee")
    void testSaveEmployee(){
        Long empId = 1L;
        String firstName = "test";
        String lastName = "user";
        String contact = "8539228375";
        BigDecimal salary = new BigDecimal(20000);
        Position position = Position.TESTER;
        Gender gender = Gender.MALE;
        String joinDate = "2022/12/12";
        Account account = new Account();
        String dateOfBirth = "2004/12/22";
        String profileImage = "profile.jpg";

        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setProfileImage(profileImage);
        employee.setJoinDate(joinDate);
        employee.setAccount(account);
        employee.setDateOfBirth(dateOfBirth);

        Employee savedEmployee = employeeRepository.save(employee);

        assertEquals(empId, savedEmployee.getEmployeeId());
        assertEquals(firstName, savedEmployee.getFirstName());
        assertEquals(lastName, savedEmployee.getLastName());
        assertEquals(contact, savedEmployee.getContact());
        assertEquals(salary, savedEmployee.getSalary());
        assertEquals(position, savedEmployee.getPosition());
        assertEquals(gender, savedEmployee.getGender());
        assertEquals(profileImage, savedEmployee.getProfileImage());
        assertEquals(joinDate, savedEmployee.getJoinDate());
        assertEquals(account, savedEmployee.getAccount());
        assertEquals(dateOfBirth, savedEmployee.getDateOfBirth());
    }
}
