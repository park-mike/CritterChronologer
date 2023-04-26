package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepo;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//
@Transactional
@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepo scheduleRepo;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepo.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }


    public List<Schedule> getScheduleForEmployee(long employeeId) {

        List<Schedule> employeeScheduleList = new ArrayList<>();

        List<Schedule> ScheduleList = getAllSchedules();

        if (!CollectionUtils.isEmpty(ScheduleList)) {
            for (Schedule schedule : ScheduleList) {

                if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
                    for (Employee emp : schedule.getEmployees()) {

                        if (employeeId == emp.getId()) {
                            employeeScheduleList.add(schedule);
                            break;
                        }
                    }
                }
            }
        }
        return employeeScheduleList;
    }


    public List<Schedule> getScheduleForPet(long petId) {
        List<Schedule> ScheduleList = getAllSchedules();
        List<Schedule> petScheduleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ScheduleList)) {
            for (Schedule s : ScheduleList) {

                if (!CollectionUtils.isEmpty(s.getPets())) {
                    for (Pet pet : s.getPets()) {

                        if (petId == pet.getId()) {
                            petScheduleList.add(s);
                            break;
                        }
                    }
                }
            }
        }
        return petScheduleList;
    }

    //customer
    public List<Schedule> getScheduleForCustomer(long customerId) {

        List<Schedule> ScheduleList = getAllSchedules();
        List<Schedule> custScheduleList = new ArrayList<>();


        if (!CollectionUtils.isEmpty(ScheduleList)) {
            for (Schedule s : ScheduleList) {

                if (!CollectionUtils.isEmpty(s.getPets())) {

                    List<Long> customerIds = s.getPets().stream().map(pet -> pet.getCustomer().getId())
                            .collect(Collectors.toList());
                    if (customerIds.contains(customerId)) {
                        custScheduleList.add(s);
                    }
                }
            }
        }
        return custScheduleList;
    }

}
