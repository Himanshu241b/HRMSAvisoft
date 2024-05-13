package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.entity.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class EmergencyContactRepositoryTests {

    @Autowired
    EmergencyContactRepository emergencyContactRepository;

    @BeforeEach
    void setup(){
        Long emergencyContactId = 1L;
        String contact = "9820640927";
        String relationship = "father";

        EmergencyContact emergencyContact = new EmergencyContact(emergencyContactId, contact, relationship);
        emergencyContactRepository.save(emergencyContact);
    }
    @Test
    @DisplayName("test_getEmergencyContactsById")
    void testGetEmergencyContactsById() {
        EmergencyContact emergencyContact = emergencyContactRepository.findById(1L).orElse(null);

        assertEquals(1L, emergencyContact.getEmergencyContactId());
        assertEquals("9820640927", emergencyContact.getContact());
        assertEquals("father", emergencyContact.getRelationship());
    }
}
