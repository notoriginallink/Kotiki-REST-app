package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;
import java.util.function.Consumer;

public interface CatRepository {
    long registerCat(Consumer<Cat> work);
    void deleteCat(Cat cat);
    List<Cat> getAllCats();
    Cat getCatById(long id);
    void updateFriendship(Cat firstCat, Cat secondCat);
}
