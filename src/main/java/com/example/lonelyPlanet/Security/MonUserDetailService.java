package com.example.lonelyPlanet.Security;

import com.example.lonelyPlanet.Model.User;
import com.example.lonelyPlanet.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MonUserDetailService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    //ici on récupère l'utilisateur qui est en train de se loguer
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDao.findByLogin(userName)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Inconnu : " + userName));

        return new MonUserDetail(user);
    }
}
