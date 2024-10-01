package com.Teretana1.controllers;

import com.Teretana1.models.Gym;
import com.Teretana1.models.Membership;
import com.Teretana1.models.Role;
import com.Teretana1.models.User;
import com.Teretana1.repositories.GymRepository;
import com.Teretana1.repositories.MembershipRepository;
import com.Teretana1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class GymController {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

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
        List<Membership> memberships = membershipRepository.findByGymId(gymId);
        List<User> availableUsers = userRepository.findUsersNotInGymAndNotAdmin(gymId, Role.ADMIN);


        model.addAttribute("gym", gym);
        model.addAttribute("memberships", memberships);
        model.addAttribute("availableUsers", availableUsers);
        return "Users/manage-gym";
    }

    @PostMapping("/gyms/{gymId}/updateMembership")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateMembership(@PathVariable Long gymId,
                                   @RequestParam Long membershipId,
                                   @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam("isActive") Boolean isActive) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setEndDate(endDate);
        membership.setIsActive(isActive);
        membershipRepository.save(membership);

        return "redirect:/gyms/" + gymId + "/manage";
    }

    @PostMapping("/gyms/{gymId}/addUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addUserToGym(
            @PathVariable Long gymId,
            @RequestParam Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("isActive") Boolean isActive,
            Model model) {

        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new RuntimeException("Gym not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Create and save the membership
        Membership membership = new Membership(user, gym, startDate, endDate, isActive);
        membershipRepository.save(membership);

        return "redirect:/gyms/" + gymId + "/manage"; // Redirect back to the management page
    }

    @PostMapping("/gyms/{gymId}/removeUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String removeUserFromGym(@PathVariable Long gymId, @RequestParam Long userId, Model model) {
        // Find the membership linking the user to the gym
        Membership membership = membershipRepository.findByUserIdAndGymId(userId, gymId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        // Remove the membership entry
        membershipRepository.delete(membership);

        // Reload the updated list of memberships and available users
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new RuntimeException("Gym not found"));
        List<Membership> memberships = membershipRepository.findByGymId(gymId);
        model.addAttribute("gym", gym);
        model.addAttribute("memberships", memberships);
        model.addAttribute("availableUsers", userRepository.findUsersNotInGymAndNotAdmin(gymId, Role.ADMIN));

        // Return the view directly to stay on the same page
        return "Users/manage-gym";
    }
}
