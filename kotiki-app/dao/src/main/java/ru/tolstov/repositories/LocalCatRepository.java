package ru.tolstov.repositories;

import ru.tolstov.models.Cat;

import java.util.List;
import java.util.function.Consumer;

public class LocalCatRepository extends LocalRepository implements CatRepository {
    @Override
    public Cat registerCat(Consumer<Cat> work) {
        Cat cat = new Cat();
        inTransaction(entityManager -> {
            work.accept(cat);
            entityManager.persist(cat);
        });

        return cat;
    }

    @Override
    public Cat deleteCat(Cat cat) {
        inTransaction(entityManager -> {
            var persistedCat = entityManager.find(Cat.class, cat.getId());
            entityManager.remove(persistedCat);
        });

        return cat;
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
