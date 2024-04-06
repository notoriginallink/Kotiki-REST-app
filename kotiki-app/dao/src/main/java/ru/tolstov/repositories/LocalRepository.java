package ru.tolstov.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.utils.EntityManagerFactoryProvider;

import java.util.function.Consumer;

public abstract class LocalRepository implements Repository {
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
