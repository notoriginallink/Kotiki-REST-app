package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;

public class LocalCatRepository extends LocalRepository implements CatRepository {
    @Override
    public long registerCat(Cat cat) {
        inTransaction(entityManager -> {
            entityManager.persist(cat);
        });

        return cat.getId();
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

    @Override
    public Cat getCatById(long id) {
        return entityManagerFactory.createEntityManager()
                .find(Cat.class, id);
    }

    @Override
    public void updateFriendship(Cat firstCat, Cat secondCat) {
        inTransaction(entityManager -> {
            entityManager.merge(firstCat);
            entityManager.merge(secondCat);
        });
    }
}
