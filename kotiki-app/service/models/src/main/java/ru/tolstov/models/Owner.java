package ru.tolstov.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "owners")
public class Owner {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthdate")
    private Date birthdate;

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Cat> cats;
}
