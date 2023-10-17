package com.example.spring.services;

import com.example.spring.dtos.PersonRecordDto;
import com.example.spring.models.PersonModel;
import com.example.spring.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePerson() {
        PersonRecordDto personRecordDto = new PersonRecordDto("Bob", new BigDecimal("50000.00"), "Engineer");

        PersonModel personModel = new PersonModel();
        BeanUtils.copyProperties(personRecordDto, personModel);
        personModel.setIdPerson(UUID.randomUUID());

        when(personRepository.save(any(PersonModel.class))).thenReturn(personModel);

        PersonModel savedPerson = personService.savePerson(personModel);

        assertThat(savedPerson).isNotNull();

        verify(personRepository, times(1)).save(any(PersonModel.class));
    }


    @Test
    public void testFindAll() {
        List<PersonModel> peopleList = new ArrayList<>();

        PersonRecordDto personRecordDto = new PersonRecordDto("Bob", new BigDecimal("50000.00"), "Engineer");
        PersonRecordDto personRecordDto1 = new PersonRecordDto("Anne", new BigDecimal("1200.00"), "Actress");

        PersonModel personModel = new PersonModel();
        BeanUtils.copyProperties(personRecordDto, personModel);
        personModel.setIdPerson(UUID.randomUUID());
        peopleList.add(personModel);

        BeanUtils.copyProperties(personRecordDto1, personModel);
        personModel.setIdPerson(UUID.randomUUID());
        peopleList.add(personModel);


        when(personRepository.findAll()).thenReturn(peopleList);

        List<PersonModel> foundPeople = personService.findAll();

        assertThat(foundPeople).isNotEmpty();

        verify(personRepository, times(1)).findAll();
    }


    @Test
    public void testFindById() {
        UUID personId = UUID.randomUUID();

        PersonRecordDto personRecordDto = new PersonRecordDto("Bob", new BigDecimal("50000.00"), "Engineer");
        PersonModel personModel = new PersonModel();
        BeanUtils.copyProperties(personRecordDto, personModel);
        personModel.setIdPerson(UUID.randomUUID());

        when(personRepository.findById(eq(personId))).thenReturn(Optional.of(personModel));

        Optional<PersonModel> foundPersonOptional = personService.findById(personId);

        assertThat(foundPersonOptional).isNotEmpty();

        verify(personRepository, times(1)).findById(eq(personId));
    }
}
