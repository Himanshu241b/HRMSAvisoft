package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Gender;
import com.example.HRMSAvisoft.entity.Position;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDTO {
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String employeeCode;
    private String contact;
    private Position position;
    private String joinDate;
    private Gender gender;
    private String adhaarNumber;
    private String panNumber;
    private String uanNumber;
    private String dateOfBirth;
    private BigDecimal salary;
}


