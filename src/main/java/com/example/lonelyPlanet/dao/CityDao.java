package com.example.lonelyPlanet.dao;

import com.example.lonelyPlanet.Model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDao extends JpaRepository<City,Integer> {
}
