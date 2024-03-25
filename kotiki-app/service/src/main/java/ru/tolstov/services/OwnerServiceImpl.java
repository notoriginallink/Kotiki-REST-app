package ru.tolstov.services;

import ru.tolstov.models.Owner;
import ru.tolstov.repositories.OwnerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public long createOwner(String firstName, String lastName, LocalDate birthdate) {
        var owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setBirthdate(birthdate);
        owner.setCats(new ArrayList<>());

        return ownerRepository.registerOwner(owner);
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.getAllOwners();
    }

    @Override
    public void removeOwner(long ownerID) {
        var owner = ownerRepository.getOwnerById(ownerID);
        if (owner == null)
            return;

        if (!owner.getCats().isEmpty())
            throw new RuntimeException("Cant remove owner while he has cats");

        ownerRepository.deleteOwner(owner);
    }
}
