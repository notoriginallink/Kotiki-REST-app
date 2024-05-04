package ru.tolstov;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.tolstov.models.CatColor;
import ru.tolstov.services.CatService;
import ru.tolstov.services.UserService;
import ru.tolstov.services.dto.UserDto;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private CatService catService;
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        userService.create(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getOwner());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        userService.delete(username);
        return ResponseEntity.ok().build();
    }
}
