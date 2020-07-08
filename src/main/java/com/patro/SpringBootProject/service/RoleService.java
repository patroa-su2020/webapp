package com.patro.SpringBootProject.service;

import com.patro.SpringBootProject.dao.RoleRepository;
import com.patro.SpringBootProject.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public String getAllRole() {
        return roleRepository.findAll().toString();
    }
}
