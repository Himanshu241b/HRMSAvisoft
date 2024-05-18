package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmergencyContactController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class EmergencyContactControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmergencyContactService emergencyContactService;


    @Test
    @DisplayName("test_getEmergencyContactsOfEmployee")
    public void testGetEmergencyContactsOfEmployee() throws Exception {
        List<EmergencyContact> emergencyContactList = new ArrayList<>();
        EmergencyContact emergencyContact1 = new EmergencyContact(1L, "208520952094", "brother");
        EmergencyContact emergencyContact2 = new EmergencyContact(2L, "208520952094", "father");
        emergencyContactList.add(emergencyContact1);
        emergencyContactList.add(emergencyContact2);

        when(emergencyContactService.getEmergencyContactsOfEmployee(anyLong())).thenReturn(emergencyContactList);

        mockMvc.perform(get("/api/v1/emergencyContact/employee/2L")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].contactId").value(1L))
                .andExpect(jsonPath("$[0].phoneNumber").value("208520952094"))
                .andExpect(jsonPath("$[0].relationship").value("brother"))
                .andExpect(jsonPath("$[1].contactId").value(2L))
                .andExpect(jsonPath("$[1].phoneNumber").value("208520952094"))
                .andExpect(jsonPath("$[1].relationship").value("father"))
                .andDo(print());
    }


    @Test
    @DisplayName("test_addEmergencyContactToEmployee")
    void testAddEmergencyContactToEmployee() throws Exception{
        EmergencyContact emergencyContact1 = new EmergencyContact(1L, "208520952094", "brother");
        this.mockMvc.perform(post("/api/v1/emergencyContact/employee/2L")).andDo(print());
    }
}

