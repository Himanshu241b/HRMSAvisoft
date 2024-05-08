package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@Transactional
public class UserController {

    private UserService userService;


    private JWTService jwtService;

    private ModelMapper modelMapper;
    public UserController(UserService userService,JWTService jwtService,ModelMapper modelMapper)
    {
        this.userService=userService;
        this.jwtService=jwtService;
        this.modelMapper=modelMapper;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }


    @PostMapping("/saveUser")
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")
    public ResponseEntity<CreateUserResponseDTO>saveUser(@AuthenticationPrincipal User loggedInUser,
                                        @RequestBody CreateUserDTO createUserDTO) throws IOException {
        Employee createdUserEmployee = userService.saveUser(createUserDTO, loggedInUser);
        CreateUserResponseDTO createUserResponseDTO = new CreateUserResponseDTO();
        createUserResponseDTO.setMessage("User Created Successfully");
        createUserResponseDTO.setEmployeeId(createdUserEmployee.getEmployeeId());
        String profileImageOfEmployee = (createdUserEmployee.getProfileImage() != null) ? createdUserEmployee.getProfileImage() : "https://api.dicebear.com/5.x/initials/svg?seed="+createdUserEmployee.getFirstName()+" "+createdUserEmployee.getLastName();
        createUserResponseDTO.setProfileImage(profileImageOfEmployee);
        String message = "{\"message\": \"User created successfully\"}";
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDTO> userLogin(@RequestBody LoginUserDTO loginUserDTO)throws EntityNotFoundException, UserService.WrongPasswordCredentialsException {
        User loggedInUser = userService.userLogin(loginUserDTO);

        LoginUserResponseDTO userResponse = new LoginUserResponseDTO();
        if(loggedInUser!=null) {
            userResponse.setMessage("Login Successful");
            userResponse.setUserId(loggedInUser.getUserId());
            userResponse.setEmail(loggedInUser.getEmail());
            userResponse.setRoles(loggedInUser.getRoles());
            Employee employee = loggedInUser.getEmployee();

            userResponse.setFirstName(employee.getFirstName());
            userResponse.setLastName(employee.getLastName());
            userResponse.setContact(employee.getContact());
            userResponse.setAddresses(employee.getAddresses());
            userResponse.setPosition(employee.getPosition());
            userResponse.setJoinDate(employee.getJoinDate());
            userResponse.setGender(employee.getGender());
            String profileImageOfEmployee = (employee.getProfileImage() != null) ? employee.getProfileImage() : "https://api.dicebear.com/5.x/initials/svg?seed="+employee.getFirstName()+" "+employee.getLastName();
            userResponse.setProfileImage(profileImageOfEmployee);
            userResponse.setDateOfBirth(employee.getDateOfBirth());
            userResponse.setAccount(employee.getAccount());
            userResponse.setSalary(employee.getSalary());
        }
        userResponse.setToken(
                JWTService.createJWT(loggedInUser.getUserId(), loggedInUser.getRoles()));
        return ResponseEntity.ok(userResponse);

    }

    @PostMapping("/loginAsSuperAdmin")
    public  ResponseEntity<LoginUserResponseDTO>superAdminLogin(@RequestBody LoginUserDTO loginUserDTO)throws EntityNotFoundException,UserService.WrongPasswordCredentialsException,UserService.RoleDoesNotMatchException
    {
        User loggedInUser = userService.superAdminLogin(loginUserDTO);

        LoginUserResponseDTO userResponse = new LoginUserResponseDTO();
        userResponse.setMessage("Login Successful");
        userResponse.setUserId(loggedInUser.getUserId());
        userResponse.setEmail(loggedInUser.getEmail());
        userResponse.setRoles(loggedInUser.getRoles());
        Employee employee = loggedInUser.getEmployee();

        userResponse.setFirstName(employee.getFirstName());
        userResponse.setLastName(employee.getLastName());
        userResponse.setContact(employee.getContact());
        userResponse.setAddresses(employee.getAddresses());
        userResponse.setPosition(employee.getPosition());
        userResponse.setJoinDate(employee.getJoinDate());
        userResponse.setGender(employee.getGender());
        String profileImageOfEmployee = (employee.getProfileImage() != null) ? employee.getProfileImage() : "https://api.dicebear.com/5.x/initials/svg?seed="+employee.getFirstName()+" "+employee.getLastName();
        userResponse.setProfileImage(profileImageOfEmployee);
        userResponse.setDateOfBirth(employee.getDateOfBirth());
        userResponse.setAccount(employee.getAccount());
        userResponse.setSalary(employee.getSalary());
        userResponse.setToken(
                JWTService.createJWT(loggedInUser.getUserId(), loggedInUser.getRoles()));
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId)throws UserService.UserNotFoundException
    {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("User Deleted Successfully");
    }
    @ExceptionHandler({UserService.WrongPasswordCredentialsException.class,EntityNotFoundException.class,UserService.EmailAlreadyExistsException.class,UserService.RoleDoesNotMatchException.class, IOException.class})
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EntityNotFoundException){
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof UserService.WrongPasswordCredentialsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof IOException){
            message = "Failed to upload Profile Image";
            status = HttpStatus.FAILED_DEPENDENCY;
        }
        else if (exception instanceof UserService.EmailAlreadyExistsException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof  UserService.RoleDoesNotMatchException){
            message=exception.getMessage();
            status=HttpStatus.BAD_REQUEST;
        }
        else{
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}


