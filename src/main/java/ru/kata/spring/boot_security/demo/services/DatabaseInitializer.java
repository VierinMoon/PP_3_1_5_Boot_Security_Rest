package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Autowired
    public DatabaseInitializer(UserService userService, RoleService roleService, RoleRepository roleRepository, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            System.out.println("User: " + user.getUsername());
            for (Role role : user.getRoles()) {
                System.out.println("Role: " + role.getName());
            }
        }
    }

    @Override
    @Transactional
    public void run(String... args) {

        roleRepository.saveAll(List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
//        userService.addNewUser("ROLE_USER",new User("user", "userUser", 66, "user" ));
//        userService.addNewUser("ROLE_ADMIN",new User("admin", "adminAdmin", 45, "admin" ));
        userService.initializeData();


        System.out.println(roleService.getAllRoles());
        System.out.println(userService.getAllUsers());



//        roleService.createRole(new Role("ROLE_ADMIN"));
//        roleService.createRole(new Role("ROLE_USER"));
//        Set<Role> adminRole = new HashSet<>();
//        Set<Role> userRole = new HashSet<>();
//        Set<Role> allRoles = new HashSet<>();
//        adminRole.add(roleService.get(1L));
//        userRole.add(roleService.showUserById(2L));
//        allRoles.add(roleService.showUserById(1L));
//        allRoles.add(roleService.showUserById(2L));
//        userService.save(new User("Mike", "Pyke", "pyke@mail.ru", "Pyke", "admin"));
//        userService.save(new User("Piter", "Lombok", "lombok@mail.ru","Lombok", "user"));
//        userService.save(new User("Kate", "password", 10,"admin"));
//        roleRepository.saveAll(List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
//        userService.addNewUser("ROLE_USER",new User("user", "userUser", 66, "user" ));
//        userService.addNewUser("ROLE_ADMIN",new User("admin", "adminAdmin", 45, "admin" ));
    }
}