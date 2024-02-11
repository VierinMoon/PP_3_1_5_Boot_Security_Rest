package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleService;

    @Autowired
    public DatabaseInitializer(UserServiceImpl userServiceImpl, RoleServiceImpl roleService) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        roleService.createRole(new Role("ROLE_ADMIN"));
        roleService.createRole(new Role("ROLE_USER"));
        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
        Set<Role> allRoles = new HashSet<>();
        adminRole.add(roleService.showUserById(1L));
        userRole.add(roleService.showUserById(2L));
        allRoles.add(roleService.showUserById(1L));
        allRoles.add(roleService.showUserById(2L));
        userServiceImpl.save(new User("admin", "100", 1, adminRole));
        userServiceImpl.save(new User("user", "150", 1, userRole));
        userServiceImpl.save(new User("superAdmin", "SuperNinja", 1, allRoles));

    }
}