package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CatService {
    long addCat(String name, Date bithdate, String breed, CatColor color, long ownerID);
    void removeCat(long id);
    List<Cat> getAllCats();
    Optional<Cat> getCatByID(long id);
    boolean areFriends(long firstCatID, long secondCatID);
    void makeFriendship(long firstCatID, long secondCatID);
    void stopFriendship(long firstCatID, long secondCatID);
}
