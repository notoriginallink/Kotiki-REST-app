package ru.tolstov.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import ru.tolstov.utils.EntityManagerFactoryProvider;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ServiceBase {
    protected final EntityManagerFactory entityManagerFactory;
    public ServiceBase() {
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

    protected boolean inTransactionBool(Function<EntityManager, Boolean> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            boolean res = work.apply(entityManager);
            transaction.commit();
            return res;
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
