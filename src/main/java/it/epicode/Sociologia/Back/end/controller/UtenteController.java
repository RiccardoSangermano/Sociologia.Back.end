package it.epicode.Sociologia.Back.end.controller;

import it.epicode.Sociologia.Back.end.dto.UtenteDto;
import it.epicode.Sociologia.Back.end.service.UtenteService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowedHeaders = "*", allowCredentials = "true")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UtenteDto> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(utenteService.getUtenteByUsername(username));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/me/teorie-preferite/{teoriaId}")
    public ResponseEntity<UtenteDto> addTeoriaPreferita(@PathVariable Long teoriaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(utenteService.addTeoriaPreferita(username, teoriaId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/me/teorie-preferite/{teoriaId}")
    public ResponseEntity<UtenteDto> removeTeoriaPreferita(@PathVariable Long teoriaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(utenteService.removeTeoriaPreferita(username, teoriaId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UtenteDto> getUtenteById(@PathVariable Long id) {
        return ResponseEntity.ok(utenteService.getUtenteById(id));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UtenteDto>> getAllUtenti() {
        return ResponseEntity.ok(utenteService.getAllUtenti());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UtenteDto> createUtente(@RequestBody UtenteDto utenteDto) {
        return ResponseEntity.ok(utenteService.createUtente(utenteDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UtenteDto> updateUtente(@PathVariable Long id, @RequestBody UtenteDto utenteDto) {
        return ResponseEntity.ok(utenteService.updateUtente(id, utenteDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) {
        utenteService.deleteUtente(id);
        return ResponseEntity.noContent().build();
    }
}