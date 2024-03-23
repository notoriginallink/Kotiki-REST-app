package ru.tolstov.services;

import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.CatRepository;

import java.util.Date;
import java.util.List;

public class CatServiceImpl implements CatService {
    private CatRepository catRepository;

    public CatServiceImpl(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Override
    public Cat addCat(String name, Date bithdate, String breed, CatColor color) {
        var cat = new Cat();
        cat.setName(name);
        cat.setBirtdate(bithdate);
        cat.setBreed(breed);
        cat.setColor(color);
        catRepository.registerCat(cat);

        return cat;
    }

    @Override
    public void removeCat(Cat cat) {
        catRepository.deleteCat(cat);
    }

    @Override
    public List<Cat> getAllCats() {
        return catRepository.getAllCats();
    }
}
