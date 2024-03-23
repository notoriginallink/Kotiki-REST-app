package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;

public class LocalCatRepository extends LocalRepository implements CatRepository {
    @Override
    public void registerCat(Cat cat) {
        inTransaction(entityManager -> {
            entityManager.persist(cat);
        });
    }

    @Override
    public void deleteCat(Cat cat) {

    }

    @Override
    public List<Cat> getAllCats() {
        return entityManagerFactory.createEntityManager()
                .createQuery("FROM Cat")
                .getResultList();
    }
}
