package ru.tolstov.repositories;

import jakarta.persistence.EntityManager;

public abstract class LocalRepository implements Repository {
    protected EntityManager entityManager;
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
