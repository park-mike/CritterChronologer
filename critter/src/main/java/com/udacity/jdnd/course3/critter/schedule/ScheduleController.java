package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;


    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) throws Exception {

        List<Employee> employees = new ArrayList<>();
        if (scheduleDTO.getEmployeeIds() != null) {
            for (Long empId : scheduleDTO.getEmployeeIds()) {
                Employee employee = employeeService.getEmployee(empId)
                        .orElseThrow(() -> new Exception("Employee not found: " + empId));
                employees.add(employee);
            }
        }

        List<Pet> pets = new ArrayList<>();
        if (scheduleDTO.getPetIds() != null) {
            for (Long petId : scheduleDTO.getPetIds()) {
                Pet pet = petService.getPetById(petId); //.orElseThrow(() -> new Exception("Pet not found: " + petId));
                pets.add(pet);
            }
        }

        Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities(), employees, pets);

        schedule = scheduleService.saveSchedule(schedule);

        scheduleDTO.setId(schedule.getId());

        return scheduleDTO;
    }


    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        List<Schedule> scheduleList = scheduleService.getAllSchedules();
        return convertScheduleListToDTOs(scheduleList);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {

        List<Schedule> petScheduleList = scheduleService.getScheduleForPet(petId);
        return convertScheduleListToDTOs(petScheduleList);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {

        List<Schedule> employeeScheduleList = scheduleService.getScheduleForEmployee(employeeId);
        return convertScheduleListToDTOs(employeeScheduleList);
    }


    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        //return scheduleService.getScheduleForCustomer(customerId).stream().map(schedule -> convertSchedule2ScheduleDTOs(schedule).collect(Collectors.toList())); }

        List<Schedule> custScheduleList = scheduleService.getScheduleForCustomer(customerId);
        return convertScheduleListToDTOs(custScheduleList);
    }

    /**
     * public ScheduleDTO convertSchedule2ScheduleDTOs(Schedule schedule) {
     * ScheduleDTO scheduleDTO = new ScheduleDTO();
     * List<Long> petIds = new ArrayList<>();
     * List<Long> employeeIds = new ArrayList<>();
     * BeanUtils.copyProperties(schedule, scheduleDTO);
     * for (Pet pet : schedule.getPets()) {
     * petIds.add(pet.getId());
     * }
     * for (Employee employee : schedule.getEmployees()) {
     * employeeIds.add(employee.getId());
     * }
     * scheduleDTO.setPetIds(petIds);
     * scheduleDTO.setEmployeeIds(employeeIds);
     * return scheduleDTO;
     * }
     **/


    private List<ScheduleDTO> convertScheduleListToDTOs(List<Schedule> scheduleList) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        ScheduleDTO scheduleDTO = null;
        for (Schedule schedule : scheduleList) {
            List<Long> petIdList = new ArrayList<>();
            List<Long> employeeIdList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(schedule.getPets())) {
                petIdList = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
                employeeIdList = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
            }

            scheduleDTO = new ScheduleDTO(schedule.getId(), employeeIdList, petIdList, schedule.getDate(),
                    schedule.getActivities());
            scheduleDTOList.add(scheduleDTO);
        }
        return scheduleDTOList;
    }


}
