package ru.tolstov.services;

import ru.tolstov.models.Owner;

import java.util.Date;
import java.util.List;

public interface OwnerService {
    /**
     * Creates a new owner and registers it in repository
     * @param firstName owner's first name
     * @param lastName owner's last name
     * @param birhdate owner's birthdate
     * @return ID of created owner
     * **/
    long createOwner(String firstName, String lastName, Date birhdate);
    /**
     * Gets all owners that are currently present in repository
     * @return {@code List} of owners
     * **/
    List<Owner> getAllOwners();
    /**
     * Removes owner from repository. If owner with this ID is not present in repository then nothing happens
     * @param ownerID owner's ID
     **/
    void removeOwner(long ownerID);
}
