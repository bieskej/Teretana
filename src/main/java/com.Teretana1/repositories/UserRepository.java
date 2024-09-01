package com.Teretana1.repositories;
import com.Teretana1.models.Gym;
import com.Teretana1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.gym IS NULL OR u.gym <> :gym")
    List<User> findUsersNotInGym(@Param("gym") Gym gym);
}
