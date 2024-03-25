package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CatService {
    /**
     * Creates a new cat, assigns it to owner and registers it in repository
     * @param name cat's name
     * @param bithdate cat's birthdate
     * @param breed cat's breed
     * @param color cat's color
     * @param ownerID id of the cat's owner
     * @return ID of created cat
     * **/
    long addCat(String name, LocalDate bithdate, String breed, CatColor color, long ownerID);
    /**
     * Removes the cat from repository. If cat with this ID is not present in repository, then nothing happens
     * @param id ID of a cat to be removed
     * **/
    void removeCat(long id);
    /**
     * Gets all cats that are currently present in the repository
     * @return List of cats
     * **/
    List<Cat> getAllCats();
    /**
     * Finds in repository cat with given ID
     * @param id cat's ID
     * @return an Optional describing the cat with given ID, or empty Optional if cat with this ID is not present
     * **/
    Optional<Cat> getCatByID(long id);
    /**
     * Checks if two cats have a friendship. Doesn't matter in which order IDs are given
     * @param firstCatID ID of first cat
     * @param secondCatID ID of second cat
     * @return true if cats are friends or false if they are not
     * **/
    boolean areFriends(long firstCatID, long secondCatID);
    /**
     * Creates friendship between two cats. If cats are already friends then nothing happens
     * @param firstCatID ID of first cat
     * @param secondCatID ID of second cat
     * **/
    void makeFriendship(long firstCatID, long secondCatID);
    /**
     * Destroys friendship between two cats. If cats are not friends then nothing happens
     * @param firstCatID ID of first cat
     * @param secondCatID ID of second cat
     * **/
    void destroyFriendship(long firstCatID, long secondCatID);
    /**
     * returns List containing all friend of cat with provided ID
     * @param id cat's ID
     * @return {@code List} with cats
     * **/
    List<Cat> getFriends(long id);
}
