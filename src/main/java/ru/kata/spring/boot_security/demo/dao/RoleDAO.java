package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Set;

public interface RoleDAO {
    Set<Role> getAllRoles();
    Role createRole(Role role);
    Role findRoleById(Long id);
    Role findRoleByName(String name);
    Set<Role> findByIds(Set<Long> ids);
}