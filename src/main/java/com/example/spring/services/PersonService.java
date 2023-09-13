package com.example.spring.services;

import com.example.spring.models.PersonModel;
import com.example.spring.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonModel savePerson(PersonModel personModel) {
        return personRepository.save(personModel);
    }

    public List<PersonModel> findAll() {
        return personRepository.findAll();
    }

    public Optional<PersonModel> findById(UUID id) {
        return personRepository.findById(id);
    }

    public void deletePerson(PersonModel personModel) {
        personRepository.delete(personModel);
    }
}
