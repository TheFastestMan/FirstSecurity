package ru.rail.FirstSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")

public class RegistrationController {
    @GetMapping
    public String users(Model model) {
        return "users/registration";
    }
}
