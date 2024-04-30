package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    public Role addRole(Role role)
    {
        return roleRepository.save(role);
    }
}
