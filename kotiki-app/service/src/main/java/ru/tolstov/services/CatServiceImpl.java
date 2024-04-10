package ru.tolstov.services;

import jakarta.persistence.EntityManagerFactory;
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

    public CatServiceImpl(EntityManagerFactory entityManagerFactory, CatRepository catRepository, OwnerRepository ownerRepository) {
        super(entityManagerFactory);
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public long addCat(String name, LocalDate bithdate, String breed, CatColor color, long ownerID) {
        return inTransaction(entityManager -> {
            ownerRepository.setEntityManager(entityManager);

            var owner = ownerRepository.getOwnerById(ownerID);
            if (owner == null)
                throw new UnknownEntityIdException("Owner with ID=%s does not exist".formatted(ownerID));

            var cat = new Cat();
            cat.setName(name);
            cat.setBirthdate(bithdate);
            cat.setBreed(breed);
            cat.setColor(color);
            cat.setOwner(owner);
            cat.setFriends(new HashSet<>());

            return catRepository.registerCat(cat);
        });
    }

    @Override
    public void removeCat(long id) {
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);

            var cat = catRepository.getCatById(id);
            if (cat != null)
                catRepository.deleteCat(cat);

            return null;
        });
    }

    @Override
    public List<CatItem> getAllCats() {
        return inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);

            List<CatItem> cats = new ArrayList<>();
            for (var cat : catRepository.getAllCats()) {
                if (cat == null)
                    cats.add(null);
                else
                    cats.add(new CatItem(cat));
            }

            return cats;
        });
    }

    @Override
    public Optional<CatItem> getCatByID(long id) {
        return inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var cat = catRepository.getCatById(id);

            if (cat == null)
                return Optional.empty();
            else
                return Optional.of(new CatItem(cat));
        });
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public boolean areFriends(long firstCatID, long secondCatID) {
        return inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var firstCat = checkCatPersistence(catRepository, firstCatID);
            var secondCat = checkCatPersistence(catRepository, secondCatID);

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
            var firstCat = checkCatPersistence(catRepository, firstCatID);
            var secondCat = checkCatPersistence(catRepository, secondCatID);

            if (firstCat.getFriends().contains(secondCat))
                return null;

            firstCat.getFriends().add(secondCat);
            secondCat.getFriends().add(firstCat);

            catRepository.updateFriendship(firstCat, secondCat);

            return null;
        });
    }

    /**
     * @throws UnknownEntityIdException if cats with given IDs are not present in repository
     * **/
    @Override
    public void destroyFriendship(long firstCatID, long secondCatID) {
        inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var firstCat = checkCatPersistence(catRepository, firstCatID);
            var secondCat = checkCatPersistence(catRepository, secondCatID);

            if (!firstCat.getFriends().contains(secondCat))
                return null;

            firstCat.getFriends().remove(secondCat);
            secondCat.getFriends().remove(firstCat);

            catRepository.updateFriendship(firstCat, secondCat);

            return null;
        });
    }

    @Override
    public List<CatItem> getFriends(long id) {
        return inTransaction(entityManager -> {
            catRepository.setEntityManager(entityManager);
            var cat = checkCatPersistence(catRepository, id);

            List<CatItem> friends = new ArrayList<>();
            for (var friend : cat.getFriends())
                friends.add(new CatItem(friend));

            return friends;
        });
    }

    private Cat checkCatPersistence(CatRepository repository, long id) {
        var cat = repository.getCatById(id);
        if (cat == null)
            throw new UnknownEntityIdException("Cat with ID=%s does not exist".formatted(id));

        return cat;
    }
}
