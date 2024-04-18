package ru.tolstov.services;

import ru.tolstov.services.dto.OwnerDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OwnerService {
    /**
     * Creates a new owner and registers it in repository
     *
     * @param firstName owner's first name
     * @param lastName  owner's last name
     * @param birthdate owner's birthdate
     * @return ID of created owner
     **/
    long createOwner(String firstName, String lastName, LocalDate birthdate);
    /**
     * Gets all owners that are currently present in repository
     * @return {@code List} of owners
     * **/
    List<OwnerDto> getAllOwners();
    /**
     * Removes owner from repository. If owner with this ID is not present in repository then nothing happens
     * @param ownerID owner's ID
     **/
    void removeOwner(long ownerID);

    Optional<OwnerDto> getById(long id);
}
