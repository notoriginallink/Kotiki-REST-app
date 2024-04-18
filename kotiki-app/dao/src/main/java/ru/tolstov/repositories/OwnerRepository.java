package ru.tolstov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tolstov.models.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
