package ru.tolstov.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryProvider {
    private static EntityManagerFactory entityManagerFactory;
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory("cats-tracker-db");
                return entityManagerFactory;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return entityManagerFactory;
    }
}
