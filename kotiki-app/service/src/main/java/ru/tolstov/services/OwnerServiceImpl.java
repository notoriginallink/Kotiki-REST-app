package ru.tolstov.services;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tolstov.models.Owner;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.OwnerItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional
    public long createOwner(String firstName, String lastName, LocalDate birthdate) {
        var owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setBirthdate(birthdate);
        owner.setCats(new ArrayList<>());

        return ownerRepository.save(owner).getId();
    }

    @Override
    @Transactional
    public List<OwnerItem> getAllOwners() {
        List<OwnerItem> owners = new ArrayList<>();
        for (var owner : ownerRepository.findAll())
            owners.add(new OwnerItem(owner));

        return owners;
    }

    @Override
    @Transactional
    public void removeOwner(long ownerID) {
        var owner = ownerRepository.findById(ownerID);
        if (owner.isEmpty())
            return;

        if (!owner.get().getCats().isEmpty())
            throw new RuntimeException("Cant remove owner while he has cats");

        ownerRepository.delete(owner.get());
    }
}
