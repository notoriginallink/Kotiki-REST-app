package ru.tolstov.repositories;

import jakarta.persistence.EntityManager;

public interface Repository {
    void setEntityManager(EntityManager entityManager);
}
