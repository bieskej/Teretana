package com.Teretana1.controllers;

import com.Teretana1.models.User;
import com.Teretana1.services.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/korisnik")
@PreAuthorize("hasAuthority('KORISNIK')")
public class KorisnikController {

    @Autowired
    private KorisnikService korisnikService;

    @GetMapping
    public String korisnikDashboard(Model model) {
        // Get the current user
        User currentUser = korisnikService.getCurrentUser();

        // Add user information to the model
        model.addAttribute("user", currentUser);

        // Add workout plans to the model
    korisnikService.getWorkoutPlansForUser(currentUser).forEach(workoutPlan -> System.out.println("AA " + workoutPlan.getExercises().toString()));
    model.addAttribute("workoutPlans", korisnikService.getWorkoutPlansForUser(currentUser));

        // Add gym information to the model
        model.addAttribute("gym", korisnikService.getGymForUser(currentUser));

        return "Korisnik/dashboard";
    }
}