package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmployeeController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class EmergencyContactControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Mock
    EmergencyContactService emergencyContactService;

    @Test
    @DisplayName("test_getEmergencyContactsOfEmployee")
    public void testGetEmergencyContactsOfEmployee() throws Exception {
        List<EmergencyContact> emergencyContactList = new ArrayList<EmergencyContact>();
        EmergencyContact emergencyContact1 = new EmergencyContact(1L, "208520952094", "brother");
        EmergencyContact emergencyContact2 = new EmergencyContact(2L, "208520952094", "father");
        emergencyContactList.add(emergencyContact1);
        emergencyContactList.add(emergencyContact2);

        when(emergencyContactService.getEmergencyContactsOfEmployee(anyLong())).thenReturn(emergencyContactList);

        this.mockMvc.perform(get("/api/v1/emergencyContact/employee/2L")).andDo(print());
    }

    @Test
    @DisplayName("test_addEmergencyContactToEmployee")
    void testAddEmergencyContactToEmployee() throws Exception{
        EmergencyContact emergencyContact1 = new EmergencyContact(1L, "208520952094", "brother");
        when(emergencyContactService.addEmergencyContact(any(CreateEmergencyContactDTO.class) ,anyLong())).thenReturn(emergencyContact1);
        this.mockMvc.perform(post("/api/v1/emergencyContact/employee/2L")).andDo(print());
    }
}

