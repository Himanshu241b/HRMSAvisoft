package com.example.HRMSAvisoft.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.HRMSAvisoft.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JWTServiceTests {

    @Mock
    private Algorithm algorithmMock;

    @InjectMocks
    private JWTService jwtService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("test_JWTKeySetter")
    public void testSetJwtKey() throws NoSuchFieldException, IllegalAccessException {
        String jwtKey = "test_jwt_key";
        when(algorithmMock.getName()).thenReturn("HMAC256"); // Mock the behavior of Algorithm

        jwtService.setJwtKey(jwtKey);

        Field jwtKeyField = JWTService.class.getDeclaredField("JWT_KEY");
        jwtKeyField.setAccessible(true);
        String actualJwtKey = (String) jwtKeyField.get(null);
        assertEquals(jwtKey, actualJwtKey); // Ensure JWT_KEY is set correctly
    }

    @Test
    @DisplayName("test_CreateJWT")
    public void testCreateJWT() {
        // Given
        Long userId = 123L;
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("admin"));


        String jwt = jwtService.createJWT(userId, roles);

        DecodedJWT decodedJWT = jwtService.retrieveJWT(jwt);
        assertEquals(userId.toString(), decodedJWT.getSubject());
        List<String> retrievedRoles = jwtService.retrieveRoles(jwt);
        assertEquals(1, retrievedRoles.size());
        assertEquals("admin", retrievedRoles.get(0));

    }

    @Test
    @DisplayName("test_RetrieveUserId")
    public void testRetrieveUserId() {

        Long userId = 123L;
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("admin"));

        String jwt = jwtService.createJWT(userId, roles);

        Long userIdFromToken = jwtService.retrieveUserId(jwt);

        assertEquals(123L, userIdFromToken);
    }

    @Test
    @DisplayName("test_RetrieveRoles")
    public void testRetrieveRoles() {

        Long userId = 123L;
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("admin"));

        String jwt = jwtService.createJWT(userId, roles);


        List<String> rolesFromToken = jwtService.retrieveRoles(jwt);

        assertEquals(1, rolesFromToken.size());
        assertEquals("admin", rolesFromToken.get(0));
    }

}
