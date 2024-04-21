package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;

public interface CatRepositoryCustom {
    List<Cat> findFiltered(String color, String breed, Integer year);
}
