package ru.tolstov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.time.LocalDate;
import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findByColor(CatColor color);
    List<Cat> findByBreedIgnoreCase(String breed);
    @Query(value = "SELECT * FROM kotiki.cats WHERE EXTRACT(YEAR FROM birthdate) = ?1", nativeQuery = true)
    List<Cat> findByBirthYear(int year);

    // Experimental method
    @Query(value = """
            SELECT * FROM kotiki.cats
            WHERE (?1 IS NULL OR color = ?1)
            AND (?2 IS NULL OR breed = ?2)
            AND (?3 is null OR extract(YEAR FROM birthdate) = ?3)
            """, nativeQuery = true)
    List<Cat> findFiltered(String color, String breed, Integer year);
}
