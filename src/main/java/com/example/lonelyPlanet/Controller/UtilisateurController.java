package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Utilisateur;
import com.example.lonelyPlanet.Security.JwtUtil;
import com.example.lonelyPlanet.Security.MonUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController {


        private AuthenticationManager authenticationManager;
        private MonUserDetailService userDetailsService;
        private JwtUtil jwtUtil;

        @Autowired
        public UtilisateurController(
                AuthenticationManager authenticationManager,
                MonUserDetailService userDetailsService,
                JwtUtil jwtUtil) {
            this.authenticationManager = authenticationManager;
            this.userDetailsService = userDetailsService;
            this.jwtUtil = jwtUtil;
        }

    @PostMapping("/authentification")
    public String authentification(@RequestBody Utilisateur utilisateur) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            utilisateur.getPseudo(), utilisateur.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new Exception("Pseudo ou mot de passe incorrect", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(utilisateur.getPseudo());
        return jwtUtil.generateToken(userDetails);
    }
}
