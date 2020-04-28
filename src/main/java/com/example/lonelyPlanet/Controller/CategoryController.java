package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Category;
import com.example.lonelyPlanet.Model.City;
import com.example.lonelyPlanet.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {
    CategoryDao categoryDao;

    @Autowired
    public CategoryController(CategoryDao categoryDao){
        this.categoryDao=categoryDao;
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable int id){
        return categoryDao.findById(id).orElse(null);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(){
        return categoryDao.findAll();
    }

    @PutMapping("/categories")
    public Category saveCategory(@RequestBody Category category) {
        return categoryDao.save(category);
    }





}
