package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.User;
import com.example.lonelyPlanet.Security.JwtUtil;
import com.example.lonelyPlanet.Security.MonUserDetailService;
import com.example.lonelyPlanet.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    private UserDao userDao;
    private AuthenticationManager authenticationManager;
    private MonUserDetailService userDetailsService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          MonUserDetailService userDetailsService,
                          JwtUtil jwtUtil, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder=passwordEncoder;
    }


    @PostMapping("/authentification")
    public String authentification(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(), user.getPassword()));

        } catch (BadCredentialsException e) {
            throw new Exception("Login ou mot de passe incorrect", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getLogin());
        return jwtUtil.generateToken(userDetails);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userDao.findById(id).orElse(null);
    }


    @GetMapping("/users")
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @PutMapping("/users")
    public int saveUser(@RequestBody User user) {
        //je retourne l'id du user enregistré en bdd (cela évite de voir le mdp)
        return userDao.save(user).getId();
    }

    @PutMapping("/inscription")
    public int inscription (@RequestBody User user){
        //il faut qu'il soit actif pour qu'il puisse faire les requetes donc dés son inscription on le set en actif
        user.setActif(true);
        //par défaut cet utilisateur n'a pas le rôle admin : isAdmin : false
        //si on veut créer un admin il faudra faire une méthode permettant de passer un utilisateur en admin : qui peut etre fait que par un admin

        //on va set le password pour le crypter en bdd
        //on a ajouté une instance de passwordEncoder au niveau des propriétés de la classe
        //getPasswordEncoder est appelé dans MaconfigurationSécurité
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userDao.save(user).getId();
}

}
