package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.City;
import com.example.lonelyPlanet.Model.User;
import com.example.lonelyPlanet.dao.ActivityDao;
import com.example.lonelyPlanet.dao.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CityController {
    private CityDao cityDao;

    @Autowired
    public CityController(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    //Récupérer une ville par son id
    @GetMapping("/cities/{id}")
    public City getCity(@PathVariable int id) {
        return cityDao.findById(id).orElse(null);
    }

    //Récupérer la liste des villes
    @GetMapping("/cities")
    public List<City> getCities() {
        return cityDao.findAll();
    }

    //Enregistrer une nouvelle ville
    @PutMapping("/cities")
    public City saveCity(@RequestBody City city) {
        return cityDao.save(city);
    }
}
