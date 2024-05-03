package com.example.HRMSAvisoft.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.dto.RegisterUserResponseDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

//    @Test
//    @DisplayName("Test Save User :Success")
//    void testSaveUser_Success() {
//        // Arrange
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setPassword("password");
//        userDTO.setRole("ROLE_USER");
//
//        User loggedInUser = new User();
//        loggedInUser.setEmail("loggedin@example.com");
//
//        when(userRepository.getByEmail("test@example.com")).thenReturn(null);
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//
//        Role role = new Role();
//        role.setRole("ROLE_USER");
//        when(roleRepository.getByRole("ROLE_USER")).thenReturn(role);
//
//        Employee employee = new Employee();
//        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
//
//        User newUser = new User();
//        newUser.setEmail("test@example.com");
//        newUser.setPassword("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(newUser);
//
//        RegisterUserResponseDTO responseDTO = new RegisterUserResponseDTO();
//        when(modelMapper.map(any(User.class), eq(RegisterUserResponseDTO.class))).thenReturn(responseDTO);
//
//        // Act
//        RegisterUserResponseDTO result = userService.saveUser(userDTO, loggedInUser);
//
//        // Assert
//        assertNotNull(result);
//        verify(userRepository, times(1)).getByEmail("test@example.com");
//        verify(roleRepository, times(1)).getByRole("ROLE_USER");
//        verify(employeeRepository, times(1)).save(any(Employee.class));
//        verify(userRepository, times(1)).save(any(User.class));
//    }

//    @Test
//    @DisplayName("Test Save User :Email Already Exists")
//    void testSaveUser_EmailAlreadyExists() {
//        // Arrange
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setPassword("password");
//        userDTO.setRole("ROLE_USER");
//
//        User existingUser = new User();
//        existingUser.setEmail("test@example.com");
//
//        when(userRepository.getByEmail("test@example.com")).thenReturn(existingUser);
//
//        // Act & Assert
//        assertThrows(UserService.EmailAlreadyExistsException.class, () -> {
//            userService.saveUser(userDTO, new User());
//        });
//
//        verify(userRepository, times(1)).getByEmail("test@example.com");
//        verify(userRepository, never()).save(any(User.class));
//    }
    @Test
    @DisplayName("Test Super Admin Login:Success")
    void testSuperAdminLogin_Success()
    {
        LoginUserDTO loginUserDTO=new LoginUserDTO();
        loginUserDTO.setEmail("test@example.com");
        loginUserDTO.setPassword("password");

        User existingUser=new User();
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");

        Role role=new Role();
        role.setRole("super_admin");

        existingUser.getRoles().add(role);
        when(userRepository.getByEmail("test@example.com")).thenReturn(existingUser);
        when(roleRepository.getByRole("super_admin")).thenReturn(role);
        when(passwordEncoder.matches(loginUserDTO.getPassword(),existingUser.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> {
            User result = userService.superAdminLogin(loginUserDTO);
            assertNotNull(result);
        });
        // Verify repository calls
        verify(userRepository, times(1)).getByEmail("test@example.com");
        verify(roleRepository, times(1)).getByRole("super_admin");

        // Verify interaction with password encoder
        verify(passwordEncoder, times(1)).matches(loginUserDTO.getPassword(), existingUser.getPassword());


    }
    @Test
    @DisplayName("Test Super Admin Login:throws Entity not found exception")
    public void testSuperAdminLogin_throwsEntityNotFoundException(){
        LoginUserDTO loginUserDTO=new LoginUserDTO();
        loginUserDTO.setEmail("test@example.com");
        loginUserDTO.setPassword("password");

        when(userRepository.getByEmail("test@example.com")).thenReturn(null);
        //act
        assertThrows(EntityNotFoundException.class,()->userService.superAdminLogin(loginUserDTO));
        //verify repository calls
        verify(userRepository,times(1)).getByEmail("test@example.com");

    }
    @Test
    @DisplayName("Test Super Admin Login:RoleDoesNotMatchException")
    public void testSuperAdminLogin_throwsRoleDoesNotMatchException(){
        LoginUserDTO loginUserDTO=new LoginUserDTO();
        loginUserDTO.setEmail("test@example.com");
        loginUserDTO.setPassword("password");

        User existingUser=new User();
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");

        Role role=new Role();
        role.setRole("anyrole");

        existingUser.getRoles().add(role);

        when(userRepository.getByEmail("test@example.com")).thenReturn(existingUser);
        when(roleRepository.getByRole("super_admin")).thenReturn(new Role());

        assertThrows(UserService.RoleDoesNotMatchException.class,()->userService.superAdminLogin(loginUserDTO));

        verify(userRepository, times(1)).getByEmail("test@example.com");
        verify(roleRepository, times(1)).getByRole("super_admin");


    }

//    @Test
//    @DisplayName("Test Super Admin login:Wrong Password credentials exception")
//    void testSuperAdminLogin_throwsWrongPasswordCredentialException(){
//        LoginUserDTO loginUserDTO=new LoginUserDTO();
//        loginUserDTO.setEmail("test@example.com");
//        loginUserDTO.setPassword("password");
//
//        User existingUser=new User();
//        existingUser.setEmail("test@example.com");
//        existingUser.setPassword("password");
//        Role role=new Role();
//        role.setRole("super_admin");
//
//        existingUser.getRoles().add(role);
//
//        when(userRepository.getByEmail("test@example.com")).thenReturn(existingUser);
//        when(roleRepository.getByRole("super_admin")).thenReturn(new Role());
//
//    }

}