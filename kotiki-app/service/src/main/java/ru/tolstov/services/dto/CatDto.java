package ru.tolstov.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CatDto {
    String name;
    LocalDate birthdate;
    String breed;
    CatColor color;
    Long owner;

    public CatDto(Cat cat) {
        if (cat != null) {
            this.name = cat.getName();
            this.birthdate = cat.getBirthdate();
            this.breed = cat.getBreed();
            this.color = cat.getColor();
            this.owner = cat.getOwner().getId();
        }
    }
}
