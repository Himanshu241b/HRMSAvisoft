package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.AddressType;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AddressControllerTest {
    private int port;

    private HttpClient client;
    @BeforeEach
    public void setUp(){
        port=5555;
        client = HttpClient.newHttpClient();
    }
    @Test
    @DisplayName("AddAddressToEmployee")
    @Transactional
    public void addAddressToEmployee() throws IOException, InterruptedException {
        Long employeeId = 1L;
        String url = "http://localhost:" + port + "/api/v1/address/" + employeeId + "/addNewAddress";

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPropertyNumber("123");
        addressDTO.setAddressType(AddressType.TEMPORARY);
        addressDTO.setZipCode(12345L);
        addressDTO.setCity("Springfield");
        addressDTO.setState("State");
        addressDTO.setCountry("Country");


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(addressDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }
    @Test
    @Transactional
    @DisplayName("Remove Address from Employee")
    public void removeAddressFromEmployee() throws IOException, InterruptedException {
        Long employeeId = 3L;
        Long addressId = 6L; // Assuming addressId
        String url = "http://localhost:" + port + "/api/v1/address/" + employeeId + "/removeAddress/" + addressId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        // Add more assertions for the response body if needed
    }
    @Test
    @DisplayName("Edit Address")
    @Transactional
    public void editAddress() throws IOException, InterruptedException {
        Long employeeId = 3L;
        Long addressId = 6L; //
        String url = "http://localhost:" + port + "/api/v1/address/" + employeeId + "/editAddress/" + addressId;

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setPropertyNumber("123");
        addressDTO.setAddressType(AddressType.TEMPORARY);
        addressDTO.setZipCode(12345L);
        addressDTO.setCity("Springfield");
        addressDTO.setState("State");
        addressDTO.setCountry("Country");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(addressDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }


}
