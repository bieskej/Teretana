package com.Teretana1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Exercise {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Exercise name is required")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @NotNull(message = "Sets are required")
    private Integer sets;

    @NotNull(message = "Repetitions are required")
    private Integer repetitions;

    // Default constructor
    public Exercise() {}

    // Parameterized constructor
    public Exercise(String name, WorkoutPlan workoutPlan, Integer sets, Integer repetitions) {
        this.name = name;
        this.workoutPlan = workoutPlan;
        this.sets = sets;
        this.repetitions = repetitions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }
}