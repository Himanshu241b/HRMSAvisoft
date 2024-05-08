package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/profileImages";

    private EmployeeRepository employeeRepository;
    private AddressRepository addressRepository;

    EmployeeService(EmployeeRepository employeeRepository,AddressRepository addressRepository){
    this.employeeRepository = employeeRepository;
    this.addressRepository = addressRepository;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        String originalFilename = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
        Files.write(fileNameAndPath, file.getBytes());
        employee.setProfileImage(originalFilename);
        employeeRepository.save(employee);
    }
    public List<Employee> getAllEmployees()throws DataAccessException
    {
        return employeeRepository.findAll();
    }
    public Employee getEmployeeById(Long employeeId)throws EmployeeNotFoundException,NullPointerException
    {
      Employee employee= employeeRepository.getByEmployeeId(employeeId);
      if(employee!=null)return employee;
      else {
          throw new EmployeeNotFoundException(employeeId);
      }
    }
    public void deleteEmployeeById(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }
    public Employee addAddressToEmployee(Long employeeId, Address address) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));

        Address newAddress =addressRepository.save(address);
        employee.getAddresses().add(newAddress);

        return employeeRepository.save(employee);
    }
    public Employee removeAddressFromEmployee(Long employeeId, Long addressId) {
        // Retrieve the employee entity by its ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));

        Address addressToRemove=addressRepository.findById(addressId)
                .orElseThrow(()->new AddressNotFoundException(addressId));

        if (addressToRemove != null) {
            if (employee.getAddresses().contains(addressToRemove)) {
                employee.getAddresses().remove(addressToRemove);
            } else {
               throw new AddressNotFoundException(employeeId,addressId);
            }
        } else {
            throw new AddressNotFoundException(addressId);
        }

        return employeeRepository.save(employee);
    }
    public class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(Long employeeId) {
            super("Employee not found with ID: " + employeeId);
        }
    }
    public class AddressNotFoundException extends RuntimeException{
        public AddressNotFoundException(Long addressId){super("Address not found with ID: " + addressId);}
        public AddressNotFoundException(Long employeeId,Long addressId){super("Employee with ID :"+employeeId+" does not contain address with ID : "+addressId);}
    }
}
