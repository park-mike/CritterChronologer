package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetService petServ;
    @Autowired
    private CustomerService customerServ;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return convertPet2PetDTO(petServ.savePet(convertPetDTO2Pet(petDTO), petDTO.getCustomerId()));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPet2PetDTO(petServ.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petServ.getAllPets().stream().map(pet -> convertPet2PetDTO(pet)).collect(Collectors.toList());
    }


    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByCustomer(@PathVariable long customerId) {
        return petServ.getAllPetsByCustomer(customerId).stream().map(pet ->
                convertPet2PetDTO(pet)).collect(Collectors.toList());
    }

    public PetDTO convertPet2PetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setType(pet.getType());
        petDTO.setCustomerId(pet.getCustomer().getId());
        return petDTO;
    }

    public Pet convertPetDTO2Pet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setType(petDTO.getType());
        return pet;
    }
}
