package ru.tolstov.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.tolstov.entities.Owner;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.CatDto;
import ru.tolstov.services.dto.OwnerDto;
import ru.tolstov.services.exceptions.UnknownEntityIdException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OwnerDto> getAllOwners() {
        List<OwnerDto> owners = new ArrayList<>();
        for (var owner : ownerRepository.findAll())
            owners.add(new OwnerDto(owner));

        return owners;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean removeOwner(Long ownerID) throws RuntimeException {
        var owner = ownerRepository.findById(ownerID);
        if (owner.isEmpty())
            return false;

        if (!owner.get().getCats().isEmpty())
            throw new RuntimeException("Cant remove owner while he has cats");

        ownerRepository.delete(owner.get());
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.isCurrentOwner(authentication, #id.longValue())")
    public Optional<OwnerDto> getById(Long id) {
        var ownerOptional = ownerRepository.findById(id);
        if (ownerOptional.isEmpty())
            return Optional.empty();
        var ownerDto = new OwnerDto(ownerOptional.get());

        return Optional.of(ownerDto);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CatDto> getAllCats(Long id) throws UnknownEntityIdException {
        var owner = ownerRepository.findById(id);
        if (owner.isEmpty())
            throw new UnknownEntityIdException("Owner with ID=%s not found");

        return owner.get().getCats().stream().map(CatDto::new).toList();
    }
}
