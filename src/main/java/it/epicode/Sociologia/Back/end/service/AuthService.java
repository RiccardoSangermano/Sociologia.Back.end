package it.epicode.Sociologia.Back.end.service;

import it.epicode.Sociologia.Back.end.exeption.SociologiaApiException;
import it.epicode.Sociologia.Back.end.enums.Roles;
import it.epicode.Sociologia.Back.end.model.Role;
import it.epicode.Sociologia.Back.end.model.Utente;
import it.epicode.Sociologia.Back.end.dto.JwtAuthResponse;
import it.epicode.Sociologia.Back.end.dto.LoginDto;
import it.epicode.Sociologia.Back.end.dto.RegisterDto;
import it.epicode.Sociologia.Back.end.repository.RoleRepository;
import it.epicode.Sociologia.Back.end.repository.UtenteRepository;
import it.epicode.Sociologia.Back.end.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return new JwtAuthResponse(token, "Bearer", roles);
    }

    public String register(RegisterDto registerDto) {
        if (utenteRepository.existsByUsername(registerDto.getUsername())) {
            throw new SociologiaApiException(HttpStatus.BAD_REQUEST, "Username già esistente.");
        }
        if (utenteRepository.existsByEmail(registerDto.getEmail())) {
            throw new SociologiaApiException(HttpStatus.BAD_REQUEST, "Email già esistente.");
        }

        Utente utente = new Utente();
        utente.setUsername(registerDto.getUsername());
        utente.setEmail(registerDto.getEmail());
        utente.setPassword(passwordEncoder.encode(registerDto.getPassword()));


        Set<Role> ruoli = new HashSet<>();
        if (registerDto.getRuoli() == null || registerDto.getRuoli().isEmpty()) {
            ruoli.add(roleRepository.findByNome(Roles.ROLE_USER).orElseThrow(
                    () -> new SociologiaApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Ruolo USER non trovato."))
            );
        } else {
            registerDto.getRuoli().forEach(roleString -> {
                try {
                    Roles roleEnum = Roles.valueOf(roleString.toUpperCase());
                    ruoli.add(roleRepository.findByNome(roleEnum).orElseThrow(
                            () -> new SociologiaApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Ruolo " + roleEnum.name() + " non trovato nel database!"))
                    );
                } catch (IllegalArgumentException e) {
                    throw new SociologiaApiException(HttpStatus.BAD_REQUEST, "Ruolo specificato non valido: " + roleString);
                }
            });
        }
        utente.setRuoli(ruoli);
        utenteRepository.save(utente);

        return "Utente registrato con successo!";
    }
}