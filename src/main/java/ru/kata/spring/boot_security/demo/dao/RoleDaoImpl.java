package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
@Repository
public class RoleDaoImpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        entityManager.persist(role);
        return role;
    }

    @Override
    @Transactional
    public Role findRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }
}
