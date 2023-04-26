package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepo;
import com.udacity.jdnd.course3.critter.repository.PetRepo;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class CustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    private PetRepo petRepo;

    public Customer saveCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public List<Customer> getAllCustomers() {

        return customerRepo.findAll();
    }

    public Customer findByPetID(long petId) {
        Pet pet = petRepo.findById(petId).orElseThrow(() -> new RuntimeException("pet id: " + petId));
        return pet.getCustomer();
    }
}
