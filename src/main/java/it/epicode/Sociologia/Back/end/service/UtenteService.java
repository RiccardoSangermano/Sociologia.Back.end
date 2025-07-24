package it.epicode.Sociologia.Back.end.service;

import it.epicode.Sociologia.Back.end.dto.TheoryResponseDto;
import it.epicode.Sociologia.Back.end.dto.UtenteDto;
import it.epicode.Sociologia.Back.end.exeption.ResourceNotFoundException;
import it.epicode.Sociologia.Back.end.model.Role;
import it.epicode.Sociologia.Back.end.model.Theory;
import it.epicode.Sociologia.Back.end.model.Utente;
import it.epicode.Sociologia.Back.end.repository.RoleRepository;
import it.epicode.Sociologia.Back.end.repository.TheoryRepository;
import it.epicode.Sociologia.Back.end.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.epicode.Sociologia.Back.end.enums.Roles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private TheoryRepository theoryRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UtenteDto mapUtenteToDto(Utente utente) {
        UtenteDto dto = new UtenteDto();
        dto.setId(utente.getId());
        dto.setUsername(utente.getUsername());
        dto.setEmail(utente.getEmail());
        dto.setRuoli(utente.getRuoli().stream()
                .map(role -> role.getNome().name())
                .collect(Collectors.toSet()));

        if (utente.getTeoriePreferite() != null) {
            dto.setTeoriePreferite(utente.getTeoriePreferite().stream()
                    .map(this::mapTheoryToResponseDto) // Utilizza il metodo helper
                    .collect(Collectors.toSet()));
        } else {
            dto.setTeoriePreferite(new HashSet<>());
        }
        dto.setPassword(null);
        return dto;
    }

    private Utente mapDtoToUtente(UtenteDto utenteDto) {
        Utente utente = new Utente();
        utente.setUsername(utenteDto.getUsername());
        utente.setEmail(utenteDto.getEmail());

        if (utenteDto.getPassword() != null && !utenteDto.getPassword().isEmpty()) {
            utente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
        } else {
            throw new IllegalArgumentException("La password non può essere vuota per la creazione di un utente.");
        }

        if (utenteDto.getRuoli() != null && !utenteDto.getRuoli().isEmpty()) {
            utente.setRuoli(utenteDto.getRuoli().stream()
                    .map(roleName -> {
                        Roles roleEnum = Roles.valueOf(roleName.toUpperCase());
                        return roleRepository.findByNome(roleEnum)
                                .orElseThrow(() -> new ResourceNotFoundException("Ruolo", "nome", roleName));
                    })
                    .collect(Collectors.toSet()));
        } else {
            Role defaultRole = roleRepository.findByNome(Roles.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Ruolo", "nome", Roles.ROLE_USER.name()));
            Set<Role> defaultRoles = new HashSet<>();
            defaultRoles.add(defaultRole);
            utente.setRuoli(defaultRoles);
        }
        return utente;
    }

    private TheoryResponseDto mapTheoryToResponseDto(Theory theory) {
        TheoryResponseDto dto = new TheoryResponseDto();
        dto.setId(theory.getId());
        dto.setNomeTeoria(theory.getNomeTeoria());
        dto.setAutore(theory.getAutore());
        dto.setImmagineAutoreUrl(theory.getImmagineAutoreUrl());
        dto.setSpiegazione(theory.getSpiegazione());
        dto.setEsempioApplicazioneModerna(theory.getEsempioApplicazioneModerna());
        return dto;
    }


    @Transactional(readOnly = true)
    public List<UtenteDto> getAllUtenti() {
        return utenteRepository.findAll().stream()
                .map(this::mapUtenteToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UtenteDto createUtente(UtenteDto utenteDto) {
        if (utenteRepository.findByUsername(utenteDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + utenteDto.getUsername() + "' già in uso.");
        }
        if (utenteRepository.findByEmail(utenteDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email '" + utenteDto.getEmail() + "' già in uso.");
        }

        Utente utente = mapDtoToUtente(utenteDto);
        Utente newUtente = utenteRepository.save(utente);
        return mapUtenteToDto(newUtente);
    }

    @Transactional
    public UtenteDto updateUtente(Long id, UtenteDto utenteDto) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "id", id));

        if (utenteDto.getUsername() != null && !utenteDto.getUsername().isEmpty()) {
            utente.setUsername(utenteDto.getUsername());
        }
        if (utenteDto.getEmail() != null && !utenteDto.getEmail().isEmpty()) {
            utente.setEmail(utenteDto.getEmail());
        }

        if (utenteDto.getPassword() != null && !utenteDto.getPassword().isEmpty()) {
            utente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
        }

        if (utenteDto.getRuoli() != null && !utenteDto.getRuoli().isEmpty()) {
            Set<Role> updatedRoles = utenteDto.getRuoli().stream()
                    .map(roleName -> {
                        Roles roleEnum = Roles.valueOf(roleName.toUpperCase());
                        return roleRepository.findByNome(roleEnum)
                                .orElseThrow(() -> new ResourceNotFoundException("Ruolo", "nome", roleName));
                    })
                    .collect(Collectors.toSet());
            utente.setRuoli(updatedRoles);
        }

        Utente updatedUtente = utenteRepository.save(utente);
        return mapUtenteToDto(updatedUtente);
    }

    @Transactional
    public void deleteUtente(Long id) {
        if (!utenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utente", "id", id);
        }
        utenteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UtenteDto getUtenteById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "id", id));
        return mapUtenteToDto(utente);
    }

    @Transactional(readOnly = true)
    public UtenteDto getUtenteByUsername(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));
        return mapUtenteToDto(utente);
    }

    @Transactional
    public UtenteDto addTeoriaPreferita(String username, Long teoriaId) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));
        Theory teoria = theoryRepository.findById(teoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Teoria", "id", teoriaId));

        if (!utente.getTeoriePreferite().contains(teoria)) {
            utente.getTeoriePreferite().add(teoria);
            Utente updatedUtente = utenteRepository.save(utente);
            return mapUtenteToDto(updatedUtente);
        }
        return mapUtenteToDto(utente);
    }

    @Transactional
    public UtenteDto removeTeoriaPreferita(String username, Long teoriaId) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", "username", username));
        Theory teoriaToRemove = theoryRepository.findById(teoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Teoria", "id", teoriaId));

        if (utente.getTeoriePreferite().contains(teoriaToRemove)) {
            utente.getTeoriePreferite().remove(teoriaToRemove);
            Utente updatedUtente = utenteRepository.save(utente);
            return mapUtenteToDto(updatedUtente);
        } else {
            return mapUtenteToDto(utente);
        }
    }
}