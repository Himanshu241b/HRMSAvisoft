package com.example.HRMSAvisoft.controller;


import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.dto.LoginUserResponseDTO;
import com.example.HRMSAvisoft.dto.RegisterUserResponseDTO;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private JWTService jwtService;
//
//    @MockBean
//    private ModelMapper modelMapper;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//    @InjectMocks
//    private UserController userController;
//
//
//    @Test
//    @WithMockUser
//    public void testHelloEndpoint() throws Exception {
//        mockMvc.perform(get("/api/v1/user/hello"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Hello"));
//    }
//
//    @Test
//   @WithMockUser(roles = {"Role_super_admin"})
//    public void testSaveUserEndpoint() throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setPassword("password");
//        userDTO.setRole("ROLE_USER");
//
//        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
//
//        RegisterUserResponseDTO responseDTO = new RegisterUserResponseDTO();
//        responseDTO.setEmail("test@example.com");
//        responseDTO.setPassword(passwordEncoder.encode("password"));
//        responseDTO.setRole(Set.of(new Role("ROLE_USER")));
//
//        when(userService.saveUser(Mockito.any(), Mockito.any())).thenReturn(responseDTO);
//
//        mockMvc.perform(post("/api/v1/user/saveUser")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(userDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.password").value("password"))
//                .andExpect(jsonPath("$.role[0]").value("ROLE_USER"));
//    }
//
//    @Test
//    void testUserLogin() throws Exception{
//        // Mock input data
//        LoginUserDTO loginUserDTO = new LoginUserDTO();
//        loginUserDTO.setEmail("test@example.com");
//        loginUserDTO.setPassword("password");
//
//        // Mock the userService method
//        User user = new User();
//        user.setUserId(1L);
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//
//        // Properly mock userService
//        when(userService.userLogin(loginUserDTO)).thenReturn(user);
//
//
//        // Mock the modelMapper method
//        LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO();
//        when(modelMapper.map(user, LoginUserResponseDTO.class)).thenReturn(loginUserResponseDTO);
//
//        // Call the controller method
//        ResponseEntity<LoginUserResponseDTO> responseEntity = userController.userLogin(loginUserDTO);
//
//        // Assertions
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(loginUserResponseDTO, responseEntity.getBody());
//        assertEquals("mockedToken", loginUserResponseDTO.getToken());
//    }
//
//    private String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}



