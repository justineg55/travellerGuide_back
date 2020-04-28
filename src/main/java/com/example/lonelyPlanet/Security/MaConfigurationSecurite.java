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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


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
                .authorizeRequests()
                //pour les requêtes qui seront uniquement accessibles avec un profil admin, il faudra ajouter /admin
//                .antMatchers("").permitAll()
                .antMatchers("/authentification").permitAll()
                .antMatchers("/users").permitAll()
                .antMatchers("/users/*").permitAll()
                .antMatchers("/categories").permitAll()
                .antMatchers("/categories/*").permitAll()
                .antMatchers("/cities").permitAll()
                .antMatchers("/cities/*").permitAll()
                .antMatchers("/activities").permitAll()
                .antMatchers("/activities/*").permitAll()
                .antMatchers("/search").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
//
////                .antMatchers("/").permitAll()
//
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


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //ajouté pour ne pas avoir de problème avec le autowired de UtilisateurController
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
