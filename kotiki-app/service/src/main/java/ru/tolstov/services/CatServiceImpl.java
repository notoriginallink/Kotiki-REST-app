package ru.tolstov.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.CatDto;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional
    public long addCat(String name, LocalDate bithdate, String breed, CatColor color, long ownerID) throws UnknownEntityIdException {
        var owner = ownerRepository.findById(ownerID);
        if (owner.isEmpty())
            throw new UnknownEntityIdException("Owner with ID=%s does not exist".formatted(ownerID));

        var cat = new Cat();
        cat.setName(name);
        cat.setBirthdate(bithdate);
        cat.setBreed(breed);
        cat.setColor(color);
        cat.setOwner(owner.get());
        cat.setFriends(new HashSet<>());

        return catRepository.save(cat).getId();
    }

    @Override
    @Transactional
    public boolean removeCat(long id) {
        var cat = catRepository.findById(id);
        if (cat.isEmpty())
            return false;

        catRepository.delete(cat.get());
        return true;
    }

    @Override
    @Transactional
    public List<CatDto> getAllCats() {
        return catRepository.findAll().stream().map(CatDto::new).toList();
    }

    @Override
    @Transactional
    public Optional<CatDto> getCatByID(long id) {
        var cat = catRepository.findById(id);

        if (cat.isEmpty())
            return Optional.empty();
        else
            return Optional.of(new CatDto(cat.get()));
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    @Transactional
    public boolean areFriends(long firstCatID, long secondCatID) throws UnknownEntityIdException {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        return firstCat.getFriends().contains(secondCat);
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    @Transactional
    public void makeFriendship(long firstCatID, long secondCatID) throws UnknownEntityIdException {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        if (firstCat.getFriends().contains(secondCat))
            throw new FriendshipException("Cats %s and %s are already friends".formatted(firstCatID, secondCatID));

        firstCat.getFriends().add(secondCat);
        secondCat.getFriends().add(firstCat);

        catRepository.saveAll(Arrays.asList(firstCat, secondCat));
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    @Transactional
    public void destroyFriendship(long firstCatID, long secondCatID) throws UnknownEntityIdException {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        if (!firstCat.getFriends().contains(secondCat))
            throw new FriendshipException("Cats %s and %s are not friends".formatted(firstCatID, secondCatID));

        firstCat.getFriends().remove(secondCat);
        secondCat.getFriends().remove(firstCat);

        catRepository.saveAll(Arrays.asList(firstCat, secondCat));
    }

    @Override
    @Transactional
    public List<CatDto> getFriends(long id) throws UnknownEntityIdException {
        var cat = checkCatPersistence(catRepository, id);

        List<CatDto> friends = new ArrayList<>();
        for (var friend : cat.getFriends())
            friends.add(new CatDto(friend));

        return friends;
    }

    public List<CatDto> findFiltered(CatColor color, String breed, Integer year) {
        String color_string = (color == null ? null : color.toString());
        return catRepository.findFiltered(color_string, breed, year).stream().map(CatDto::new).toList();
    }

    private Cat checkCatPersistence(CatRepository repository, long id) {
        var cat = repository.findById(id);
        if (cat.isEmpty())
            throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(id));

        return cat.get();
    }
}
