package ru.tolstov.repositories;

import ru.tolstov.models.Owner;

import java.util.List;
import java.util.function.Consumer;

public interface OwnerRepository {
    long registerOwner(Consumer<Owner> work);
    List<Owner> getAllOwners();
    Owner getOwnerById(long id);
    void deleteOwner(Owner owner);
}
