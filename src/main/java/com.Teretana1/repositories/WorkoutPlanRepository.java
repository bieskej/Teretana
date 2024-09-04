package com.Teretana1.repositories;

import com.Teretana1.models.WorkoutPlan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanRepository  extends JpaRepository<WorkoutPlan, Long> {


    List<WorkoutPlan> findByCreatorId(Long id);
}
