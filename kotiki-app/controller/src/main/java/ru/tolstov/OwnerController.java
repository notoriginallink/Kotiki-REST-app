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
@RequestMapping("/owners/")
public class OwnerController {
    @Autowired
    private final OwnerService ownerService;

    @GetMapping("all")
    public ResponseEntity<List<OwnerDto>> getAll() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("{id}")
    public ResponseEntity<OwnerDto> getById(@PathVariable long id) {
        var owner = ownerService.getById(id);
        if (owner.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(owner.get());
    }
}
