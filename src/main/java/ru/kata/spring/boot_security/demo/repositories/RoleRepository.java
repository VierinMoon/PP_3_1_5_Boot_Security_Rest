package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Здесь могут быть дополнительные запросы, если они необходимы
    List<User> findUsersByName(String name);
    Role findByName(String name);
}