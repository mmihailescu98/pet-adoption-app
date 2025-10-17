package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.AdoptionRequest;
import cloudflight.integra.backend.service.impl.AdoptionServiceImpl;
import cloudflight.integra.backend.service.impl.PetServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/adoptions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdoptionAdminController {

    private final AdoptionServiceImpl adoptionService;

    @PostMapping("/{id}/approve")
    public ResponseEntity<AdoptionRequest> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.approveRequest(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<AdoptionRequest> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.rejectRequest(id));
    }
}

