package com.example.lonelyPlanet.dao;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.Category;
import com.example.lonelyPlanet.Model.Enum.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ActivityDao extends JpaRepository<Activity,Integer> {
    @Query(nativeQuery = true,value="select a.id from traveller.activity a " +
            " INNER JOIN traveller.city ci on ci.id =a.id_city " +
            " INNER join traveller.activity_category ac on ac.id_activity =a.id " +
            " INNER join traveller.category c on c.id =ac.id_category " +
            " where ci.id =:cityId and c.type in (:categoriesSearch) and a.budget ='€' and a.period in(:periodsSearch) group by a.id ")
    List<Integer> getActivitiesAfterSearch(@Param("categoriesSearch") Set<Category> categoriesSearch, @Param("periodsSearch") List<String> periodsSearch, @Param("cityId") Integer cityId );
}
