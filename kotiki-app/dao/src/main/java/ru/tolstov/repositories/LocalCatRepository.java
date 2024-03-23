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
        inTransaction(entityManager -> {
            var persistedCat = entityManager.find(Cat.class, cat.getId());
            entityManager.remove(persistedCat);
        });
    }

    @Override
    public List<Cat> getAllCats() {
        return entityManagerFactory.createEntityManager()
                .createQuery("FROM Cat")
                .getResultList();
    }
}
