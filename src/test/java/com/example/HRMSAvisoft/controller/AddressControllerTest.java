package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.AddressService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private  AddressService addressService;
    @InjectMocks
    private AddressController addressController;



}
