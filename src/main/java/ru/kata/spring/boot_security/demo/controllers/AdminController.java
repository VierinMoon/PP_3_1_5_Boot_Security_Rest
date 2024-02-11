package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;


@Controller
@RequestMapping("/admin")
public class AdminController {

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
    public String showCreateUserForm(User user, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "user-create";
    }

    @PostMapping("/user-create")
    public String createUser(@ModelAttribute("user") User user) {
        userServiceImpl.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/user-edit")
    public String showUpdateUserForm(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.showUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles()); // Предполагается, что у вас есть метод getAllRoles
        return "user-edit";
    }

    @PostMapping("/user-edit")
    public String updateUser(@ModelAttribute("user") User user) {
        userServiceImpl.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }
}