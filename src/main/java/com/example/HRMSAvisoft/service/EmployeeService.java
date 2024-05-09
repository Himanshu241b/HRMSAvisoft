package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class EmployeeService {


    private final Cloudinary cloudinary;

    private final EmployeeRepository employeeRepository;

    private final AddressRepository addressRepository;
    @Autowired
    EmployeeService(EmployeeRepository employeeRepository, Cloudinary cloudinary, AddressRepository addressRepository){
        this.addressRepository = addressRepository;
        this.employeeRepository = employeeRepository;
        this.cloudinary = cloudinary;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException, RuntimeException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        // Upload file to Cloudinary
        Map<?, ?> params = ObjectUtils.asMap(
                "public_id", "profile_images/" + employeeId, // You can change the public_id format as you need
                "folder", "profile_images" // Optional: folder in Cloudinary to organize your images
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

        // Get the URL of the uploaded image
        String imageUrl = (String) uploadResult.get("secure_url");

        // Set the image URL to the employee object and save it
        employee.setProfileImage(imageUrl);
        employeeRepository.save(employee);
    }


    public List<Employee> searchEmployeesByName(String name)throws IllegalArgumentException{
        if(name == ""){
            throw new IllegalArgumentException("Search field empty");
        }
        if(!validateSearchTerm(name)){
            throw new IllegalArgumentException("Only Alphabets allowed");
        }
        List<Employee> searchedEmployees = employeeRepository.searchEmployeesByName(name);
        return searchedEmployees;
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
    public Employee addAddressToEmployee(Long employeeId, Address address) throws EmployeeNotFoundException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));

        Address newAddress =addressRepository.save(address);
        employee.getAddresses().add(newAddress);

        return employeeRepository.save(employee);
    }
    public Employee removeAddressFromEmployee(Long employeeId, Long addressId) throws EmployeeNotFoundException {
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

    private boolean validateSearchTerm(String term) {
        // Regular expression pattern to allow only alphabets and spaces
        String pattern = "^[a-zA-Z\\s]+$";
        return Pattern.matches(pattern, term);
    }

    public static class EmployeeNotFoundException extends Exception {
        public EmployeeNotFoundException(Long employeeId) {
            super("Employee not found with ID: " + employeeId);
        }
    }


    public static class AddressNotFoundException extends RuntimeException{
        public AddressNotFoundException(Long addressId){super("Address not found with ID: " + addressId);}
        public AddressNotFoundException(Long employeeId,Long addressId){super("Employee with ID :"+employeeId+" does not contain address with ID : "+addressId);}
    }


}
