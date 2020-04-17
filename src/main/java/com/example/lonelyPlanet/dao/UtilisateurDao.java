package com.example.lonelyPlanet.dao;

import com.example.lonelyPlanet.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurDao extends JpaRepository<Utilisateur, Integer> {
    // c'est la récupération du champ qui est unique
    Optional <Utilisateur> findByPseudo(String pseudo);
}
