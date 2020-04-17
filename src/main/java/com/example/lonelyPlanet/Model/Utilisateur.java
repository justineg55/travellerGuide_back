package com.example.lonelyPlanet.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    //on choisit quel champ sera unique (c'est le champ UserName dans UserDetail)
    @Column(unique=true)
    private String pseudo;

    private String password;
    private boolean actif;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="role_utilisateur")
    private Set<Role> listeRole;


}
