package com.Teretana1.controllers;


import com.Teretana1.models.Membership;
import com.Teretana1.models.User;
import com.Teretana1.repositories.GymRepository;
import com.Teretana1.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GymRepository gymRepository;

    public UserRestController(UserRepository userRepository, GymRepository gymRepository) {
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
    }

    @GetMapping("/users321")
    public List<UserRestDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserRestDto> usersResponseList = new ArrayList<>();

        for (User user : users) {
            UserRestDto userRestDto = new UserRestDto(user.getIme(), user.getPrezime(), user.getLozinka(), null);
            usersResponseList.add(userRestDto);
        }

       // return users.stream().map(dbUser -> new UserRestDto(dbUser.getIme(), dbUser.getPrezime(), dbUser.getLozinka(), List.of())).toList();

        return usersResponseList;
    }

    @PostMapping("/users123/create")
    public Long createUser(UserRestDto userRestDto) {
        var dbUser = new User();
        dbUser.setIme(userRestDto.name());
        dbUser.setPrezime(userRestDto.lastName());
        dbUser.setLozinka(userRestDto.password());
        dbUser.setEmail("Aaaaaaaaaaaaaaaaa");
        return userRepository.save(dbUser).getId();
    }

// curl -X POST -d {"name":"Bosko","lastName":"Raguz","password":"$2a$10$3BLAQ5/.8aj6L/8rK8MsfOovujEiV9wfs5doH7P9pfJh55oLIMHlC"} http://localhost:8080/users/create

    @GetMapping("/users123")
    public List<UserRestDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserRestDto> usersResponseList = new ArrayList<>();

        // userId - lista membersipa
        Map<Long, List<Membership>> userIdMembershipMap = userRepository.getMembershipsByUserIds(users.stream().map(User::getId).toList()).stream().collect(Collectors.groupingBy(membersip -> membersip.getUser().getId()));

        for (User user : users) {
            List<GymRestDto> gymRestDtos = new ArrayList<>();
            if(userIdMembershipMap.containsKey(user.getId())) {
                var dbGyms = userIdMembershipMap.get(user.getId()).stream().map(Membership::getGym).toList();
                dbGyms.forEach(dbGym ->
                        gymRestDtos.add(new GymRestDto(dbGym.getId(), dbGym.getName(), dbGym.getAddress())));
            }
            UserRestDto userRestDto = new UserRestDto(user.getIme(), user.getPrezime(), user.getLozinka(), gymRestDtos);

            usersResponseList.add(userRestDto);
        }

        return usersResponseList;
    }

    @GetMapping("/users/{userId}/gyms")
    public List<GymRestDto> getGymForAUser(@PathVariable Long userId) {
        // validacija
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(String.format("user %d does not exist!", userId));
        }

        var userMemberships = userRepository.getMembershipsByUserIds(List.of(userId));

        if(userMemberships == null || userMemberships.isEmpty()) {
            return List.of();
        }
        var userGyms = userMemberships.stream().map(Membership::getGym).toList();
        if(userGyms.isEmpty()) {
            return List.of();
        }

        /*
        List<GymRestDto> gymRestDtos = new ArrayList<>();
        for (var dbUserGym : userGyms) {
            var gymRestDto = new GymRestDto(dbUserGym.getId(), dbUserGym.getName(), dbUserGym.getAddress());
            gymRestDtos.add(gymRestDto);
        }
        return gymRestDtos;
        */

        return userGyms.stream().map(dbUserGym -> new GymRestDto(dbUserGym.getId(), dbUserGym.getName(), dbUserGym.getAddress())).toList();
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record UserRestDto(String name, String lastName, String password, List<GymRestDto> gyms) {
    }

    public record GymRestDto(long id, String name, String address) {}

}
