package ru.tolstov.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tolstov.models.Owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OwnerItem {
    String firstName;
    String lastName;
    LocalDate birthdate;
    List<Long> catIds;

    public OwnerItem(Owner owner) {
        this.firstName = owner.getFirstName();
        this.lastName = owner.getLastName();
        this.birthdate = owner.getBirthdate();
        catIds = new ArrayList<>();
        for (var cat : owner.getCats())
            catIds.add(cat.getId());
    }
}
