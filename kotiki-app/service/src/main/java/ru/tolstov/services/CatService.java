package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.util.Date;
import java.util.List;

public interface CatService {
    // TODO add friends and owner
    Cat addCat(String name, Date bithdate, String breed, CatColor color);
    void removeCat(Cat cat);
    List<Cat> getAllCats();
}
