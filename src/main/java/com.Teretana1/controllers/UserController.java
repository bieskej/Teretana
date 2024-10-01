package com.Teretana1.controllers;


import com.Teretana1.models.Gym;
import com.Teretana1.models.User;
import com.Teretana1.repositories.GymRepository;
import com.Teretana1.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GymRepository gymRepository;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        List<Gym> gyms = gymRepository.findAll(); // Assuming you have a GymRepository
        model.addAttribute("gyms", gyms);

        return "Users/index";
    }
    // U klasi UserController

    // server side rendering sa thymeleafom
    @GetMapping("/users/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "Users/add";
    }

    @PostMapping("/users/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "Users/add";
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordEncoded = encoder.encode(user.getLozinka());
            user.setLozinka(passwordEncoded);
            user.setPotvrdaLozinke(passwordEncoded);
            userRepository.save(user);
            return "redirect:/users";
        }
    }

    @PostMapping("/users/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
        return "redirect:/users";
    }

    // U klasi UserController

    @GetMapping("/users/edit/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditUserForm(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Neispravan ID korisnika: " + userId));
        model.addAttribute("user", user);
        return "Users/edit";
    }

    @PatchMapping("/users/edit/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(@PathVariable Long userId, @ModelAttribute User user, Model model) {
        // Provjerite postoji li korisnik s tim ID-om
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Neispravan ID korisnika: " + userId));
        existingUser.setIme(user.getIme());
        existingUser.setPrezime(user.getPrezime());
        existingUser.setEmail(user.getEmail());
        // Lozinka
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String lozinka = encoder.encode(user.getLozinka());
        existingUser.setLozinka(lozinka);
        existingUser.setPotvrdaLozinke(lozinka);
        existingUser.setRoles(user.getRoles());
        // Postavite ostala polja po potrebi
        userRepository.save(existingUser);
        return "redirect:/users";
    }
}
