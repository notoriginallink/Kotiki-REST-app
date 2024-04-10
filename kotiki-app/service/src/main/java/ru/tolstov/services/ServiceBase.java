package ru.tolstov.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.tolstov.utils.EntityManagerFactoryProvider;

import java.util.function.Function;

public abstract class ServiceBase {
    protected final EntityManagerFactory entityManagerFactory;
    public ServiceBase(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    protected <T> T inTransaction(Function<EntityManager, T> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        T result;
        try {
            transaction.begin();
            result = work.apply(entityManager);
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

        return result;
    }
}
