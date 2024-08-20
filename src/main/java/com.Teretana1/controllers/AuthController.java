package com.Teretana1.controllers;

import com.Teretana1.models.User;
import com.Teretana1.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    final
    UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/auth/register")
    public String add (Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "Users/register";
    }

    @PostMapping("/auth/register")
    public String newUser (@Valid User user, BindingResult result, Model model){
        boolean errors = result.hasErrors();
        if (errors){
            model.addAttribute("user", user);
            return "Users/register";
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordEncoded = encoder.encode(user.getLozinka());
            user.setLozinka(passwordEncoded);
            user.setPotvrdaLozinke(passwordEncoded);
            userRepo.save(user);
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/auth/login")
    public String login (Model model){
        model.addAttribute("user", new User());
        return "Users/index";
    }
}
