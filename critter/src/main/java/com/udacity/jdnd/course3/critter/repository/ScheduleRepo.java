package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

}
