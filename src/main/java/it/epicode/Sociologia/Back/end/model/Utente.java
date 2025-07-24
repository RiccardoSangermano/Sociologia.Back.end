package it.epicode.Sociologia.Back.end.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "utenti", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "utenti_ruoli",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
    private Set<Role> ruoli = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "utenti_teorie_preferite",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "teoria_id"))
    private Set<Theory> teoriePreferite = new HashSet<>();
}
