package ru.tolstov.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.tolstov.utils.EntityManagerFactoryProvider;

import java.util.function.Consumer;

public abstract class LocalRepository {
    protected final EntityManagerFactory entityManagerFactory;
    public LocalRepository() {
        entityManagerFactory = EntityManagerFactoryProvider.getEntityManagerFactory();
    }
    protected void inTransaction(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            entityManager.close();
        }
    }
}
