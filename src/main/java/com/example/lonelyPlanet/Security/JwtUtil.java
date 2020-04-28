package com.example.lonelyPlanet.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    //aller voir la value dans application properties
    @Value("${jwt.secret}")
    private String secret;

    //le claims est le format qu'on va extraire du token
    //Retourne le corps du token
    public Claims extractionDuCorpDuToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    //Retourne un token
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> tokenData = new HashMap<>();

        //ici vous pouvez rajouter tout ce que vous voulez comme données sauf le mot de passe
        //ici on a ajouté le role
        tokenData.put("role", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));

        // ce sont les données obligatoires
        return Jwts.builder()
                .setClaims(tokenData)
                //sub dans le token
                .setSubject(userDetails.getUsername())
                //date d'aujourd'hui qu'il faut mettre en milliseconds
                //iat
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //date d'expiration du token : ici c'est 10 heures
                //exp
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                //avec quel algo on va le signer
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //Retourne vrai si le token n'a pas dépassé la date d'expiration
    private Boolean tokenNonDepasseDateExpiration(String token) {
        return extractionDuCorpDuToken(token)
                .getExpiration()
                .before(new Date());
    }

    //Retourne vrai si le nom de l'utilisateur tentant de se connecter correspond
    // au subject du corp du token et si la date d'expiration n'est pas passée.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractionDuCorpDuToken(token).getSubject();
        return (username.equals(userDetails.getUsername()) && !tokenNonDepasseDateExpiration(token));
    }

}
