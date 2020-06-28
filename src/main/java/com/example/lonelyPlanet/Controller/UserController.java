package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Category;
import com.example.lonelyPlanet.Model.User;
import com.example.lonelyPlanet.Security.JwtUtil;
import com.example.lonelyPlanet.Security.MonUserDetail;
import com.example.lonelyPlanet.Security.MonUserDetailService;
import com.example.lonelyPlanet.dao.CategoryDao;
import com.example.lonelyPlanet.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
public class UserController {
    private UserDao userDao;
    private AuthenticationManager authenticationManager;
    private MonUserDetailService userDetailsService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    private CategoryDao categoryDao;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          MonUserDetailService userDetailsService,
                          JwtUtil jwtUtil, UserDao userDao, PasswordEncoder passwordEncoder, CategoryDao categoryDao) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder=passwordEncoder;
        this.categoryDao = categoryDao;
    }

    //authentification d'un utilisateur pour génération d'un token
    @PostMapping("/authentification")
    public String authentification(@RequestBody User user) throws Exception {
        System.out.println("User trying to connect : " + user.toString());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(), user.getPassword()));

        } catch (BadCredentialsException e) {
            throw new Exception("Login ou mot de passe incorrect", e);
        }

        //ici on génère le token avec les infos du user
        final MonUserDetail userDetails = (MonUserDetail) userDetailsService
                .loadUserByUsername(user.getLogin());
        System.out.println("userDetails after loadUserByUsername : " + userDetails.toString());

        return jwtUtil.generateToken(userDetails);

        //ancienne version sans récupération de l'id de l'user
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(user.getLogin());
//        return jwtUtil.generateToken(userDetails);

    }

    //récupération d'un utilisateur par son id
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userDao.findById(id).orElse(null);
    }

    //Récupérer un user avec son login (et donc récup l'id de l'utilisateur)
    @GetMapping("/users/user-by-login/{login}")
    public ResponseEntity<User> getUtilisateur(@PathVariable String login) throws Exception {

        Optional<User> user = userDao.findByLogin(login);

        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    //Récupérer la liste des utilisateurs (réservé rôle admin)
    @GetMapping("/users")
    public List<User> getUsers() {
        return userDao.findAll();
    }

//    @PutMapping("/users")
//    public int saveUser(@RequestBody User user) {
//        //je retourne l'id du user enregistré en bdd (cela évite de voir le mdp)
//        return userDao.save(user).getId();
//    }

    //enregistrement d'un nouveau compte utilisateur avec login et mot de passe
    @PutMapping("/inscription")
    public ResponseEntity<String> inscription (@RequestBody User user){
        //il faut qu'il soit actif pour qu'il puisse faire les requetes donc dés son inscription on le set en actif
        user.setActif(true);
        //par défaut cet utilisateur n'a pas le rôle admin : isAdmin : false
        //si on veut créer un admin il faudra faire une méthode permettant de passer un utilisateur en admin : qui peut etre fait que par un admin


        //on vérifie que le login n'est pas déjà utilisé (si déjà présent en bdd)
        if(userDao.findByLogin(user.getLogin()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login existe déjà");
        }

        //on va set le password pour le crypter en bdd
        //on a ajouté une instance de passwordEncoder au niveau des propriétés de la classe
        //getPasswordEncoder est appelé dans MaconfigurationSécurité
        user.setPassword(passwordEncoder.encode(user.getPassword()));

//        return userDao.save(user).getId();
        //on ajoute l'user en bdd
        userDao.save(user);
        //on retourne un type ResponseEntity et qui renvoie l'id de l'user nouvellement créé quand tout se passe bien (login libre pour etre enregistré en bdd)
        return ResponseEntity.ok(user.getId().toString());
}



//gérer l'ajout d'image dans le server
    @GetMapping(value = "/users/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable int id) throws IOException {

        User user = userDao.findById(id).orElse(null);

        ClassPathResource imgFile = new ClassPathResource("private/images/avatars/" + user.getNomAvatar());
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }

    //mise à jour des préférences d'un utilisateur (budget et catégories)
    @PutMapping("/users/update")
    public ResponseEntity saveEditedUser(@RequestBody User userFront) {
        //récupération de l'utilisateur à mettre à jour
        User userDB = userDao.findById(userFront.getId())
                .orElse(null);

        if (userDB != null) {
            //on met à jour le budget dans l'user à mettre à jour
            userDB.setBudget(userFront.getBudget());

            // on récupère les catégories par type qui sont récupérées du front et on les met dans une liste
            Set<Category> categories = new HashSet<>();
            for(Category cat : userFront.getListCategory()){
                Category category = categoryDao.findByType(cat.getType());
                categories.add(category);
            }

            //on met à jour les catégories dans l'user à mettre à jour
            userDB.setListCategory(categories);
            //on enregistre l'user avec les changements effectués en bdd
            userDao.save(userDB);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();

    }

}
