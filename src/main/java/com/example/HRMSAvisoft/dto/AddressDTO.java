package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String propertyNumber;
    private AddressType addressType;
    private Long zipCode;
    private String city;
    private String state;
    private String country;

}
