package ru.tolstov.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tolstov.entities.Owner;
import ru.tolstov.entities.User;
import ru.tolstov.models.UserRole;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.repositories.UserRepository;
import ru.tolstov.services.dto.UserDto;
import ru.tolstov.services.exceptions.UserServiceOperationException;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    @Override
    @Transactional
    public void create(String username, String password, UserRole role, Long ownerId) {
        if (userRepository.findById(username).isPresent())
            throw new UserServiceOperationException("Failed to create user: user with username %s already exists".formatted(username));


        Owner owner = null;
        if (ownerId != null) {
            var optional = ownerRepository.findById(ownerId);
            if (optional.isEmpty())
                throw new UserServiceOperationException("Failed to create user: owner %s does not exist");
            owner = optional.get();
        }

        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setOwner(owner);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(String username) {
        var optional = userRepository.findById(username);
        if (optional.isEmpty())
            throw new UserServiceOperationException("Failed to delete: user with username %s not found".formatted(username));

        userRepository.delete(optional.get());
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserDto::new).toList();
    }
}
