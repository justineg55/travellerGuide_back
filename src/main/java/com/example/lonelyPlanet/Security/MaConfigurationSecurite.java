package com.example.lonelyPlanet.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@EnableWebSecurity
public class MaConfigurationSecurite extends WebSecurityConfigurerAdapter {


    @Autowired
    MonUserDetailService userDetailService;

    @Autowired
    JwtRequestFilter jwtRequestFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    //surcharge l'authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //il faut désactiver le fait de ne pas pouvoir faire des requetes sur un autre serveur
        http.csrf().disable()
                //pour faire une requete avec javascript
//                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//                .and().httpBasic()
//                .and().authorizeRequests()
                .authorizeRequests()
                //pour l'inscription et l'authentification il faut que ce soit accessible à tout le monde
                .antMatchers("/authentification","/inscription", "/").permitAll()
                .antMatchers("/users").hasRole("ADMIN")
                .antMatchers("/categories", "/categories/**",
                        "/cities", "/cities/**",
                        "/activities", "/activities/**",
                        "users/**", "/search").hasAnyRole("USER", "ADMIN")
                //c'était pour générer une page de login, on va pas l'utiliser avec les tokens
//                .and()
//                .formLogin();
                //on doit être authentifié pour faire une requête
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    //le @Bean permet de faire un héritage
    @Bean
    public PasswordEncoder getPasswordEncoder() {

//        return NoOpPasswordEncoder.getInstance();
        //on va crypter le mot de passe :
        return new BCryptPasswordEncoder();
    }

    //ajouté pour ne pas avoir de problème avec le autowired de UtilisateurController
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
