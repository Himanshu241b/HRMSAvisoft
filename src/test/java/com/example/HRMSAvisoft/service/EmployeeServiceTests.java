package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.config.CloudinaryConfiguration;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeServiceTests {

        @Value("${cloudinary.cloud_name}")
        private String cloudName = "dbshajpgp";

        @Value("${cloudinary.api_key}")
        private String apiKey = "435238499726496";

        @Value("${cloudinary.api_secret}")
        private String apiSecret = "iv4cQJz8xzihy7W-kiX69Fix0RY";
        @Mock
        private    AddressRepository addressRepository;



        @BeforeEach
        void setup() throws Exception {
            CloudinaryConfiguration cloudinaryConfiguration = Mockito.mock(CloudinaryConfiguration.class);
            Mockito.when(cloudinaryConfiguration.cloudinary())
                    .thenReturn(new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret)));

            Map<?, ?> params = ObjectUtils.asMap(
                    "public_id", "profile_images/" + 1L, // You can change the public_id format as you need
                    "folder", "profile_images" // Optional: folder in Cloudinary to organize your images
            );
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "image.jpg",
                    "image/jpeg",
                    "file content".getBytes()
            );

            Mockito.when(cloudinaryConfiguration.cloudinary().uploader().upload(file.getBytes(), params)).thenReturn(new HashMap());
        }

        @Test
        @DisplayName("test_validEmployeeIdAndValidImageFile")
        public void test_validEmployeeIdAndValidImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);


            Employee employee = new Employee();
            employee.setEmployeeId(1L);

            CloudinaryConfiguration cloudinaryConfiguration = Mockito.mock(CloudinaryConfiguration.class);

            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "image.jpg",
                    "image/jpeg",
                    "file content".getBytes()
            );


            EmployeeService employeeService = new EmployeeService(employeeRepository, addressRepository,cloudinaryConfiguration.cloudinary());

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


            EmployeeService employeeService = new EmployeeService(employeeRepository,addressRepository, cloudinary);

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

            EmployeeService employeeService = new EmployeeService(employeeRepository, addressRepository,cloudinary);

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

            EmployeeService employeeService = new EmployeeService(employeeRepository, addressRepository,cloudinary);

            assertThrows(NullPointerException.class, () -> {
                employeeService.uploadProfileImage(1L, file);
            });
        }

    }

