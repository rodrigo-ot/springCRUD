package com.example.spring.controllers;

import com.example.spring.dtos.PersonRecordDto;
import com.example.spring.models.PersonModel;
import com.example.spring.repositories.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @PostMapping("/people")
    public ResponseEntity<PersonModel> savePerson(@RequestBody @Valid PersonRecordDto personRecordDto) {
        var personModel = new PersonModel();
        BeanUtils.copyProperties(personRecordDto, personModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(personRepository.save(personModel));
    }

    @GetMapping("/people")
    public ResponseEntity<List<PersonModel>> getAllPeople() {
        List<PersonModel> peopleList = personRepository.findAll();
        if(!peopleList.isEmpty()){
            for(PersonModel person : peopleList){
                UUID id = person.getIdPerson();
                person.add(linkTo(methodOn(PersonController.class).getOnePerson(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(peopleList);
    }

    @GetMapping("/people/{id}")
    public ResponseEntity<Object> getOnePerson(@PathVariable(value = "id") UUID id) {
        Optional<PersonModel> person0 = personRepository.findById(id);
        if (person0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
        person0.get().add(linkTo(methodOn(PersonController.class).getAllPeople()).withRel("People List"));
        return ResponseEntity.status(HttpStatus.OK).body(person0.get());
    }

    @PutMapping("/people/{id}")
    public ResponseEntity<Object> updatePerson(@PathVariable(value="id") UUID id,
                                                @RequestBody @Valid PersonRecordDto personRecordDto) {
        Optional<PersonModel> person0 = personRepository.findById(id);
        if (person0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
        var personModel = person0.get();
        BeanUtils.copyProperties(personRecordDto, personModel);
        return ResponseEntity.status(HttpStatus.OK).body(personRepository.save(personModel));
    }


    @DeleteMapping("/people/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable(value="id") UUID id){
        Optional<PersonModel> person0 = personRepository.findById(id);
        if (person0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
        personRepository.delete(person0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person deleted");
    }





}