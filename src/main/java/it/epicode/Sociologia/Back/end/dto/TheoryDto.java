package it.epicode.Sociologia.Back.end.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheoryDto {
    private Long id;

    @NotEmpty(message = "Il nome della teoria non può essere vuoto o nullo.")
    @Size(min = 3, max = 100, message = "Il nome della teoria deve avere tra 3 e 50 caratteri")
    private String nomeTeoria;

    @NotEmpty(message = "L'autore non può essere vuoto o nullo.")
    private String autore;

    private String immagineAutoreUrl;

    @NotEmpty(message = "La spiegazione non può essere vuoto o nullo.")
    private String spiegazione;

    @NotEmpty(message = "L'esempio di applicazione moderna non può essere vuoto o nullo.")
    private String esempioApplicazioneModerna;
}
