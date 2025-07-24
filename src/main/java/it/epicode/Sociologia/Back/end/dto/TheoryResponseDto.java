package it.epicode.Sociologia.Back.end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheoryResponseDto {
    private Long id;
    private String nomeTeoria;
    private String autore;
    private String immagineAutoreUrl;
    private String spiegazione;
    private String esempioApplicazioneModerna;
}
