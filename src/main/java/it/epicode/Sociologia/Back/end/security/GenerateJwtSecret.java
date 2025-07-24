package it.epicode.Sociologia.Back.end.security;

import java.util.Base64;
import java.security.SecureRandom;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        byte[] secretBytes = new byte[32];
        new SecureRandom().nextBytes(secretBytes);
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);
        System.out.println("La tua nuova chiave segreta Base64: " + base64Secret);
    }
}
