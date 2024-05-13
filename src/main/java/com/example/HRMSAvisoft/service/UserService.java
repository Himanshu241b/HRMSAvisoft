package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddNewUserDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
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

    public Employee saveUser(CreateUserDTO createUserDTO, User loggedInUser) throws IOException {

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
        newEmployee.setProfileImage("https://api.dicebear.com/5.x/initials/svg?seed="+createUserDTO.getFirstName()+" "+createUserDTO.getLastName());
        Employee savedEmployee = employeeRepository.save(newEmployee);

        newUser.setEmployee(savedEmployee);
        userRepository.save(newUser);

        return savedEmployee;

    }
    public User addNewUser(AddNewUserDTO addNewUserDTO, User loggedInUser)throws IOException,EmailAlreadyExistsException{
        User alreadyRegisteredUser = userRepository.getByEmail(addNewUserDTO.getEmail());
        if(alreadyRegisteredUser!=null){
            throw new EmailAlreadyExistsException(addNewUserDTO.getEmail());
        }
        User newUser=new User();
        newUser.setEmail(addNewUserDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(addNewUserDTO.getPassword()));

        newUser.setCreatedBy(loggedInUser);
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newUser.setCreatedAt(createdAt.format(createdAtFormatter));

        Role roleToAdd = roleRepository.getByRole(addNewUserDTO.getRole());
        newUser.getRoles().add(roleToAdd);
        Employee employee=new Employee();
        Employee savedEmployee = employeeRepository.save(employee);

        newUser.setEmployee(savedEmployee);
        return userRepository.save(newUser);
    }

    public User userLogin(LoginUserDTO loginUserDTO) throws EntityNotFoundException, WrongPasswordCredentialsException, IllegalAccessRoleException, IllegalArgumentException{

        if (loginUserDTO.getEmail() == null || loginUserDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (loginUserDTO.getPassword() == null || loginUserDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        User loggedInUser = userRepository.getByEmail(loginUserDTO.getEmail());
        if(loggedInUser == null){
            throw new EntityNotFoundException("User with email " + loginUserDTO.getEmail()+" not found");
        }
        Role roleUserWantToLoginWith = roleRepository.getByRole(loginUserDTO.getRole());

        if(!loggedInUser.getRoles().contains(roleUserWantToLoginWith)){
            throw new IllegalAccessRoleException(loginUserDTO.getEmail(), loginUserDTO.getRole());
        }
        else if(passwordEncoder.matches(loginUserDTO.getPassword(), loggedInUser.getPassword())){
            return loggedInUser;
        }
        else{
            throw new WrongPasswordCredentialsException(loggedInUser.getEmail());
        }
    }

    public static class IllegalAccessRoleException extends IllegalAccessException{
        public IllegalAccessRoleException(String email, String role){
            super(email +" does not have the access to "+ role +" role.");
        }
    }

    public void deleteUser(Long userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        userRepository.deleteById(userId);
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

    public static class UserNotFoundException extends RuntimeException{
        public UserNotFoundException(Long id){
            super("User with ID:" +id+" not found!!");
        }
    }


}
