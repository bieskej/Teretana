package com.Teretana1.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*var authorities = authentication.getAuthorities();

        for (var authority : authorities) {
            System.out.println("this is  " + authority.getAuthority());
        }

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            response.sendRedirect("/users");
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("TRENER"))) {
            response.sendRedirect("/trener");
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("KORISNIK"))) {
            response.sendRedirect("/korisnik");
        } else {
            response.sendRedirect("/login");
        }
        */
    }
}
