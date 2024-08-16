package com.Teretana1.services;

import com.Teretana1.models.User;
import com.Teretana1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository repository;
    @Override
    public com.Teretana1.models.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repository.findByEmail(username);
        return new com.Teretana1.models.UserDetails(u);
    }

}