package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String REDIRECT_TO_ADMIN = "redirect:/admin";
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleService;


    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleService) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String listUsers(Model model, Principal principal) {
        model.addAttribute("users", userServiceImpl.getAllUsers());

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        if (principal != null) {
            String username = principal.getName();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                model.addAttribute("username", username);
                model.addAttribute("roles", roles);
            }
        }
        return "user-list";
    }

    @GetMapping("/user-create")
    public String showCreateUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "user-list";
    }

    @PostMapping("/user-create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds, Model model) {
        // Получаем роли по идентификаторам
        Set<Role> roles = roleService.findByIds(roleIds);

        // Устанавливаем роли для пользователя
        user.setRoles(roles);

        // Сохраняем пользователя
        userServiceImpl.save(user);

        // Перенаправляем пользователя обратно в админ-панель
        return REDIRECT_TO_ADMIN;
    }

    @GetMapping("/user-edit")
    public String showUserEditForm(@RequestParam("id") Long id, Model model) {
        User user = userServiceImpl.showUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles()); // Добавляем все роли в модель
        return "user-edit"; // Возвращаем имя представления
    }

    @PostMapping("/user-edit")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds, Model model) {
        Set<Role> roles = roleService.findByIds(roleIds);
        user.setRoles(roles);
        userServiceImpl.updateUser(user);
        return "redirect:/admin"; // Перенаправляем пользователя обратно на страницу администратора
    }

    //new method

    //----------
    @GetMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userServiceImpl.deleteUser(id);
        return REDIRECT_TO_ADMIN;
    }

//    @GetMapping(value = "/{id}/delete_user")
//    public String deleteUserById(@PathVariable Long id) {
//        userServiceImpl.deleteUser(id);
//        return REDIRECT_TO_ADMIN;
//    }

}