package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
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
        return "Hello ";
    }


    @PostMapping("/addNewUser")
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    public ResponseEntity<Map<String ,Object>>addNewUser(@AuthenticationPrincipal User loggedInUser,
                                                         @RequestBody @Valid AddNewUserDTO addNewUserDTO)throws IOException,UserService.EmailAlreadyExistsException{
        User createdUser=userService.addNewUser(addNewUserDTO,loggedInUser);
        NewUserResponseDTO newUser=new NewUserResponseDTO();
        newUser.setUserId(createdUser.getUserId());
        newUser.setEmail(createdUser.getEmail());
        newUser.setCreatedAt(createdUser.getCreatedAt());
        newUser.setEmployeeId(createdUser.getEmployee().getEmployeeId());
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "New User Created!!");
        response.put("success", true);
        response.put("newUser", newUser);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody LoginUserDTO loginUserDTO)throws EntityNotFoundException, IllegalArgumentException, UserService.WrongPasswordCredentialsException, UserService.IllegalAccessRoleException {
        User loggedInUser = userService.userLogin(loginUserDTO);

        LoginUserResponseDTO userResponse = new LoginUserResponseDTO();
        if(loggedInUser!=null) {
            userResponse.setUserId(loggedInUser.getUserId());
            userResponse.setEmail(loggedInUser.getEmail());
            userResponse.setRoles(loggedInUser.getRoles());
            userResponse.setCreatedAt(loggedInUser.getCreatedAt());
            Employee employee = loggedInUser.getEmployee();
            userResponse.setFirstName(employee.getFirstName());
            userResponse.setLastName(employee.getLastName());
            userResponse.setContact(employee.getContact());
            userResponse.setDepartment(employee.getDepartment());
            userResponse.setEmployeeCode(employee.getEmployeeCode());
            userResponse.setAdhaarNumber(employee.getAdhaarNumber());
            userResponse.setPanNumber(employee.getPanNumber());
            userResponse.setUanNumber(employee.getUanNumber());
            userResponse.setAddresses(employee.getAddresses());
            userResponse.setPosition(employee.getPosition());
            userResponse.setJoinDate(employee.getJoinDate());
            userResponse.setGender(employee.getGender());
            String userProfileImage = userResponse.getProfileImage() == null ? "https://api.dicebear.com/5.x/initials/svg?seed="+userResponse.getFirstName()+" "+userResponse.getLastName() : userResponse.getProfileImage();
            userResponse.setProfileImage(userProfileImage);
            userResponse.setDateOfBirth(employee.getDateOfBirth());
            userResponse.setAccount(employee.getAccount());
            userResponse.setSalary(employee.getSalary());
        }


        String token = JWTService.createJWT(loggedInUser.getUserId(), loggedInUser.getRoles());

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "Login Successful");
        response.put("success", true);
        response.put("token", token);
        response.put("loginUser", userResponse);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteEmployee(@PathVariable("userId") Long userId)throws EmployeeNotFoundException {
        if(userService.deleteUser(userId))
            return ResponseEntity.status(204).body(null);
        else
            return ResponseEntity.status(500).body(null);
    }



//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
//        // Extract the token from the Authorization header
//        String jwt = token.substring(7); // Assuming "Bearer " prefix is used
//
//        // Add the token to the blacklist
//        jwtBlacklist.addToBlacklist(jwt);
//
//        // You can also implement additional logic here, such as notifying clients of successful logout
//
//        return ResponseEntity.ok("Logout successful");
//    }


    @ExceptionHandler(
            {UserService.WrongPasswordCredentialsException.class
                    ,UserService.EmailAlreadyExistsException.class, IOException.class,
                    UserService.IllegalAccessRoleException.class, IllegalArgumentException.class
                    ,EntityNotFoundException.class
            })
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
         if(exception instanceof IllegalArgumentException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
         else if(exception instanceof EntityNotFoundException){
             message = exception.getMessage();
             status = HttpStatus.BAD_REQUEST;
         }
        else if(exception instanceof UserService.IllegalAccessRoleException){
            message = exception.getMessage();
            status = HttpStatus.UNAUTHORIZED;
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


