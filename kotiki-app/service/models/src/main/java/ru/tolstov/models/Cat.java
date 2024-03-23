package ru.tolstov.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "cats")
public class Cat {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "birthdate")
    private Date birtdate;
    @Column(name = "breed")
    private String breed;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private CatColor color;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
//    private List<Cat> friends;
}
