package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.FavoritePetDTO;
import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.FavoritePet;
import cloudflight.integra.backend.service.FavoritePetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController()
public class FavoritePetsController {
    FavoritePetService favoritePetService;

    public FavoritePetsController(FavoritePetService favoritePetService) {
        this.favoritePetService = favoritePetService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<PetDTO>> getFavoritePets(@RequestParam Long userId) {
        if (userId != null) {
            try{
                List<PetDTO> dtos = PetMapper.INSTANCE.petToPetDTOList(favoritePetService.getFavoritePetsForUser(userId));
                return new ResponseEntity<>(dtos, HttpStatus.OK);
            }catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }else
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoritePet> addFavoritePet(@RequestBody FavoritePetDTO requestdto) {
        try {
            FavoritePet createdfavPet = favoritePetService.saveFavoritePet(requestdto.userId(),requestdto.petId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdfavPet);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<FavoritePet> removeFavoritePet(@RequestBody FavoritePetDTO requestdto) {
        try{
            FavoritePet deletedFavPet = favoritePetService.deleteFavoritePet(requestdto.userId(),requestdto.petId());
            return ResponseEntity.ok(deletedFavPet);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
