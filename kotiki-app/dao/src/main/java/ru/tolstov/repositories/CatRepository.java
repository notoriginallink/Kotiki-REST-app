package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;
import java.util.function.Consumer;

public interface CatRepository {
    /**
     * registers a new cat in repository
     * @param work operations to initialize a cat
     * @return created cat
     * **/
    Cat registerCat(Consumer<Cat> work);
    Cat deleteCat(Cat cat);
    List<Cat> getAllCats();
    Cat getCatById(long id);
    void updateFriendship(Cat firstCat, Cat secondCat);
}
