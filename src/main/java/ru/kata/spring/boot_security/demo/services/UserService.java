package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void initializeData() {
        // Проверяем, существует ли уже пользователь с именем 'admin'
        if (!userRepository.existsByUsername("admin")) {
            // Создаем новую роль, если она еще не существует
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = new Role();
                userRole.setName("ROLE_ADMIN");
                userRole = roleRepository.save(userRole);
            }

            Role userRole2 = roleRepository.findByName("ROLE_ADMIN");
            if (userRole2 == null) {
                userRole2 = new Role();
                userRole2.setName("ROLE_USER");
                userRole2 = roleRepository.save(userRole2);
            }

            // Создаем нового пользователя
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("{bcrypt}$2a$12$chV0.LAVVdAEc5CYq7AsUuJ.7QWD0BADy6J7QKxKEXTOxSeh7Rqau"); // Используйте BCryptPasswordEncoder для шифрования пароля
            adminUser.setAge(30);
            adminUser.setRoles(Collections.singleton(userRole));

            User user = new User("user", "{bcrypt}$2a$12$chV0.LAVVdAEc5CYq7AsUuJ.7QWD0BADy6J7QKxKEXTOxSeh7Rqau", 20, Collections.singleton(userRole2));
            userRepository.save(user);
            System.out.println("пользователь добавлен в базу");
            // Сохраняем пользователя в базу данных
            userRepository.save(adminUser);
            System.out.println("пользователь добавлен в базу");

        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//Тест нового метода
    @Transactional
    public void addNewUser(String role,User saveUser) {
        Role role1 = roleRepository.findByName(role);
        saveUser.getRoles().add(role1);
        userRepository.save(saveUser);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Дополнительные методы для работы с ролями
    public List<Role> getRolesByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRoles)
                .map(roles -> roles.stream().collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void assignRoleToUser(Long userId, Role role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(Long userId, Role role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}