package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private EmployeeService employeeService;
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    @DisplayName("Test Get All Employees")
    void testGetAllEmployees() {
        // Given
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);
        employee1.setFirstName("Test1");
        employee1.setLastName("User");

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setFirstName("Test2");
        employee2.setLastName("User");


        List<Employee> employees = Arrays.asList(employee1, employee2);

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<Employee> retrievedEmployees = employeeService.getAllEmployees();

        // Then
        Assertions.assertEquals(2, retrievedEmployees.size());
        Assertions.assertEquals("Test1", retrievedEmployees.get(0).getFirstName());
        Assertions.assertEquals("Test2", retrievedEmployees.get(1).getFirstName());
        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();
    }
    @Test
    @DisplayName("Test Update Employee")
    public void testUpdateEmployee() {
        // Prepare mock data
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1L);
        existingEmployee.setFirstName("John");
        existingEmployee.setLastName("Doe");
        // Update information
        existingEmployee.setFirstName("Updated First Name");
        existingEmployee.setLastName("Updated Last Name");

        // Mock repository behavior
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        // Call the service method
        Employee updatedEmployee = employeeService.updateEmployee(existingEmployee);

        // Assert the result
        assertEquals("Updated First Name", updatedEmployee.getFirstName());
        assertEquals("Updated Last Name", updatedEmployee.getLastName());
    }

    @Test
    @DisplayName("Test Get Employee By ID")
    void testGetEmployeeById() throws Exception{
        // Given
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("John Doe");

        Mockito.when(employeeRepository.getByEmployeeId(employeeId)).thenReturn(employee);

        // When
        Employee retrievedEmployee = employeeService.getEmployeeById(employeeId);

        // Then
        Assertions.assertEquals(employeeId, retrievedEmployee.getEmployeeId());
        Assertions.assertEquals("John Doe", retrievedEmployee.getFirstName());
        Mockito.verify(employeeRepository, Mockito.times(1)).getByEmployeeId(employeeId);
    }
    @Test
    @DisplayName("Test Add Address to Employee")
    void testAddAddressToEmployee() throws Exception{
        // Given
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Address address = new Address();
        address.setPropertyNumber("123");
        address.setCountry("USA");
        employee.getAddresses().add(address);
        // Mock repository behavior
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(addressRepository.save(address)).thenReturn(address);
        when(employeeRepository.save(employee)).thenReturn(employee);
        // When
        Employee updatedEmployee = employeeService.addAddressToEmployee(employeeId, address);

        // Then
        assertNotNull(updatedEmployee);
        assertTrue(updatedEmployee.getAddresses().contains(address));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(addressRepository, times(1)).save(address);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    @DisplayName("Test Remove Address From Employee")
    void testRemoveAddressFromEmployee() throws Exception{
        // Given
        Long employeeId = 1L;
        Long addressId = 2L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Address address = new Address();
        address.setAddressId(addressId);
        employee.getAddresses().add(address);

        // Mock repository behavior
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(employeeRepository.save(employee)).thenReturn(employee);
        // When
        Employee updatedEmployee = employeeService.removeAddressFromEmployee(employeeId, addressId);

        // Then
        assertNotNull(updatedEmployee);
        assertFalse(updatedEmployee.getAddresses().contains(address));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(employeeRepository, times(1)).save(employee);
    }


}
