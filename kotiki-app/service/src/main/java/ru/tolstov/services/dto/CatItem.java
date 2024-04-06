package ru.tolstov.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CatItem {
    String name;
    LocalDate birthdate;
    String breed;
    CatColor color;
    Long owner;

    public CatItem(Cat cat) {
        this.name = cat.getName();
        this.birthdate = cat.getBirthdate();
        this.breed = cat.getBreed();
        this.color = cat.getColor();
        this.owner = cat.getOwner().getId();
    }
}
