package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestUserRepository {
    @Autowired
    private UserRepository userRepository;
    @Test
    @Transactional

    public void testSaveUser() {


        Employee employee = new Employee();

        Role role = new Role();
        role.setRole("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now().toString());
        user.setCreatedBy(null);
        user.setEmployee(employee);
        user.setRoles(roles);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("password");
    }
}
