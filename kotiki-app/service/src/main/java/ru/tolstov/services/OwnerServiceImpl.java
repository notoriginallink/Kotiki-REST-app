package ru.tolstov.services;

import jakarta.persistence.EntityManagerFactory;
import ru.tolstov.models.Owner;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.OwnerItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerServiceImpl extends ServiceBase implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(EntityManagerFactory entityManagerFactory, OwnerRepository ownerRepository) {
        super(entityManagerFactory);
        this.ownerRepository = ownerRepository;
    }

    @Override
    public long createOwner(String firstName, String lastName, LocalDate birthdate) {
        return inTransaction(entityManager -> {
            var owner = new Owner();
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setBirthdate(birthdate);
            owner.setCats(new ArrayList<>());

            ownerRepository.setEntityManager(entityManager);

            return ownerRepository.registerOwner(owner);
        });
    }

    @Override
    public List<OwnerItem> getAllOwners() {
        return inTransaction(entityManager -> {
            ownerRepository.setEntityManager(entityManager);
            List<OwnerItem> owners = new ArrayList<>();
            for (var owner : ownerRepository.getAllOwners())
                owners.add(new OwnerItem(owner));

            return owners;
        });
    }

    @Override
    public void removeOwner(long ownerID) {
        inTransaction(entityManager -> {
            ownerRepository.setEntityManager(entityManager);

            var owner = ownerRepository.getOwnerById(ownerID);
            if (owner == null)
                return null;

            if (!owner.getCats().isEmpty())
                throw new RuntimeException("Cant remove owner while he has cats");

            ownerRepository.deleteOwner(owner);

            return null;
        });
    }
}
