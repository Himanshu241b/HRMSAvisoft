package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserResponseDTO {
    private Long userId;
    private String employeeCode;
    private String email;
    private String createdAt;
    private Set<Role> roles;
    private Department department;
    private String firstName;
    private String lastName;
    private String contact;
    private List<Address> addresses;
    private String adhaarNumber;
    private String panNumber;
    private String uanNumber;
    private Position position;
    private String joinDate;
    private Gender gender;
    private String profileImage;
    private String dateOfBirth;
    private BigDecimal salary;
    private Account account;
}
