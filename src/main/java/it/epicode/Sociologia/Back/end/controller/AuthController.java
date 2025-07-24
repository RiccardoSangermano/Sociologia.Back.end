package it.epicode.Sociologia.Back.end.controller;

import it.epicode.Sociologia.Back.end.dto.JwtAuthResponse;
import it.epicode.Sociologia.Back.end.dto.LoginDto;
import it.epicode.Sociologia.Back.end.dto.RegisterDto;
import it.epicode.Sociologia.Back.end.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        try {
            String response = authService.register(registerDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore interno del server durante la registrazione: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            JwtAuthResponse response = authService.login(loginDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}