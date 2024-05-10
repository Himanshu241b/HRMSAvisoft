package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.UpdateEmployeeDetailsDTO;
import com.example.HRMSAvisoft.dto.UpdatePersonalDetailsDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Gender;
import com.example.HRMSAvisoft.entity.Position;
import com.example.HRMSAvisoft.service.AddressService;
import com.example.HRMSAvisoft.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private AddressService addressService;
    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeController employeeController;
    HttpClient client;
    String port;

    @Value("${offset.uploadImage.url}")
    String uploadImageUrl;

    @Value("${searchEmployee.url}")
    String searchEmployeeUrl;

    @BeforeEach
    public void setUp() {
         mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService)).build();

        client = HttpClient.newHttpClient();
        port = "5555";
    }
    @Test
    @WithMockUser
    @DisplayName("Test Get All Employees")
    public void testGetAllEmployees() throws Exception {
        // Given
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(employee));

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/getAllEmployees"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Employees").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Employees[0].employeeId").isNotEmpty())
                .andExpect(jsonPath("$.Employees[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employees Retrieved Successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Success").value(true));
        verify(employeeService, times(1)).getAllEmployees();
    }
    @Test
    @WithMockUser
    void testGetEmployeeById() throws Exception {
        // Given
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("John");
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Employee.employeeId").value(employeeId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Employee.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee retrieved Successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Status").value(true));
        verify(employeeService, times(1)).getEmployeeById(employeeId);

    }
    @Test
    void testUpdatePersonalDetails() throws Exception {
        // Given
        Long employeeId = 1L;
        UpdatePersonalDetailsDTO updatePersonalDetails = new UpdatePersonalDetailsDTO();
        updatePersonalDetails.setFirstName("John");
        updatePersonalDetails.setLastName("Doe");
        updatePersonalDetails.setGender(Gender.MALE);
        updatePersonalDetails.setContact("1234567890");
        updatePersonalDetails.setDateOfBirth("1990-01-01");

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(employeeId);
        existingEmployee.setFirstName("OldFirst");
        existingEmployee.setLastName("OldLast");
        existingEmployee.setGender(Gender.FEMALE);
        existingEmployee.setContact("OldContact");
        existingEmployee.setDateOfBirth("1990-02-02");


        when(employeeService.getEmployeeById(employeeId)).thenReturn(existingEmployee);
        when(employeeService.updateEmployee(existingEmployee)).thenReturn(existingEmployee);

        // When/Then
        mockMvc.perform(put("/api/v1/employee/updatePersonalDetails/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"firstName\": \"John\",\n" +
                                "  \"lastName\": \"Doe\",\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"contact\": \"1234567890\",\n" +
                                "  \"dateOfBirth\": \"1990-01-01\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UpdatedEmployee.employeeId").value(employeeId))
                .andExpect(jsonPath("$.UpdatedEmployee.firstName").value("John"))
                .andExpect(jsonPath("$.UpdatedEmployee.lastName").value("Doe"))
                .andExpect(jsonPath("$.UpdatedEmployee.gender").value("MALE"))
                .andExpect(jsonPath("$.UpdatedEmployee.contact").value("1234567890"))
                .andExpect(jsonPath("$.UpdatedEmployee.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.message").value("Personal Details Updated"))
                .andExpect(jsonPath("$.Status").value(true));

        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(employeeService, times(1)).updateEmployee(existingEmployee);
    }
    @Test
    public void testUpdateEmployeeDetails() throws Exception {
        // Prepare test data
        Long employeeId = 1L;
        UpdateEmployeeDetailsDTO updateEmployeeDetailsDTO = new UpdateEmployeeDetailsDTO();
        updateEmployeeDetailsDTO.setFirstName("John");
        updateEmployeeDetailsDTO.setLastName("Doe");
        updateEmployeeDetailsDTO.setContact("1234567890");
        updateEmployeeDetailsDTO.setGender(Gender.MALE);
        updateEmployeeDetailsDTO.setDateOfBirth("25/02/2001");
        updateEmployeeDetailsDTO.setJoinDate("24/01/2024");
        updateEmployeeDetailsDTO.setPosition(Position.DEVELOPER);
        updateEmployeeDetailsDTO.setSalary(50000);

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(employeeId);
        // Mock behavior of EmployeeService
        when(employeeService.getEmployeeById(employeeId)).thenReturn(existingEmployee);
        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(existingEmployee);



        // Perform PUT request
        mockMvc.perform(put("/api/v1/employee/updateEmployeeDetails/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateEmployeeDetailsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.UpdatedEmployee.employeeId").value(employeeId))
                .andExpect(jsonPath("$.message").value("Personal Details Updated"))
                .andExpect(jsonPath("$.Status").value(true));

        // Verify that the EmployeeService methods were called
        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
        verifyNoMoreInteractions(employeeService);
    }
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



//    @Test
//    @DisplayName("test_image_upload_success")
//    @Transactional
//    void test_imageUploadSuccess() throws IOException, InterruptedException{
//
//        MultipartFile file = Mockito.mock(MultipartFile.class);
//
//        byte[] fileContent = "file content".getBytes();
//        Mockito.when(file.getBytes()).thenReturn(fileContent);
//
//        HttpRequest postRequest = HttpRequest.newBuilder()
//                .uri(URI.create(uploadImageUrl))
//                .header("Content-Type", "multipart/form-data")
//                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
//                .build();
//
//        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
//        assertEquals(200, postResponse.statusCode());
//    }

    @Test
    @DisplayName("test_search_employee_success")
    void test_search_employee_success() throws Exception {

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        employee1.setFirstName("test");
        employee1.setLastName("user");
        employee2.setFirstName("test2");
        employee2.setLastName("user2");
        List<Employee> mockEmployees = Arrays.asList(employee1, employee2);

        // Mock the service method
        when(employeeService.searchEmployeesByName("test")).thenReturn(mockEmployees);

        // Perform the MVC request and verify the results
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:5555/api/v1/employee/searchEmployee")
                        .param("name", "test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("test"))
                .andExpect(jsonPath("$[1].firstName").value("test2"))
                .andExpect(jsonPath("$[0].lastName").value("user"))
                .andExpect(jsonPath("$[1].lastName").value("user2"));
    }
}
