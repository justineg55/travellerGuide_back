package com.example.lonelyPlanet.Security;

import com.example.lonelyPlanet.Model.Role;
import com.example.lonelyPlanet.Model.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//c'est ici qu'on va regrouper toutes les infos de l'utilisateur
public class MonUserDetail implements UserDetails {
        private String userName;
        private String password;
        private boolean active;
        private List<GrantedAuthority> authorities;


//        //exemple si on a un boolean isAdmin
//    public MonUserDetail(String pseudo, String motdepasse, boolean isAdmin) {
//        this.userName = pseudo;
//        this.password = motdepasse;
//        this.active = true;
//
//        this.authorities=new ArrayList<>();
//        if(isAdmin){
//            this.authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }
//    }


        public MonUserDetail(Utilisateur utilisateur) {
            this.userName = utilisateur.getPseudo();
            this.password = utilisateur.getPassword();
            this.active = utilisateur.isActif();

//            this.authorities = utilisateur.getListeRole().stream()
//                    .map(Role::getNom)
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());

            //c'est dans la liste authorities de type GrantedAuthority qu'on doit ajouter les rôles
            this.authorities=new ArrayList<>();

            for(Role role : utilisateur.getListeRole()){
                this.authorities.add(new SimpleGrantedAuthority(role.getNom()));
            }
        }

        //c'est ici qu'on récupère les rôles, on les récupère comme on veut mais il faut que dans getAuthorities il récupère les rôles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    //Username=champ unique : ca peut être n'importe quoi : identifiant, code, code de l'emprinte digitale, email, numéro de compte bancaire
    @Override
    public String getUsername() {
        return userName;
    }

    //session expirée
    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    //verrouille le compte
    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    //est-ce que son compte a été supprimé ?
    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    //est-ce qu'il est actif ou pas ?
    @Override
    public boolean isEnabled() {
        return active;
    }
}
