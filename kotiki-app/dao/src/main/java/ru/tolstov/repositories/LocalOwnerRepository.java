package ru.tolstov.repositories;

import ru.tolstov.models.Owner;

import java.util.List;

public class LocalOwnerRepository extends LocalRepository implements OwnerRepository {
    @Override
    public long registerOwner(Owner owner) {
        entityManager.persist(owner);

        return owner.getId();
    }

    @Override
    public List<Owner> getAllOwners() {
        return entityManager
                .createQuery("FROM Owner")
                .getResultList();
    }

    @Override
    public Owner getOwnerById(long id) {
        return entityManager
                .find(Owner.class, id);
    }

    @Override
    public void deleteOwner(Owner owner) {
        entityManager.remove(owner);
    }
}
