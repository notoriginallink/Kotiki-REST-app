package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;

import java.util.Date;
import java.util.List;

public class CatServiceImpl implements CatService {
    @Override
    public Cat addCat(String name, Date bithdate, String breed, CatColor color) {
        return null;
    }

    @Override
    public void removeCat(Cat cat) {

    }

    @Override
    public List<Cat> getAllCats() {
        return null;
    }
}
