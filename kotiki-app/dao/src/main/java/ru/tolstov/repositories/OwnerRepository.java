package ru.tolstov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tolstov.models.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
