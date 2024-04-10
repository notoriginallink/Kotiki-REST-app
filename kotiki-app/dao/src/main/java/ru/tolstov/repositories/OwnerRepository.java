package ru.tolstov.repositories;

import jakarta.persistence.EntityManager;
import ru.tolstov.models.Owner;

import java.util.List;

public interface OwnerRepository extends Repository {
    long registerOwner(Owner owner);
    List<Owner> getAllOwners();
    Owner getOwnerById(long id);
    void deleteOwner(Owner owner);
}
