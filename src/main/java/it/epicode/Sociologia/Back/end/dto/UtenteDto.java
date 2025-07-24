package it.epicode.Sociologia.Back.end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtenteDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<String> ruoli;
    private Set<TheoryResponseDto> teoriePreferite;
}