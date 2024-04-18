package ru.tolstov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tolstov.models.Cat;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
