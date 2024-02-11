package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    List<User> getAllUsers() ;

    User showUserById(Long id);

    void save(User user);

    void deleteUser(Long id);
}
