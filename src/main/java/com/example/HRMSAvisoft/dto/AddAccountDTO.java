package com.example.HRMSAvisoft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddAccountDTO {
    private String bankName;
    private String accountNumber;
    private String ifsc;
    private String branch;
}
