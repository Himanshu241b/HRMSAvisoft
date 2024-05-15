package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountControllerTest {
    HttpClient client;
    String port;
    @BeforeEach
    public void setUp(){
        client =HttpClient.newHttpClient();
        port="5555";

    }
    @Test
    @Transactional
    @DisplayName("AddAccount")
    public void addAccount() throws  IOException, InterruptedException,EmployeeNotFoundException {
        Long employeeId = 1L;
        String url = "http://localhost:5555/api/v1/account/2/AddBankAccount";

        // Account DTO
        AddAccountDTO accountDTO = new AddAccountDTO();
        accountDTO.setAccountNumber("1234567890");
        accountDTO.setIfsc("IFSC0123456");
        accountDTO.setBankName("Bank");
        accountDTO.setBranch("Branch");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(accountDTO);

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check the response status code
        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        System.out.println(responseBody);


    }

    @Test
    @Transactional
    @DisplayName("RemoveAccountFromEmployee")
    public void removeAccountFromEmployee_Success() throws IOException, InterruptedException , EmployeeNotFoundException {
        Long employeeId = 2L;
        String url = "http://localhost:5555/api/v1/account/" + employeeId + "/removeAccount";

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check the response status code and body
        assertEquals(200, response.statusCode());
        assertEquals("{\"success\":true,\"message\":\"Account Removed\"}", response.body());
    }

}
