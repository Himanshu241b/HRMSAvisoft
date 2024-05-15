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
    @NotBlank(message = "First name field is empty")
    private String firstName;
    @NotBlank(message = "Last name field is empty")
    private String lastName;
    @NotBlank(message = "Department ID field is empty")
    private Long departmentId;
    @NotBlank(message = "Employee Code field is empty")
    private String employeeCode;
    @NotBlank(message = "Employee Contact field is empty")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",message = "Invalid Format for phone Number")
    private String contact;
    private Position position;
    @NotBlank(message = "Join Date field is empty")
    private String joinDate;
    private Gender gender;
    @Pattern(regexp = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$",message = "Invalid format for Adhaar Number")
    private String adhaarNumber;
    @NotBlank(message = "Pan Number Field is empty")
    private String panNumber;
    @NotBlank(message = "UAN Number fielf is empty")
    private String uanNumber;
    @NotBlank(message = "Date of birth field is empty")
    private String dateOfBirth;
    @NotBlank(message = "Salary field is empty")
    private BigDecimal salary;
}


