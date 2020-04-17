package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.City;
import com.example.lonelyPlanet.dao.ActivityDao;
import com.example.lonelyPlanet.dao.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CityController {
    private CityDao cityDao;

    @Autowired
    public CityController(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @GetMapping("/cities/{id}")
    public City getCity(@PathVariable int id) {
        return cityDao.findById(id).orElse(null);
    }

    @GetMapping("/cities")
    public List<City> getCities() {
        return cityDao.findAll();
    }
}
