package com.Teretana1.repositories;
import com.Teretana1.models.Gym;
import com.Teretana1.models.Membership;
import com.Teretana1.models.Role;
import com.Teretana1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);



    @Query("""
           SELECT u FROM User u 
           WHERE :gymId NOT IN (
                SELECT m.gym.id FROM Membership m WHERE m.user.id = u.id
           )
           AND :role NOT MEMBER OF u.roles
           """)
    List<User> findUsersNotInGymAndNotAdmin(@Param("gymId") Long gymId, @Param("role") Role role);


    @Query("""
    SELECT m.gym FROM Membership m
    WHERE m.user.id = :userId
""")
    Gym getUsersGym(@Param("userId") Long userId);

    @Query("SELECT m FROM Membership m WHERE m.user.id IN :userIds")
    List<Membership> getMembershipsByUserIds(List<Long> userIds);
}