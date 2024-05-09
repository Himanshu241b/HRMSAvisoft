package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;


public class AddressServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    AddressRepository addressRepository;
    @InjectMocks
    AddressService addressService;




}
