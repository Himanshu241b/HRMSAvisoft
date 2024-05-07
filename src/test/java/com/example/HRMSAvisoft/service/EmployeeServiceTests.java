package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeServiceTests {

        @Test
        @DisplayName("test_validEmployeeIdAndValidImageFile")
        public void test_validEmployeeIdAndValidImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);


            Employee employee = new Employee();
            employee.setEmployeeId(1L);

            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            MultipartFile file = Mockito.mock(MultipartFile.class);

            Mockito.when(file.getOriginalFilename()).thenReturn("image.jpg");

            byte[] fileContent = "file content".getBytes();
            Mockito.when(file.getBytes()).thenReturn(fileContent);

            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);

            employeeService.uploadProfileImage(1L, file);

            assertEquals("image.jpg", employee.getProfileImage());

            Mockito.verify(employeeRepository).save(employee);
        }


        @Test
        @DisplayName("test_invalidEmployeeIdAndValidImageFile")
        public void test_invalidEmployeeIdAndValidImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);
            Mockito.when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

            MultipartFile file = Mockito.mock(MultipartFile.class);

            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);

            assertThrows(EmployeeService.EmployeeNotFoundException.class, () -> {
                employeeService.uploadProfileImage(2L, file);
            });
        }

        @Test
        @DisplayName("test_validEmployeeIdAndNullImageFile")
        public void test_validEmployeeIdAndNullImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);

            Employee employee = new Employee();
            employee.setEmployeeId(1L);

            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);

            assertThrows(NullPointerException.class, () -> {
                employeeService.uploadProfileImage(1L, null);
            });
        }

        @Test
        @DisplayName("test_validEmployeeIdAndEmptyImageFile")
        public void test_validEmployeeIdAndEmptyImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);

            Employee employee = new Employee();
            employee.setEmployeeId(1L);

            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            MultipartFile file = Mockito.mock(MultipartFile.class);

            byte[] fileContent = new byte[0];
            Mockito.when(file.getBytes()).thenReturn(fileContent);

            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);

            assertThrows(NullPointerException.class, () -> {
                employeeService.uploadProfileImage(1L, file);
            });
        }

    }

