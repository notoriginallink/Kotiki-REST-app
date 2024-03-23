package ru.tolstov.repositories;

import ru.tolstov.models.Owner;

import java.util.List;

public class LocalOwnerRepository extends LocalRepository implements OwnerRepository {
    @Override
    public void registerOwner(Owner owner) {
        inTransaction(entityManager -> {
            entityManager.persist(owner);
        });
    }

    @Override
    public List<Owner> getAllOwners() {
        return entityManagerFactory.createEntityManager()
                .createQuery("FROM Owner")
                .getResultList();
    }

    @Override
    public Owner getOwnerById(long id) {
        return entityManagerFactory.createEntityManager()
                .find(Owner.class, id);
    }

    @Override
    public void deleteOwner(Owner owner) {
        inTransaction(entityManager -> {
            var persistedOwner = entityManager.find(Owner.class, owner.getId());
            entityManager.remove(persistedOwner);
        });
    }
}
