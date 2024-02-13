package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.*;



@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final String REDIRECT_TO_ADMIN = "redirect:/admin";
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleService;


    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleService) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String listUsers(Model model) {
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "user-list";
    }

    @GetMapping("/user-create")
    public String showCreateUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "user-create";
    }

    @PostMapping("/user-create")
    public String processCreateUserForm(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds, Model model) {
        // Получите список объектов Role по ID
        Set<Role> roles = roleService.findByIds(roleIds);
        user.setRoles(roles);
        // Сохраните пользователя и перенаправьте на страницу успеха
        userServiceImpl.save(user);
        return REDIRECT_TO_ADMIN;
    }
    @GetMapping("/user-edit")
    public String showUpdateUserForm(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.showUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "user-edit";
    }

    @PostMapping("/user-edit")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds, Model model) {
        // Получите список объектов Role по ID
        Set<Role> roles = roleService.findByIds(roleIds);
        user.setRoles(roles);
        // Обновите пользователя и сохраните изменения
        userServiceImpl.updateUser(user);
        return REDIRECT_TO_ADMIN;
    }

    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userServiceImpl.deleteUser(id);
        return REDIRECT_TO_ADMIN;
    }
}