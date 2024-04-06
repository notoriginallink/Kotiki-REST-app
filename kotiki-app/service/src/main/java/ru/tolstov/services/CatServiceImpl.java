package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.dto.CatItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class CatServiceImpl extends ServiceBase implements CatService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    public CatServiceImpl(CatRepository catRepository, OwnerRepository ownerRepository) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public long addCat(String name, LocalDate bithdate, String breed, CatColor color, long ownerID) {
        var cat = new Cat();

        inTransaction(entityManager -> {
            ownerRepository.setEntityManager(entityManager);

            var owner = ownerRepository.getOwnerById(ownerID);
            if (owner == null)
                throw new UnknownEntityIdException("Owner with ID=%s does not exist".formatted(ownerID));

            cat.setName(name);
            cat.setBirthdate(bithdate);
            cat.setBreed(breed);
            cat.setColor(color);
            cat.setOwner(owner);
            cat.setFriends(new HashSet<>());

            cat.setId(catRepository.registerCat(cat));
        });

        return cat.getId();
    }

    @Override
    public void removeCat(long id) {
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);

            var cat = catRepository.getCatById(id);
            if (cat != null)
                catRepository.deleteCat(cat);
        });
    }

    @Override
    public List<CatItem> getAllCats() {
        List<CatItem> cats = new ArrayList<>();

        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            for (var cat : catRepository.getAllCats()) {
                if (cat == null)
                    cats.add(null);
                else
                    cats.add(new CatItem(cat));
            }
        });

        return cats;
    }

    @Override
    public Optional<CatItem> getCatByID(long id) {
        List<CatItem> cats = new ArrayList<>();
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var cat = catRepository.getCatById(id);
            if (cat != null)
                cats.add(new CatItem(cat));
        });

        return cats.stream().findFirst();
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public boolean areFriends(long firstCatID, long secondCatID) {
        return inTransactionBool(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var firstCat = catRepository.getCatById(firstCatID);
            var secondCat = catRepository.getCatById(secondCatID);

            if (firstCat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(firstCat));

            if (secondCat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(secondCat));

            return firstCat.getFriends().contains(secondCat);
        });
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public void makeFriendship(long firstCatID, long secondCatID) {
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var firstCat = catRepository.getCatById(firstCatID);
            var secondCat = catRepository.getCatById(secondCatID);

            if (firstCat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(firstCat));

            if (secondCat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(secondCat));

            catRepository.updateFriendship(firstCat, secondCat);
        });
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public void destroyFriendship(long firstCatID, long secondCatID) {
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var firstCat = catRepository.getCatById(firstCatID);
            var secondCat = catRepository.getCatById(secondCatID);

            if (firstCat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(firstCat));

            if (secondCat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(secondCat));

            catRepository.updateFriendship(firstCat, secondCat);
        });
    }

    @Override
    public List<CatItem> getFriends(long id) {
        List<CatItem> friends = new ArrayList<>();
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var cat = catRepository.getCatById(id);

            if (cat == null)
                throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(cat));

            for (var friend : cat.getFriends())
                friends.add(new CatItem(friend));
        });

        return friends;
    }
}
