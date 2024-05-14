package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import com.example.HRMSAvisoft.service.EmployeeService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(EmployeeController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class EmergencyContactControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyContactService emergencyContactService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup().build();
    }

    @Value(("${offset.getEmergencyContacts.url}"))
    String getEmergencyContactsUrl;

    @Test
    @DisplayName("test_getEmergencyContactsOfEmployee")
    public void testGetEmergencyContactsOfEmployee() throws Exception {
        EmergencyContact emergencyContact = new EmergencyContact(5L, "2807580295", "brother");
        Long employeeId = 1L;
        when(emergencyContactService.getEmergencyContactsOfEmployee(anyLong())).thenReturn(Collections.singletonList(emergencyContact));

        mockMvc.perform(MockMvcRequestBuilders.get(getEmergencyContactsUrl, employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("emergency contacts of employee " + employeeId + " fetched"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.emergencyContacts").exists());
    }
    }

