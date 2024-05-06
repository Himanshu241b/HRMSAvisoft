package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class EmployeeService {


    private final Cloudinary cloudinary;

    private final EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService(EmployeeRepository employeeRepository, Cloudinary cloudinary){
    this.employeeRepository = employeeRepository;
    this.cloudinary = cloudinary;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException {
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

    public class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(Long employeeId) {
            super("Employee not found with ID: " + employeeId);
        }
    }
}
