package ru.tolstov.repositories;

import ru.tolstov.models.Owner;

import java.util.List;

public interface OwnerRepository {
    long registerOwner(Owner owner);
    List<Owner> getAllOwners();
    Owner getOwnerById(long id);
    void deleteOwner(Owner owner);
}
