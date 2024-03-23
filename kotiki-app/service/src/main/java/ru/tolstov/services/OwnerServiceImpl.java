package ru.tolstov.services;

import ru.tolstov.models.Owner;
import ru.tolstov.repositories.OwnerRepository;

import java.util.Date;
import java.util.List;

public class OwnerServiceImpl implements OwnerService {
    private OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner createOwner(String firstName, String lastName, Date birhdate) {
        var owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setBirthdate(birhdate);
        ownerRepository.registerOwner(owner);

        return owner;
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.getAllOwners();
    }

    @Override
    public void removeOwner(Owner owner) {
        ownerRepository.deleteOwner(owner);
    }
}
