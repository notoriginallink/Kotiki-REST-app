package ru.tolstov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tolstov.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
