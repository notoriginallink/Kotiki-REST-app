package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;

public class LocalCatRepository extends LocalRepository implements CatRepository {
    @Override
    public long registerCat(Cat cat) {
        entityManager.persist(cat);

        return cat.getId();
    }

    @Override
    public void deleteCat(Cat cat) {
        entityManager.remove(cat);
    }

    @Override
    public List<Cat> getAllCats() {
        return entityManager
                .createQuery("FROM Cat")
                .getResultList();
    }

    @Override
    public Cat getCatById(long id) {
        return entityManager
                .find(Cat.class, id);
    }

    @Override
    public void updateFriendship(Cat firstCat, Cat secondCat) {
        entityManager.merge(firstCat);
        entityManager.merge(secondCat);
    }
}
