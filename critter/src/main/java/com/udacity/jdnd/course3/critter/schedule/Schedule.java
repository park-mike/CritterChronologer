package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.LAZY)
    private final List<Employee> employees;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "schedule_pet",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private final List<Pet> pets;

    private final LocalDate date;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private final Set<EmployeeSkill> activities;

    public Schedule(LocalDate date, Set<EmployeeSkill> activities, List<Employee> employees, List<Pet> pets) {
        this.date = date;
        this.activities = activities;
        this.employees = employees;
        this.pets = pets;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Employee> getEmployees() {
        return employees;
    }


    public List<Pet> getPets() {
        return pets;
    }


    public LocalDate getDate() {
        return date;
    }


    public Set<EmployeeSkill> getActivities() {
        return activities;
    }


}
