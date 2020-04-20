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
    //requete personnalisée pour récupérer les id des activités en fonction de l'id de la ville renseigné par l'user, de la liste des catégories de l'user, du budget et des périodes renseignées par l'user
    //on groupe les activités par id car sinon on aura plusieurs fois la même activité qui ressortira (par exemple même activité avec une catégorie différente)
    //ce qui nous interesse de récupérer ce sont les id des activités pour ensuite pouvoir afficher les activités avec les champs qu'on veut
    //on met les champs à remplacer par une varaible en paramètre de la méthode avec @Param
    //pour les listes @Param fonctionne qu'avec une liste de String : d'où les conversions effectuées dans le ActivityController avant l'appel de cette méthode
    //méthode qui permet de récupérer une liste d'id activités pour une recherche effectuée par l'utilisateur
    @Query(nativeQuery = true,value="select a.id from traveller.activity a " +
            " INNER JOIN traveller.city ci on ci.id =a.id_city " +
            " INNER join traveller.activity_category ac on ac.id_activity =a.id " +
            " INNER join traveller.category c on c.id =ac.id_category " +
            " where ci.id =:cityId and a.budget in (:budgetsacceptedUser) and c.type in (:categoriesList) and a.period in (:periodsSearch) group by a.id ")
    List<Integer> getActivitiesAfterSearch(@Param("periodsSearch") List<String> periodsSearch, @Param("cityId") Integer cityId, @Param("budgetsacceptedUser") List<String> acceptedBudgetsToString, @Param("categoriesList") List<String> categoriesList );
}
