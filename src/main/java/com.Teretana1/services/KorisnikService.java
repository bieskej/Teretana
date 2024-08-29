package com.Teretana1.services;

import com.Teretana1.models.User;
import com.Teretana1.models.WorkoutPlan;
import com.Teretana1.models.Gym;
import com.Teretana1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KorisnikService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Assuming email is used as username
        return userRepository.findByEmail(email);
    }

    public List<WorkoutPlan> getWorkoutPlansForUser(User user) {
        return user.getWorkoutPlans().stream().toList();
    }

    public Gym getGymForUser(User user) {
        return user.getGym();
    }
}