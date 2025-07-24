package it.epicode.Sociologia.Back.end.security;

import org.springframework.security.core.Authentication;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.epicode.Sociologia.Back.end.exeption.SociologiaApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new SociologiaApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new SociologiaApiException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new SociologiaApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new SociologiaApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        } catch (SignatureException e) {
            throw new SociologiaApiException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature");
        }
    }
}