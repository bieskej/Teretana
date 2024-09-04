package com.Teretana1.repositories;

import com.Teretana1.models.Membership;
import com.Teretana1.models.Role;
import com.Teretana1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findByGymId(Long gymId);

    Optional<Membership> findByUserIdAndGymId(Long userId, Long gymId);


    @Query("""
           SELECT m.user FROM Membership m
           WHERE m.gym.id = :gymId
           AND :coachId IN (
                SELECT u.id FROM User u JOIN u.roles r WHERE r = 'TRENER'
           )
           """)
    List<User> findUsersByGymAndCoach(@Param("gymId") Long gymId, @Param("coachId") Long coachId);

    @Query("""
           SELECT m FROM Membership m
           WHERE m.user.id = :userId AND :role MEMBER OF m.user.roles
           """)
    Optional<Membership> findByUserIdAndRole(@Param("userId") Long userId, @Param("role") Role role);
}
