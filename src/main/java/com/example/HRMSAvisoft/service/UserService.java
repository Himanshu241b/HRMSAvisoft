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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmployeeRepository employeeRepository;
    public User getUserById(Long id){
        User user=userRepository.getByUserId(id);

        return user;
    }
    public UserDTO saveUser(UserDTO userDTO,User loggedInUser){

        User user1 = userRepository.getByEmail(userDTO.getEmail());

        if(user1!=null){
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }

        User newUser=new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));


        newUser.setCreatedBy(loggedInUser);
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newUser.setCreatedAt(createdAt.format(formatter));

        Role roleToAdd = roleRepository.getByRole(userDTO.getRole());
        newUser.getRoles().add(roleToAdd);

        Employee newEmployee = new Employee();
        Employee savedEmployee =employeeRepository.save(newEmployee);

        newUser.setEmployee(savedEmployee);
        User createdUser=userRepository.save(newUser);
        return modelMapper.map(createdUser,UserDTO.class);

    }
    public User userLogin(LoginUserDTO loginUserDTO) throws EntityNotFoundException, WrongPasswordCredentialsException{
        User loggedInUser = userRepository.getByEmail(loginUserDTO.getEmail());
        if(loggedInUser == null){
            throw new EntityNotFoundException("User with email " + loginUserDTO.getEmail()+" not found");
        }
        if(passwordEncoder.matches(loginUserDTO.getPassword(), loggedInUser.getPassword())){
            return loggedInUser;
        }
        else{
            throw new WrongPasswordCredentialsException(loggedInUser.getEmail());
        }
    }
    public User superAdminLogin(LoginUserDTO loginUserDTO)throws EntityNotFoundException, WrongPasswordCredentialsException,RoleDoesNotMatchException{
        User loggedInUser=userRepository.getByEmail(loginUserDTO.getEmail());
        if(loggedInUser==null){
            throw new EntityNotFoundException("User with email " + loginUserDTO.getEmail()+" not found");
        }else {
            Role superAdmin = roleRepository.getByRole("super_admin");

             if (!loggedInUser.getRoles().contains(superAdmin)) {
                throw new RoleDoesNotMatchException("User does not have superAdmin as role ");
            } else if (passwordEncoder.matches(loginUserDTO.getPassword(), loggedInUser.getPassword())) {
                return loggedInUser;
            } else {
                throw new WrongPasswordCredentialsException(loggedInUser.getEmail());
            }
        }


    }
    public static class WrongPasswordCredentialsException extends IllegalAccessException{
        public WrongPasswordCredentialsException(String email){
            super("Wrong password for " + email);
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException{
        public EmailAlreadyExistsException(String email){
            super(email+ " already exists");
        }
    }
    public static class RoleDoesNotMatchException extends IllegalAccessException{
        public RoleDoesNotMatchException(String message){super(message);}
    }
}
