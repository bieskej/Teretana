package com.Teretana1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
@PreAuthorize("hasAuthority('TEACHER')")
public class TrenerController {

    @GetMapping
    public String teacherDashboard(Model model) {
        // Dodajte potrebne atribute u model
        return "teacher/dashboard";
    }

    // Ostale metode specifične za nastavnike
}
