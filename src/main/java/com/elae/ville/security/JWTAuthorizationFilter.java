package com.elae.ville.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lire l'en-tête Authorization
        String jwt = request.getHeader("Authorization");

        // 2. Pas de token ou mauvais format : on laisse passer la requête
        if (jwt == null || !jwt.startsWith(SecParams.PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Préparer le vérificateur avec la même clé secrète que celle de la génération
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecParams.SECRET)).build();

        // 4. Enlever "Bearer " (7 caractères)
        jwt = jwt.substring(SecParams.PREFIX.length());

     // 5. Vérifier la signature et l'expiration, puis décoder
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(jwt);
        } catch (JWTVerificationException e) {
            // Token expiré ou signature incorrecte : on refuse proprement
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 6. Extraire le username (subject) et les rôles
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);

        // 7. Convertir les rôles en GrantedAuthority
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String r : roles)
            authorities.add(new SimpleGrantedAuthority(r));

        // 8. Créer l'objet d'authentification (pas de mot de passe : null)
        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // 9. Informer Spring Security que cet utilisateur est authentifié
        SecurityContextHolder.getContext().setAuthentication(user);

        // 10. Passer au filtre suivant
        filterChain.doFilter(request, response);
    }
}

