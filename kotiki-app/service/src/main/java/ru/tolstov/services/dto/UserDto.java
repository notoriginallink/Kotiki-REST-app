package ru.tolstov.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tolstov.entities.User;
import ru.tolstov.models.UserRole;

@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private UserRole role;
    private Long owner;

    public UserDto(User user) {
        username = user.getUsername();
        role = user.getRole();
        password = user.getPassword();
        if (user.getOwner() != null)
            owner = user.getOwner().getId();
    }
}
