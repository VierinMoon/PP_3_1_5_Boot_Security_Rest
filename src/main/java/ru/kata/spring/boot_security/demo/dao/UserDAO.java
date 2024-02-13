package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserDAO {
    User findByUsername(String username);
    List<User> getAllUsers();
    User findUserById(Long id);
    void save(User user);
    void deleteUser(Long id);
    void updateUser(User user);
}