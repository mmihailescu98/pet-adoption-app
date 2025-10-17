package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.service.impl.PetServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/pets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PetAdminController {

    private final PetServiceImpl petService;

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.savePet(pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePetById(Math.toIntExact(id));
        return ResponseEntity.noContent().build();
    }
}
