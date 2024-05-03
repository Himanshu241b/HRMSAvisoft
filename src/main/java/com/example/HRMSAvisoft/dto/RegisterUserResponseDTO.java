package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
public class RegisterUserResponseDTO {
    private String email;
    private String password;
    private Set<Role> role;
}
