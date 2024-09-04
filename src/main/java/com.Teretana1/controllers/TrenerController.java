package com.Teretana1.controllers;

import com.Teretana1.models.*;
import com.Teretana1.repositories.ExerciseRepository;
import com.Teretana1.repositories.MembershipRepository;
import com.Teretana1.repositories.UserRepository;
import com.Teretana1.repositories.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/trener")
@PreAuthorize("hasAuthority('TRENER')")
public class TrenerController {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping()
    public String viewGymMembers( Model model) {
        // Find the coach's user ID based on the logged-in principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User coach = userRepository.findByEmail(email);


        // Assuming each coach is associated with only one gym:
        Membership membership = membershipRepository.findByUserIdAndRole(coach.getId(), Role.TRENER)
                .orElseThrow(() -> new RuntimeException("Coach is not associated with any gym"));

        Long gymId = membership.getGym().getId();

        // Get all users in the gym where the coach is assigned
        List<User> gymMembers = membershipRepository.findUsersByGymAndCoach(gymId, coach.getId());

        // Add users to the model
        model.addAttribute("gymMembers", gymMembers);
        model.addAttribute("gymId", gymId);

        return "Trener/dashboard";
    }

    @GetMapping("/gym/{gymId}/assignWorkoutPlan")
    public String assignWorkoutPlan(@RequestParam Long memberId, @PathVariable Long gymId, Model model) {
        // Load the member and workout plan details
        User member = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User coach = userRepository.findByEmail(email);

        // Load the available workout plans (could filter by coach or gym)
        List<WorkoutPlan> availablePlans = workoutPlanRepository.findByCreatorId(coach.getId());

        model.addAttribute("member", member);
        model.addAttribute("availablePlans", availablePlans);
        model.addAttribute("gymId", gymId);

        return "Trener/assign-workout-plan";
    }

    @PostMapping("/gym/{gymId}/assignWorkoutPlan")
    @PreAuthorize("hasAuthority('TRENER')")
    public String assignWorkoutPlan(
            @PathVariable Long gymId,
            @RequestParam Long memberId,
            @RequestParam Long workoutPlanId,
            Model model) {

        // Find the workout plan and the member
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new RuntimeException("Workout Plan not found"));
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Assign the workout plan to the member
        workoutPlan.setUser(member);
        workoutPlanRepository.save(workoutPlan);

        // Redirect back to the gym members page
        return "redirect:/trener";
    }

    @GetMapping("/gym/{gymId}/createWorkoutPlan")
    @PreAuthorize("hasAuthority('TRENER')")
    public String showCreateWorkoutPlanForm(@PathVariable Long gymId, Model model) {
        model.addAttribute("gymId", gymId);
        return "Trener/create-workout-plan"; // Return the Thymeleaf template for creating a workout plan
    }

    @PostMapping("/gym/{gymId}/createWorkoutPlan")
    @PreAuthorize("hasAuthority('TRENER')")
    public String createWorkoutPlan(
            @PathVariable Long gymId,
            @RequestParam String planName,
            @ModelAttribute("exercises") ArrayList<Exercise> exercises) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User coach = userRepository.findByEmail(email);
        // Find the logged-in coach (creator of the workout plan)

        // Create the WorkoutPlan
        WorkoutPlan workoutPlan = new WorkoutPlan(planName, coach);
        workoutPlan = workoutPlanRepository.save(workoutPlan);

        // Loop through the list of exercises and assign them to the workout plan
        for (Exercise exercise : exercises) {
            exercise.setWorkoutPlan(workoutPlan); // Associate each exercise with the new workout plan
            exerciseRepository.save(exercise); // Save the exercise to the database
        }

        // Redirect to the gym members page or another appropriate page
        return "redirect:/trener";
    }
}
