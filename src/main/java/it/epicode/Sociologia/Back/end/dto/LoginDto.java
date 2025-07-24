package it.epicode.Sociologia.Back.end.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = " L'username o email non posssono essere vuoti")
    private String usernameOrEmail;

    @NotBlank(message = " La password non può essere vuota")
    private String password;
}
