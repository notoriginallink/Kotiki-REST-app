package ru.tolstov.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.tolstov.entities.Cat;
import ru.tolstov.entities.Owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CatRepositoryImpl implements CatRepositoryCustom {
    private EntityManager entityManager;
    @Override
    public List<Cat> findFiltered(String color, String breed, Integer year, Long ownerId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cat> query = builder.createQuery(Cat.class);

        Root<Cat> cat = query.from(Cat.class);
        Join<Owner, Cat> catsOwner = cat.join("owner");
        List<Predicate> predicates = new ArrayList<>();

        if (color != null)
            predicates.add(builder.equal(cat.get("color"), color));

        if (breed != null)
            predicates.add(builder.equal(cat.get("breed"), breed));

        if (year != null)
            predicates.add(builder.between(
                    cat.get("birthdate"),
                    LocalDate.of(year, 1, 1),
                    LocalDate.of(year + 1, 1, 1)));

        if (ownerId != null)
            predicates.add(builder.equal(catsOwner.get("id"), ownerId));

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
