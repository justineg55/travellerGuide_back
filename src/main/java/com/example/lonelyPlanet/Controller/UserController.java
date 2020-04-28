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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    private UserDao userDao;
    private AuthenticationManager authenticationManager;
    private MonUserDetailService userDetailsService;
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          MonUserDetailService userDetailsService,
                          JwtUtil jwtUtil, UserDao userDao) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
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
        return userDao.save(user).getId();
    }

}
