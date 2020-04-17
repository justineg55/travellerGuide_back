package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.User;
import com.example.lonelyPlanet.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    UserDao userDao;

    @Autowired
    public UserController(UserDao userDao){
        this.userDao=userDao;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id){
        return userDao.findById(id).orElse(null);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userDao.findAll();
    }

    @PutMapping("/users")
    public User saveVille(@RequestBody User user){
        return userDao.save(user);
    }



}
