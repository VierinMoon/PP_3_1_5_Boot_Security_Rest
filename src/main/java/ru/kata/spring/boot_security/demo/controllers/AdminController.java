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

//    @GetMapping("/user-edit")
//    public String showUpdateUserForm(@RequestParam("id") Long id, Model model) {
//        User user = userServiceImpl.showUserById(id);
//        model.addAttribute("user", user);
//        model.addAttribute("roles", roleService.getAllRoles());
//        return "user-edit"; // Возвращаем страницу с формой обновления пользователя
//    }
//    @GetMapping("/user-edit")
//    public ResponseEntity<String> showUpdateUserForm(@RequestParam("id") Long id) {
//        User user = userServiceImpl.showUserById(id);
//        String formHtml = generateUserEditFormHtml(user); // Метод для генерации HTML-формы
//        return ResponseEntity.ok(formHtml);
//    }
//    private String generateUserEditFormHtml(User user) {
//        StringBuilder formHtml = new StringBuilder();
//        formHtml.append("<form id=\"userEditForm\" action=\"/admin/user-edit\" method=\"post\">");
//        formHtml.append("<input type=\"hidden\" name=\"id\" value=\"").append(user.getId()).append("\">");
//        formHtml.append("<div class=\"form-group\">");
//        formHtml.append("<label for=\"username\">Username:</label>");
//        formHtml.append("<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" value=\"").append(user.getUsername()).append("\">");
//        formHtml.append("</div>");
//        // Добавьте другие поля формы, такие как пароль, возраст и роли
//        formHtml.append("<div class=\"form-group\">");
//        formHtml.append("<label for=\"password\">Password:</label>");
//        formHtml.append("<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" value=\"").append(user.getPassword()).append("\">");
//        formHtml.append("</div>");
//        formHtml.append("<div class=\"form-group\">");
//        formHtml.append("<label for=\"age\">Age:</label>");
//        formHtml.append("<input type=\"number\" class=\"form-control\" id=\"age\" name=\"age\" value=\"").append(user.getAge()).append("\">");
//        formHtml.append("</div>");
//        formHtml.append("<div class=\"form-group\">");
//        formHtml.append("<label for=\"roles\">Roles:</label>");
//        formHtml.append("<select multiple class=\"form-control\" id=\"roles\" name=\"roles\">");
//        // Добавьте опции для ролей
//        for (Role role : roleService.getAllRoles()) {
//            formHtml.append("<option value=\"").append(role.getId()).append("\"");
//            if (user.getRoles().contains(role)) {
//                formHtml.append(" selected");
//            }
//            formHtml.append(">").append(role.getName()).append("</option>");
//        }
//        formHtml.append("</select>");
//        formHtml.append("</div>");
//        formHtml.append("<button type=\"submit\" class=\"btn btn-success\">Update User</button>");
//        formHtml.append("</form>");
//        return formHtml.toString();
//    }
//
//    @PostMapping("/user-edit")
//    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds, Model model) {
//        Set<Role> roles = roleService.findByIds(roleIds);
//        user.setRoles(roles);
//        userServiceImpl.updateUser(user);
//        return REDIRECT_TO_ADMIN; // Перенаправляем пользователя обратно на страницу администратора
//    }
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
    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userServiceImpl.deleteUser(id);
        return REDIRECT_TO_ADMIN;
    }
}