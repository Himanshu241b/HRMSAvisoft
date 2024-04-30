package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.dto.UserDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);


    }
    @Test
    @DisplayName("Test GetUserById 1")
    void testGetUserById() {
        User user = new User();
        user.setUserId(1L); // Set user ID
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setCreatedAt("29-04-2024");

        // Mock the behavior of userRepository.getByUserId() to return the user when called with the user ID
        when(userRepository.getByUserId(1L)).thenReturn(user);

        // Call the method under test to retrieve the user by ID
        User retrievedUser = userService.getUserById(1L);

        // Assertions
        assertEquals(user.getUserId(), retrievedUser.getUserId());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
        assertEquals(user.getCreatedAt(), retrievedUser.getCreatedAt());
    }
//    @Test
//    void testGetUserById_UserNotFound() {
//        // Mock data
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Call the method under test and assert that it throws EntityNotFoundException
//        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
//    }

    @Test
    @DisplayName("Test Save User: Email Already Exists")
    void testSaveUser_EmailAlreadyExists() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("existing@example.com");
        userDTO.setPassword("password");
        userDTO.setRole("ROLE_USER");

        User loggedInUser = new User();
        loggedInUser.setEmail("admin@example.com");

        when(userRepository.getByEmail(loggedInUser.getEmail())).thenReturn(loggedInUser);
        when(userRepository.getByEmail(userDTO.getEmail())).thenReturn(new User());

        // Act and Assert
        assertThrows(UserService.EmailAlreadyExistsException.class, () -> userService.saveUser(userDTO, loggedInUser));
    }
    @Test
    @DisplayName("Test Login User: User exists and password matches")
    void testLoginUser_Success() throws UserService.WrongPasswordCredentialsException {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encode password

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail(email);
        loginUserDTO.setPassword(password);

        when(userRepository.getByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // Act
        User loggedInUser = userService.userLogin(loginUserDTO);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
    }


    @Test
    @DisplayName("Test Login User: User does not exist")
    void testLoginUser_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "password123";

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail(email);
        loginUserDTO.setPassword(password);

        when(userRepository.getByEmail(email)).thenReturn(null);

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> userService.userLogin(loginUserDTO));
    }

    @Test
    @DisplayName("Test Login User: Password does not match")
    void testLoginUser_PasswordMismatch() {
        // Arrange
        String email = "test@example.com";
        String correctPassword = "password123";
        String incorrectPassword = "wrongPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(correctPassword)); // Encode correct password

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail(email);
        loginUserDTO.setPassword(incorrectPassword); // Use incorrect password

        when(userRepository.getByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(incorrectPassword, user.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(UserService.WrongPasswordCredentialsException.class, () -> userService.userLogin(loginUserDTO));
    }

}
