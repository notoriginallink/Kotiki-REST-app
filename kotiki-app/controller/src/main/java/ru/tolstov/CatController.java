package ru.tolstov;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.tolstov.entities.User;
import ru.tolstov.models.CatColor;
import ru.tolstov.models.UserRole;
import ru.tolstov.services.CatService;
import ru.tolstov.services.UserService;
import ru.tolstov.services.dto.CatDto;
import ru.tolstov.services.dto.UserDto;
import ru.tolstov.services.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cats")
@AllArgsConstructor
public class CatController {
    private final CatService catService;

    @GetMapping("")
    public ResponseEntity<List<CatDto>> filter(
            @RequestParam(required = false) CatColor color,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer year
    ) {
        var userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long ownerId = userDetails.getUser().getOwner();
        return ResponseEntity.ok(catService.findFiltered(color, breed, year, ownerId));
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody CatDto cat) {
        long id = catService.addCat(
                cat.getName(),
                cat.getBirthdate(),
                cat.getBreed(),
                cat.getColor(),
                cat.getOwner()
        );
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getById(@PathVariable long id) {
        var cat = catService.getCatByID(id);

        if (cat.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(cat.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        catService.removeCat(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> findFriends(@PathVariable long id) {
        var friends = catService.getFriends(id);
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/{id}/friends")
    public ResponseEntity<?> makeFriend(
            @PathVariable long id,
            @RequestParam long friendId
    ) {
        catService.makeFriendship(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/friends")
    public ResponseEntity<?> deleteFriend(
            @PathVariable long id,
            @RequestParam long friendId
    ) {
        catService.destroyFriendship(id, friendId);
        return ResponseEntity.ok().build();
    }
}
