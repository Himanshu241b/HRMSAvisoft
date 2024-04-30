package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.dto.LoginUserResponseDTO;
import com.example.HRMSAvisoft.dto.UserDTO;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }


    @PostMapping("/v1/saveUser")
    @PreAuthorize("hasAnyAuthority('Role_super_admin','Role_admin')")

    public ResponseEntity<UserDTO>saveUser(@AuthenticationPrincipal User loggedInUser, @RequestBody UserDTO userDTO ) {
        UserDTO createdUser =userService.saveUser(userDTO,loggedInUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("/v1/login")
    public ResponseEntity<LoginUserResponseDTO> userLogin(@RequestBody LoginUserDTO loginUserDTO)throws EntityNotFoundException, UserService.WrongPasswordCredentialsException {
        User loggedInUser = userService.userLogin(loginUserDTO);
        LoginUserResponseDTO userResponse = modelMapper.map(loggedInUser, LoginUserResponseDTO.class);
        userResponse.setToken(
                JWTService.createJWT(loggedInUser.getUserId(), loggedInUser.getRoles()));
        return ResponseEntity.ok(userResponse);
    }
    @PostMapping("/v1/loginAsSuperAdmin")
    public  ResponseEntity<LoginUserResponseDTO>superAdminLogin(@RequestBody LoginUserDTO loginUserDTO)throws EntityNotFoundException,UserService.WrongPasswordCredentialsException,UserService.RoleDoesNotMatchException
    {
        User loggedInUser = userService.superAdminLogin(loginUserDTO);
        LoginUserResponseDTO userResponse = modelMapper.map(loggedInUser, LoginUserResponseDTO.class);
        userResponse.setToken(
                JWTService.createJWT(loggedInUser.getUserId(), loggedInUser.getRoles()));
        return ResponseEntity.ok(userResponse);
    }
    @ExceptionHandler({UserService.WrongPasswordCredentialsException.class,EntityNotFoundException.class,UserService.EmailAlreadyExistsException.class,UserService.RoleDoesNotMatchException.class})
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
