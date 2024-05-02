package ru.tolstov.services;

import ru.tolstov.models.UserRole;
import ru.tolstov.services.dto.UserDto;

import java.util.List;

public interface UserService {
    void create(String username, String password, UserRole role, Long ownerId);
    void delete(String username);
    List<UserDto> getAll();
}
