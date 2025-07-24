package it.epicode.Sociologia.Back.end.model;

import it.epicode.Sociologia.Back.end.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ruoli")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Roles nome;
}