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

import javax.sql.DataSource;

@EnableWebSecurity
public class MaConfigurationSecurite extends WebSecurityConfigurerAdapter {

//    sert pour le jdbcAuthentication
//    @Autowired
//    DataSource dataSource;

    @Autowired
    MonUserDetailService userDetailService;

    @Autowired
    JwtRequestFilter jwtRequestFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);


        //bien faire attention aux espaces dans la requete sql : il faut qu'il y ait des espaces entre chaque mot
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select nom, mot_de_passe, actif " +
//                        "from utilisateur " +
//                        "where nom= ?")
//                //récupération des rôles
//                .authoritiesByUsernameQuery("select u.nom, r.nom " +
//                        "from role r " +
//                        "join utilisateur_role ur on r.id = ur.id_role " +
//                        "join utilisateur u on u.id = ur.id_utilisateur " +
//                        "where u.nom = ?");
//
//                //2eme facon de faire : avec un boolean isadmin dans la table utilisateur
//                //on est obligé de mettre ROLE_role pour que ca fonctionne dans la bdd, juste admin ne fonctionne pas
////                .authoritiesByUsernameQuery("select u.nom, IF(u.is_admin,'ROLE_ADMIN','ROLE_USER') " +
////                "from utilisateur u " +
////                "where u.nom = ?");

    }

//    //surcharge l'authentification
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("root")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("root")
//                .roles("USER");
//    }

    //surcharge l'authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //il faut désactiver le fait de ne pas pouvoir faire des requetes sur un autre serveur
        http.csrf().disable()
                .authorizeRequests()
                //pour les requêtes qui seront uniquement accessibles avec un profil admin, il faudra ajouter /admin
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/authentification").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/categories").permitAll()
                .antMatchers("/cities").permitAll()
                .antMatchers("/activities").permitAll()
                .antMatchers("/recherche").permitAll()
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
