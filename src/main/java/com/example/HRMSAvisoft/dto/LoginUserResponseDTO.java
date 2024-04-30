package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserResponseDTO {
    private Long userId;
    private String email;
    private String token;
    private Set<Role> roles;
}