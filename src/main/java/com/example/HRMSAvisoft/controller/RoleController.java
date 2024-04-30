package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.RoleDTO;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RoleService roleService;
    @PostMapping("/addRole")
    public ResponseEntity<RoleDTO> saveUser(@RequestBody RoleDTO roleDTO ) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role roleAdded =roleService.addRole(role);
        RoleDTO addedRoleDTO = modelMapper.map(roleAdded,RoleDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRoleDTO);
    }
}
