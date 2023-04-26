package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepo;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    public Employee saveEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public Optional<Employee> getEmployee(Long employeeId) {
        return employeeRepo.findById(employeeId);
    }

    public List<Employee> findAvailableEmployees(DayOfWeek dayOfWeek, Set<EmployeeSkill> skillSet) {


        List<Employee> employeeList = getAllEmployees();
        List<Employee> availEmployeeList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(employeeList)) {
            for (Employee employee : employeeList) {
                if (employee.getDaysAvailable().contains(dayOfWeek) && employee.getSkills().containsAll(skillSet)) {
                    availEmployeeList.add(employee);
                }
            }
        }
        return availEmployeeList;
    }

}
