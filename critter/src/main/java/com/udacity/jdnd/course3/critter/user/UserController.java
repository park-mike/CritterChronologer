package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PetService petService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        customer = customerService.saveCustomer(customer);
        customerDTO.setId(customer.getId());

        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (Customer customer : customerList) {
            customerDTOList.add(convertCustomer2CustomerDTO(customer));
        }
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getCustomerByPet(@PathVariable long petId) {
        return convertCustomer2CustomerDTO(customerService.findByPetID(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return convertEmployee2EmployeeDTO(employeeService.saveEmployee(convertEmployeeDTO2Employee(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) throws Exception {
        Employee employee = employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new Exception("Employee not found for id " + employeeId));
        EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getDaysAvailable(),
                employee.getSkills());
        return employeeDTO;
    }


    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId)
            throws Exception {

        Employee employee = employeeService.getEmployee(employeeId)
                .orElseThrow(() -> new Exception("Employee not found for id " + employeeId));
        employee.setDaysAvailable(daysAvailable);
        employeeService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {

        List<Employee> availableEmployeeList = employeeService
                .findAvailableEmployees(employeeRequestDTO.getDate().getDayOfWeek(), employeeRequestDTO.getSkills());
        List<EmployeeDTO> availableEmployeeDTOs = new ArrayList<>();
        EmployeeDTO employeeDTO = null;
        if (!CollectionUtils.isEmpty(availableEmployeeList)) {
            for (Employee employee : availableEmployeeList) {
                employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getDaysAvailable(),
                        employee.getSkills());
                availableEmployeeDTOs.add(employeeDTO);
            }
        }

        return availableEmployeeDTOs;
    }

    public CustomerDTO convertCustomer2CustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        List<Pet> petList = customer.getPets();
        List<Long> petIds = new ArrayList<>();
        BeanUtils.copyProperties(customer, customerDTO);
        for (Pet pet : petList) {
            petIds.add(pet.getId());
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;

    }

    public Customer convertCustomerDTO2Customer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public EmployeeDTO convertEmployee2EmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public Employee convertEmployeeDTO2Employee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
