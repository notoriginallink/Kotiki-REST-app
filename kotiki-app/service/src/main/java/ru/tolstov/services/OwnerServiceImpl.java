package ru.tolstov.services;

import ru.tolstov.models.Owner;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.OwnerItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerServiceImpl extends ServiceBase implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public long createOwner(String firstName, String lastName, LocalDate birthdate) {
        var owner = new Owner();

        inTransaction(entityManager -> {
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setBirthdate(birthdate);
            owner.setCats(new ArrayList<>());

            ownerRepository.setEntityManager(entityManager);
            owner.setId(ownerRepository.registerOwner(owner));
        });

        return owner.getId();
    }

    @Override
    public List<OwnerItem> getAllOwners() {
        List<OwnerItem> owners = new ArrayList<>();

        inTransaction(entityManager -> {
            ownerRepository.setEntityManager(entityManager);
            for (var owner : ownerRepository.getAllOwners()) {
                owners.add(new OwnerItem(owner));
            }
        });

        return owners;
    }

    @Override
    public void removeOwner(long ownerID) {
        inTransaction(entityManager -> {
            ownerRepository.setEntityManager(entityManager);

            var owner = ownerRepository.getOwnerById(ownerID);
            if (owner == null)
                return;

            if (!owner.getCats().isEmpty())
                throw new RuntimeException("Cant remove owner while he has cats");

            ownerRepository.deleteOwner(owner);
        });
    }
}
