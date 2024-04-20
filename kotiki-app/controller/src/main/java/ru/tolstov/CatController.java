package ru.tolstov;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tolstov.models.CatColor;
import ru.tolstov.services.CatService;
import ru.tolstov.services.dto.CatDto;

import java.util.List;

@RestController
@RequestMapping("/cats")
@AllArgsConstructor
public class CatController {
    @Autowired
    private final CatService catService;

    @GetMapping("filter")
    public ResponseEntity<List<CatDto>> filter(
            @RequestParam(required = false) CatColor color,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(catService.findFiltered(color, breed, year));
    }
    @GetMapping("all")
    public ResponseEntity<List<CatDto>> getAll() {
        return ResponseEntity.ok(catService.getAllCats());
    }

    @GetMapping("/id{id}")
    public ResponseEntity<CatDto> getById(@PathVariable long id) {
        var cat = catService.getCatByID(id);
        if (cat.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(cat.get());
    }

    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody CatDto cat) {
        try {
            long id = catService.addCat(
                    cat.getName(),
                    cat.getBirthdate(),
                    cat.getBreed(),
                    cat.getColor(),
                    cat.getOwner()
            );
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("id{id}/delete")
    public ResponseEntity<?> delete(@PathVariable long id) {
        var result = catService.removeCat(id);
        if (result)
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @GetMapping("id{id}/friends")
    public ResponseEntity<?> findFriends(@PathVariable long id) {
        try {
            var friends = catService.getFriends(id);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("id{id}/make-friend{friendId}")
    public ResponseEntity<?> makeFriends(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        try {
            boolean areFriends = catService.areFriends(id, friendId);
            if (areFriends)
                return ResponseEntity.badRequest().body("Are already friends");

            catService.makeFriendship(id, friendId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("id{id}/delete-friend{friendId}")
    public ResponseEntity<?> deleteFriend(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        try {
            boolean areFriends = catService.areFriends(id, friendId);
            if (!areFriends)
                return ResponseEntity.badRequest().body("Are not friends");

            catService.destroyFriendship(id, friendId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
