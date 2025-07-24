package it.epicode.Sociologia.Back.end.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Tentativo di autenticazione per la richiesta: {}", request.getRequestURI());

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            logger.debug("Token estratto: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
            if (jwtTokenProvider.validateToken(token)) {
                logger.debug("Token JWT valido.");
                String username = jwtTokenProvider.getUsername(token);
                logger.debug("Username estratto dal token: {}", username);

                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.debug("UserDetails caricati per l'utente: {}", username);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.debug("Autenticazione impostata nel SecurityContextHolder per l'utente: {}", username);
                } catch (Exception e) {

                    logger.error("Errore durante il caricamento dell'utente o l'impostazione dell'autenticazione per l'username {}: {}", username, e.getMessage());
                }
            } else {
                logger.warn("Token JWT NON valido o scaduto per la richiesta: {}", request.getRequestURI());
            }
        } else {

            logger.debug("Nessun token JWT trovato nell'header Authorization per la richiesta: {}", request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            logger.debug("Header Authorization trovato: {}", bearerToken);
            return bearerToken.substring(7);
        }
        logger.debug("Header Authorization non trovato o non inizia con 'Bearer '");
        return null;
    }
}