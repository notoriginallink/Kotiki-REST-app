package ru.tolstov;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tolstov.services.OwnerService;
import ru.tolstov.services.dto.OwnerDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("")
    public ResponseEntity<List<OwnerDto>> getAll() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody OwnerDto owner) {
        var id = ownerService.createOwner(
                owner.getFirstName(),
                owner.getLastName(),
                owner.getBirthdate());
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getById(@PathVariable long id) {
        var owner = ownerService.getById(id);
        if (owner.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(owner.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        ownerService.removeOwner(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/cats")
    public ResponseEntity<?> getCats(@PathVariable long id) {
        var cats = ownerService.getAllCats(id);
        return ResponseEntity.ok(cats);
    }
}
