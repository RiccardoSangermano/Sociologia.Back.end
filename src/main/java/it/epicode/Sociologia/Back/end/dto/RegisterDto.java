package it.epicode.Sociologia.Back.end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class RegisterDto {
    @NotBlank(message = "Il nome non puo essere vuoto")
    @Size(min= 5, max = 30, message = "Il nome deve avere minimo 5 e massimo 30 caratteri ")
    private String username;

    @NotBlank(message = "L'email non puo essere vuoto")
    @Email(message = "Formato non valido")
    private String email;

    @NotBlank(message = "Il nome non puo essere vuoto")
    @Size(min= 8, message = "La password deve avere minimo 8 caratteri ")
    private String password;

    private Set<String> ruoli;
}
