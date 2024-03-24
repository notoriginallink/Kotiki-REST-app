package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;

import java.lang.annotation.Inherited;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    public CatServiceImpl(CatRepository catRepository, OwnerRepository ownerRepository) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public long addCat(String name, Date bithdate, String breed, CatColor color, long ownerID) {
        var owner = ownerRepository.getOwnerById(ownerID);
        if (owner == null)
            throw new UnknownEntityIdException("Owner with ID=%s does not exist".formatted(ownerID));

        return catRepository.registerCat(cat -> {
            cat.setName(name);
            cat.setBirtdate(bithdate);
            cat.setBreed(breed);
            cat.setColor(color);
            cat.setOwner(owner);
            cat.setFriends(new HashSet<>());
        });
    }

    @Override
    public void removeCat(long id) {
        var cat = catRepository.getCatById(id);
        if (cat != null)
            catRepository.deleteCat(cat);
    }

    @Override
    public List<Cat> getAllCats() {
        return catRepository.getAllCats();
    }

    @Override
    public Optional<Cat> getCatByID(long id) {
        return Optional.ofNullable(catRepository.getCatById(id));
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public boolean areFriends(long firstCatID, long secondCatID) {
        Cat firstCat = checkCatPresent(firstCatID);
        Cat secondCat = checkCatPresent(secondCatID);

        return firstCat.getFriends().contains(secondCat);
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public void makeFriendship(long firstCatID, long secondCatID) {
        Cat firstCat = checkCatPresent(firstCatID);
        Cat secondCat = checkCatPresent(secondCatID);

        if (firstCat.getFriends().contains(secondCat))
            return;

        firstCat.getFriends().add(secondCat);
        secondCat.getFriends().add(firstCat);

        catRepository.updateFriendship(firstCat, secondCat);
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public void destroyFriendship(long firstCatID, long secondCatID) {
        Cat firstCat = checkCatPresent(firstCatID);
        Cat secondCat = checkCatPresent(secondCatID);

        firstCat.getFriends().remove(secondCat);
        secondCat.getFriends().remove(firstCat);

        catRepository.updateFriendship(firstCat, secondCat);
    }

    private Cat checkCatPresent(long id) {
        var cat = catRepository.getCatById(id);
        if (cat == null)
            throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(id));

        return cat;
    }
}
