package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;

public interface CatRepository {
    long registerCat(Cat cat);
    void deleteCat(Cat cat);
    List<Cat> getAllCats();
    Cat getCatById(long id);
    void updateFriendship(Cat firstCat, Cat secondCat);
}
