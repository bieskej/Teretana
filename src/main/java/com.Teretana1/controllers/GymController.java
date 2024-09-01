package com.Teretana1.controllers;

import com.Teretana1.models.Gym;
import com.Teretana1.models.User;
import com.Teretana1.repositories.GymRepository;
import com.Teretana1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GymController {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/gyms/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAddGymForm(Model model) {
        return "Users/add-gym";
    }


    @PostMapping("/gyms/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addGym(@RequestParam String name, @RequestParam String address) {
        Gym gym = new Gym(name, address);
        gymRepository.save(gym);
        return "redirect:/users";
    }

    @GetMapping("/gyms/{gymId}/manage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manageGym(@PathVariable Long gymId, Model model) {
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new RuntimeException("Gym not found"));
        List<User> availableUsers = userRepository.findUsersNotInGym(gym);
        model.addAttribute("gym", gym);
        model.addAttribute("availableUsers", availableUsers);
        return "Users/manage-gym";
    }

    @PostMapping("/gyms/{gymId}/addUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addUserToGym(@PathVariable Long gymId, @RequestParam Long userId) {
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new RuntimeException("Gym not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setGym(gym);
        userRepository.save(user);
        return "redirect:/gyms/" + gymId + "/manage";
    }

    @PostMapping("/gyms/{gymId}/removeUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String removeUserFromGym(@PathVariable Long gymId, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setGym(null);
        userRepository.save(user);
        return "redirect:/gyms/" + gymId + "/manage";
    }
}
