package ru.tolstov.services;

import ru.tolstov.models.Owner;

import java.util.Date;
import java.util.List;

public interface OwnerService {
    Owner createOwner(String firstName, String lastName, Date birhdate);
    List<Owner> getAllOwners();
    void removeOwner(Owner owner);
}
