package ru.rail.FirstSecurity.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rail.FirstSecurity.entity.User;

import ru.rail.FirstSecurity.service.UserService;


@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/users";
    }

    @GetMapping("/{id}")
    public String user(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Проверяем, имеет ли пользователь доступ к странице
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        if (!authentication.getName().equals(user.getUsername()) && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to access this page");
        }
        model.addAttribute("user", user);
        return "users/user";
    }

    @PostMapping
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User savedUser = userService.saveUser(user);
        return "redirect:/users/" + savedUser.getId();
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

}

