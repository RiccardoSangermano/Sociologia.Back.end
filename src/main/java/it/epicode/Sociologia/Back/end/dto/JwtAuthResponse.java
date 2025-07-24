package it.epicode.Sociologia.Back.end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Set<String> roles;
}