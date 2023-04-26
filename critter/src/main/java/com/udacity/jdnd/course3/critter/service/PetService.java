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
public class PetService {

    @Autowired
    private PetRepo petRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public Pet savePet(Pet pet, Long id) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Pet id not created"));
        pet.setCustomer(customer);
        Pet savePet = petRepo.save(pet);
        customer.addPet(savePet);
        customerRepo.save(customer);
        return savePet;


    }

    public List<Pet> getAllPets() {
        return petRepo.findAll();
    }

    public Pet getPetById(long petId) {
        return petRepo.findById(petId).orElseThrow(() -> new RuntimeException("Invalid pet Id"));
    }


    public List<Pet> getAllPetsByCustomer(long customerId) {
        return petRepo.findByCustomerId(customerId);
    }


}
