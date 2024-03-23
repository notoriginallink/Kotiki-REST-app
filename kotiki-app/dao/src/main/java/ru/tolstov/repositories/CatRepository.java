package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;

public interface CatRepository {
    void registerCat(Cat cat);
    void deleteCat(Cat cat);
    List<Cat> getAllCats();
}
