package it.epicode.Sociologia.Back.end.security;

import it.epicode.Sociologia.Back.end.model.Utente;
import it.epicode.Sociologia.Back.end.repository.UtenteRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UtenteRepository utenteRepository;

    public CustomUserDetailsService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> utenteRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con username o email: "+ usernameOrEmail)));

        Set<GrantedAuthority> authorities = utente.getRuoli().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getNome().name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(utente.getUsername(),
                utente.getPassword(),
                authorities);
    }
}
