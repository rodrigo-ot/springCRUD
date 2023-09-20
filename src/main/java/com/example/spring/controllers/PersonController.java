package com.example.spring.controllers;

import com.example.spring.dtos.PersonRecordDto;
import com.example.spring.models.PersonModel;
import com.example.spring.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/people")
    public ResponseEntity<PersonModel> savePerson(@RequestBody @Valid PersonRecordDto personRecordDto) {
        var personModel = new PersonModel();
        BeanUtils.copyProperties(personRecordDto, personModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.savePerson(personModel));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/people")
    public ResponseEntity<List<PersonModel>> getAllPeople() {
        List<PersonModel> peopleList = personService.findAll();
        if (!peopleList.isEmpty()) {
            for (PersonModel person : peopleList) {
                UUID id = person.getIdPerson();
                person.add(linkTo(methodOn(PersonController.class).getOnePerson(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(peopleList);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/people/{id}")
    public ResponseEntity<Object> getOnePerson(@PathVariable(value = "id") UUID id) {
        Optional<PersonModel> personModelOptional = personService.findById(id);
        if (personModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
        personModelOptional.get().add(linkTo(methodOn(PersonController.class).getAllPeople()).withRel("People List"));
        return ResponseEntity.status(HttpStatus.OK).body(personModelOptional.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/people/{id}")
    public ResponseEntity<Object> updatePerson(@PathVariable(value = "id") UUID id,
                                               @RequestBody @Valid PersonRecordDto personRecordDto) {
        Optional<PersonModel> personModelOptional = personService.findById(id);
        if (personModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }

        var personModel = personModelOptional.get();
        BeanUtils.copyProperties(personRecordDto, personModel);
        return ResponseEntity.status(HttpStatus.OK).body(personService.savePerson(personModel));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/people/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable(value = "id") UUID id) {
        Optional<PersonModel> personModelOptional = personService.findById(id);
        if (personModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
        personService.deletePerson(personModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person deleted");
    }





}