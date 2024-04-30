package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class UserDTO {
    private String email;
    private String password;
    private String role;

}
