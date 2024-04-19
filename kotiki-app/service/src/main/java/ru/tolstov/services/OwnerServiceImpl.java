package ru.tolstov.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tolstov.models.Owner;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.CatDto;
import ru.tolstov.services.dto.OwnerDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<OwnerDto> getAllOwners() {
        List<OwnerDto> owners = new ArrayList<>();
        for (var owner : ownerRepository.findAll())
            owners.add(new OwnerDto(owner));

        return owners;
    }

    @Override
    @Transactional
    public void removeOwner(long ownerID) throws RuntimeException {
        var owner = ownerRepository.findById(ownerID);
        if (owner.isEmpty())
            return;

        if (!owner.get().getCats().isEmpty())
            throw new RuntimeException("Cant remove owner while he has cats");

        ownerRepository.delete(owner.get());
    }

    @Override
    public Optional<OwnerDto> getById(long id) {
        var owner = ownerRepository.findById(id);
        if (owner.isEmpty())
            return Optional.empty();

        return Optional.of(new OwnerDto(owner.get()));
    }

    @Override
    public List<CatDto> getAllCats(long id) throws UnknownEntityIdException {
        var owner = ownerRepository.findById(id);
        if (owner.isEmpty())
            throw new UnknownEntityIdException("Owner with ID=%s not found");

        return owner.get().getCats().stream().map(CatDto::new).toList();
    }
}
