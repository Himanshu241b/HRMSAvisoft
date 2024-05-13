package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.dto.CreateEmployeeDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
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


    private  Cloudinary cloudinary;

    private  EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    EmployeeService(EmployeeRepository employeeRepository, Cloudinary cloudinary){

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

    public Employee saveEmployeePersonalInfo(Long employeeId, CreateEmployeeDTO createEmployeeDTO)throws EmployeeNotFoundException{
        Department departmentOfEmployee =departmentRepository.findById(createEmployeeDTO.getDepartmentId()).orElse(null);

        Employee employeeToAddInfo = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));

        employeeToAddInfo.setFirstName(createEmployeeDTO.getFirstName());
        employeeToAddInfo.setLastName(createEmployeeDTO.getLastName());
        employeeToAddInfo.setContact(createEmployeeDTO.getContact());
        employeeToAddInfo.setGender(createEmployeeDTO.getGender());
        employeeToAddInfo.setSalary(createEmployeeDTO.getSalary());
        employeeToAddInfo.setEmployeeCode(createEmployeeDTO.getEmployeeCode());
        employeeToAddInfo.setDepartment(departmentOfEmployee);
        employeeToAddInfo.setAdhaarNumber(createEmployeeDTO.getAdhaarNumber());
        employeeToAddInfo.setPanNumber(createEmployeeDTO.getPanNumber());
        employeeToAddInfo.setUanNumber(createEmployeeDTO.getUanNumber());
        employeeToAddInfo.setPosition(createEmployeeDTO.getPosition());
        employeeToAddInfo.setJoinDate(createEmployeeDTO.getJoinDate());
        employeeToAddInfo.setAdhaarNumber(createEmployeeDTO.getAdhaarNumber());
        employeeToAddInfo.setPanNumber(createEmployeeDTO.getPanNumber());
        employeeToAddInfo.setUanNumber(createEmployeeDTO.getUanNumber());
        employeeToAddInfo.setDateOfBirth(createEmployeeDTO.getDateOfBirth());

        return employeeRepository.save(employeeToAddInfo);
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





}
