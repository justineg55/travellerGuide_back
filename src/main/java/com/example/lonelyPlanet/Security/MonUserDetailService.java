package com.example.lonelyPlanet.Security;

import com.example.lonelyPlanet.Model.Utilisateur;
import com.example.lonelyPlanet.dao.UtilisateurDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MonUserDetailService implements UserDetailsService {
    @Autowired
    UtilisateurDao utilisateurDao;

    //ici on récupère l'utilisateur qui est en train de se loguer
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurDao.findByPseudo(userName)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Inconnu : " + userName));

        return new MonUserDetail(utilisateur);
    }
}
