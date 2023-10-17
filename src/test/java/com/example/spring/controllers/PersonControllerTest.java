package com.example.spring.controllers;

import com.example.spring.dtos.PersonRecordDto;
import com.example.spring.models.PersonModel;
import com.example.spring.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    @WithMockUser(roles = "USER")
    public void testSavePerson() throws Exception {
        PersonRecordDto personDto = new PersonRecordDto("John Doe", new BigDecimal("50000.00"), "Engineer");
        PersonModel personModel = new PersonModel();

        when(personService.savePerson(personModel)).thenReturn(personModel);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllPeople() throws Exception {
        List<PersonModel> people = new ArrayList<>();

        when(personService.findAll()).thenReturn(people);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/people")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetOnePerson() throws Exception {
        UUID personId = UUID.randomUUID();
        PersonModel personModel = new PersonModel();

        when(personService.findById(personId)).thenReturn(Optional.of(personModel));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/people/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdatePerson() throws Exception {
        UUID personId = UUID.randomUUID();
        PersonRecordDto personDto = new PersonRecordDto("John Doe", new BigDecimal("50000.00"), "Engineer");
        PersonModel personModel = new PersonModel();

        when(personService.findById(personId)).thenReturn(Optional.of(personModel));
        when(personService.savePerson(personModel)).thenReturn(personModel);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/people/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeletePerson() throws Exception {
        UUID personId = UUID.randomUUID();
        PersonModel personModel = new PersonModel();

        when(personService.findById(personId)).thenReturn(Optional.of(personModel));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/people/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
