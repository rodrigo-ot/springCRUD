package com.example.spring.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_PRODUCTS")
public class PersonModel extends RepresentationModel<PersonModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID idPerson;
    private String name;
    private BigDecimal wage;
    private String job;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public UUID getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(UUID idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWage() {
        return this.wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }
}
