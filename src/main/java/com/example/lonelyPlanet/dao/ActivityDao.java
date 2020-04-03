package com.example.lonelyPlanet.dao;

import com.example.lonelyPlanet.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityDao extends JpaRepository<Activity,Integer> {
}
