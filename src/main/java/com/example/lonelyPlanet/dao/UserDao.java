package com.example.lonelyPlanet.dao;

import com.example.lonelyPlanet.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {

}
