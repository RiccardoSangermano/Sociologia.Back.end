package it.epicode.Sociologia.Back.end.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "teorie")
@NoArgsConstructor
@AllArgsConstructor
public class Theory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeTeoria;
    private String autore;
    private String immagineAutoreUrl;
    @Column(columnDefinition = "TEXT")
    private String spiegazione;
    @Column(columnDefinition = "TEXT")
    private String esempioApplicazioneModerna;
}
