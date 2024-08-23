package com.Teretana1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/korisnik")
@PreAuthorize("hasAuthority('KORISNIK')")
public class KorisnikController {

    @GetMapping
    public String korisnikDashboard(Model model) {
        // Dodajte potrebne atribute u model
        return "Korisnik/dashboard";
    }


}
