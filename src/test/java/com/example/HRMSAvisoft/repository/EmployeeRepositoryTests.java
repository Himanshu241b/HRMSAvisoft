package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        String dateOfBirth = "2004/12/22";


        Employee employee = new Employee();
        employee.setEmployeeId(empId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContact(contact);
        employee.setSalary(salary);
        employee.setPosition(position);
        employee.setGender(gender);
        employee.setJoinDate(joinDate);
        employee.setDateOfBirth(dateOfBirth);

        Employee savedEmployee = employeeRepository.save(employee);

        assertEquals(empId, savedEmployee.getEmployeeId());
        assertEquals(firstName, savedEmployee.getFirstName());
        assertEquals(lastName, savedEmployee.getLastName());
        assertEquals(contact, savedEmployee.getContact());
        assertEquals(salary, savedEmployee.getSalary());
        assertEquals(position, savedEmployee.getPosition());
        assertEquals(gender, savedEmployee.getGender());
        assertEquals(joinDate, savedEmployee.getJoinDate());
        assertEquals(dateOfBirth, savedEmployee.getDateOfBirth());
    }
}
