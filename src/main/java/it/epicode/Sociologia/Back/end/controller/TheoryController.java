package it.epicode.Sociologia.Back.end.controller;

import it.epicode.Sociologia.Back.end.dto.TheoryDto;
import it.epicode.Sociologia.Back.end.dto.TheoryPagedResponseDto;
import it.epicode.Sociologia.Back.end.dto.TheoryResponseDto;
import it.epicode.Sociologia.Back.end.service.TheoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/theories")
@CrossOrigin(origins = "http://localhost:5173")
public class TheoryController {

    @Autowired
    private TheoryService theoryService;

    @GetMapping
    public ResponseEntity<TheoryPagedResponseDto> getAllTheories(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword
    ) {
        TheoryPagedResponseDto theories = theoryService.getTeorie(keyword, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(theories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheoryResponseDto> getTeoriaById(@PathVariable Long id) {
        return ResponseEntity.ok(theoryService.getTeoriaById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TheoryResponseDto> createTeoria(@Valid @RequestBody TheoryDto teoriaDto) {
        TheoryResponseDto createdTeoria = theoryService.createTeoria(teoriaDto);
        return new ResponseEntity<>(createdTeoria, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TheoryResponseDto> updateTeoria(@PathVariable Long id, @Valid @RequestBody TheoryDto teoriaDto) {
        TheoryResponseDto updatedTeoria = theoryService.updateTeoria(id, teoriaDto);
        return ResponseEntity.ok(updatedTeoria);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeoria(@PathVariable Long id) {
        theoryService.deleteTeoria(id);
        return ResponseEntity.ok("Teoria eliminata con successo!");
    }
}