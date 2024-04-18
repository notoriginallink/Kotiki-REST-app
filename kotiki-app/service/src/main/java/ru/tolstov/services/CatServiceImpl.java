package ru.tolstov.services;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.CatItem;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class CatServiceImpl implements CatService {
    @Autowired
    private final CatRepository catRepository;
    @Autowired
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional
    public long addCat(String name, LocalDate bithdate, String breed, CatColor color, long ownerID) {
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
    public void removeCat(long id) {
        var cat = catRepository.findById(id);
        cat.ifPresent(catRepository::delete);
    }

    @Override
    @Transactional
    public List<CatItem> getAllCats() {
        List<CatItem> cats = new ArrayList<>();
        for (var cat : catRepository.findAll()) {
            if (cat == null)
                cats.add(null); // TODO why?
            else
                cats.add(new CatItem(cat));
        }

        return cats;
    }

    @Override
    @Transactional
    public Optional<CatItem> getCatByID(long id) {
        var cat = catRepository.findById(id);

        if (cat.isEmpty())
            return Optional.empty();
        else
            return Optional.of(new CatItem(cat.get()));
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    @Transactional
    public boolean areFriends(long firstCatID, long secondCatID) {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        return firstCat.getFriends().contains(secondCat);
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    @Transactional
    public void makeFriendship(long firstCatID, long secondCatID) {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        if (firstCat.getFriends().contains(secondCat))
            return;

        firstCat.getFriends().add(secondCat);
        secondCat.getFriends().add(firstCat);

        catRepository.saveAll(Arrays.asList(firstCat, secondCat));
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    @Transactional
    public void destroyFriendship(long firstCatID, long secondCatID) {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        if (!firstCat.getFriends().contains(secondCat))
            return;

        firstCat.getFriends().remove(secondCat);
        secondCat.getFriends().remove(firstCat);

        catRepository.saveAll(Arrays.asList(firstCat, secondCat));
    }

    @Override
    @Transactional
    public List<CatItem> getFriends(long id) {
        var cat = checkCatPersistence(catRepository, id);

        List<CatItem> friends = new ArrayList<>();
        for (var friend : cat.getFriends())
            friends.add(new CatItem(friend));

        return friends;
    }

    private Cat checkCatPersistence(CatRepository repository, long id) {
        var cat = repository.findById(id);
        if (cat.isEmpty())
            throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(id));

        return cat.get();
    }
}
