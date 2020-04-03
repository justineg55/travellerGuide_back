package com.example.lonelyPlanet.dao;

import com.example.lonelyPlanet.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category,Integer> {
}
