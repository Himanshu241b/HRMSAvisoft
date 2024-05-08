package com.example.HRMSAvisoft.controller;
import com.example.HRMSAvisoft.dto.CreateUserDTO;
import com.example.HRMSAvisoft.dto.CreateUserResponseDTO;
import com.example.HRMSAvisoft.dto.LoginUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getUrl() {
        return "http://localhost:" + port + "/api/v1/user";
    }
    @Test
    public void testSaveUser() {
        // Prepare request body
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test9@example.com");
        createUserDTO.setPassword("password");
        createUserDTO.setFirstName("John");
        createUserDTO.setLastName("Doe");

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        // Create HTTP entity with headers and request body
        HttpEntity<CreateUserDTO> requestEntity = new HttpEntity<>(createUserDTO, headers);

        // Send POST request
        ResponseEntity<CreateUserResponseDTO> responseEntity = restTemplate.postForEntity(getUrl() + "/saveUser", requestEntity, CreateUserResponseDTO.class);

        // Verify response status code
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Verify response body
        CreateUserResponseDTO responseBody = responseEntity.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("User Created Successfully");
        assertThat(responseBody.getEmployeeId()).isNotNull();
    }

    @Test
    void testUserLogin() {
        String baseUrl =getUrl()+ "/login";
        LoginUserDTO loginUserDTO=new LoginUserDTO();
        loginUserDTO.setEmail("test@example.com");
        loginUserDTO.setPassword("password");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<LoginUserDTO> request = new HttpEntity<>(loginUserDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testSuperAdminLogin() {
        String baseUrl = getUrl()+"/loginAsSuperAdmin";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        LoginUserDTO loginUserDTO=new LoginUserDTO();
        loginUserDTO.setEmail("avinash@avisoft.io");
        loginUserDTO.setPassword("avinash");

        HttpEntity<LoginUserDTO> request = new HttpEntity<>(loginUserDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}

