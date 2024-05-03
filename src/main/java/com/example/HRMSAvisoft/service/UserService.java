package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateUserDTO;
import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {

    final private UserRepository userRepository;

    final private PasswordEncoder passwordEncoder;

    final private RoleRepository roleRepository;

    final private EmployeeRepository employeeRepository;
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,RoleRepository roleRepository,EmployeeRepository employeeRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.employeeRepository=employeeRepository;
    }
    public User getUserById(Long id){
        User user=userRepository.getByUserId(id);

        return user;
    }
    public Long saveUser(CreateUserDTO createUserDTO, User loggedInUser){

        User alreadyRegisteredUser = userRepository.getByEmail(createUserDTO.getEmail());

        if(alreadyRegisteredUser!=null){
            throw new EmailAlreadyExistsException(createUserDTO.getEmail());
        }

        User newUser=new User();
        newUser.setEmail(createUserDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));


        newUser.setCreatedBy(loggedInUser);
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newUser.setCreatedAt(createdAt.format(createdAtFormatter));

        Role roleToAdd = roleRepository.getByRole(createUserDTO.getRole());
        newUser.getRoles().add(roleToAdd);

        // make employee instance corresponding to the user and set some data of employee

        Employee newEmployee = new Employee();
        newEmployee.setFirstName(createUserDTO.getFirstName());
        newEmployee.setLastName(createUserDTO.getLastName());
        newEmployee.setJoinDate(createUserDTO.getJoinDate());
        newEmployee.setGender(createUserDTO.getGender());
        newEmployee.setPosition(createUserDTO.getPosition());
        newEmployee.setSalary(createUserDTO.getSalary());
        newEmployee.setDateOfBirth(createUserDTO.getDateOfBirth());
        Employee savedEmployee =employeeRepository.save(newEmployee);

        newUser.setEmployee(savedEmployee);
        userRepository.save(newUser);

        return savedEmployee.getEmployeeId();
    }

    public User userLogin(LoginUserDTO loginUserDTO) throws EntityNotFoundException, WrongPasswordCredentialsException{
        User loggedInUser = userRepository.getByEmail(loginUserDTO.getEmail());
        if(loggedInUser == null){
            throw new EntityNotFoundException("User with email " + loginUserDTO.getEmail()+" not found");
        }
        else if(passwordEncoder.matches(loginUserDTO.getPassword(), loggedInUser.getPassword())){
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
