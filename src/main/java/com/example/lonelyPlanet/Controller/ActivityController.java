package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.Category;
import com.example.lonelyPlanet.Model.Enum.Budget;
import com.example.lonelyPlanet.Model.User;
import com.example.lonelyPlanet.View.MyJsonView;
import com.example.lonelyPlanet.dao.ActivityDao;
import com.example.lonelyPlanet.dao.UserDao;
import com.example.lonelyPlanet.dto.SearchActivitiesDto;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ActivityController {
    private ActivityDao activityDao;
    private UserDao userDao;

    @Autowired
    public ActivityController(ActivityDao activityDao, UserDao userDao) {
        this.activityDao = activityDao;
        this.userDao = userDao;
    }

    @GetMapping("/activities/{id}")
    public Activity getActivity(@PathVariable int id) {
        return activityDao.findById(id).orElse(null);
    }

    @GetMapping("/activities")
    @JsonView(MyJsonView.VueActivity.class)
    public List<Activity> getActivities() {
        return activityDao.findAll();
    }

    @PutMapping("/activities")
    public int saveActivity(@RequestBody Activity activity) {
        return activityDao.save(activity).getId();
    }

    @PostMapping("/search")
    public List<Activity> getActivitiesAfterSearch(@RequestBody SearchActivitiesDto searchActivitiesDto) {
        //on récupère l'id de l'user connecté grâce au searchActivitiesDto
        int userId = searchActivitiesDto.getUserId();
        System.out.println("userid= " + userId);

        //on récupère l'objet user avec l'id récupéré avant
        User user = userDao.findById(userId).orElse(null);

        //on vérifie si l'user a bien été trouvé
        if (user != null) {
            //on récupère les préférences de l'user connecté : le budget puis les catégories
            Budget budgetSearch = user.getBudget();

            //ici on crée une liste qui comprend toutes les valeurs de l'enum budget : méthode .values()
            List<Budget> acceptedBudgets = new ArrayList<>(Arrays.asList(Budget.values()));
            //la méthode ordinal retourne la place du budget dans l'enum : budget min.ordinal=0, budget max.ordinal=2 car 3 valeurs dans l'enum budget
            //on veut que la recherche permette de cibler le buget choisi en tant que budget max
            //budget max de l'enum comprendra toutes les valeurs de la liste car acceptera toutes les activités de tous types de budget
            //pour les autres budget on fait un if pour enlever les budgets trop élevés de la liste acceptedBudgets
            if (budgetSearch.ordinal() == 1)
                acceptedBudgets.remove(2);
            else if (budgetSearch.ordinal() == 0) {
                acceptedBudgets.remove(2);
                acceptedBudgets.remove(1);
            }
            System.out.println("liste en budget : " + acceptedBudgets);
            List<String> acceptedBudgetsToString;
            //on convertit la liste des budgets en type String car notre requete prend des liste de String en paramètre
            acceptedBudgetsToString = acceptedBudgets.stream().map(bud -> bud.toString()).collect(Collectors.toList());
            System.out.println("liste de strings budgets" + acceptedBudgetsToString);

            //on récupère les préférences de l'user pour les catégories
            Set<Category> categoriesSearch = user.getListCategory();
            List<String> categoriesList;
            //on convertit cette liste en type String pour la requête qui prend en paramètre des listes de string
            categoriesList = categoriesSearch.stream().map(cat -> cat.getType()).collect(Collectors.toList());
            System.out.println("liste de categories :" + categoriesList);


            //on récupère la ville sélectionnée par l'utilisateur lors de la recherche
            int cityId = searchActivitiesDto.getCityId();
            System.out.println("cityid : " + cityId);

            //on récupère la les périodes selectionnées par l'user
            List<String> periodsSearch = searchActivitiesDto.getPeriod();
            System.out.println("liste de period : " + periodsSearch);

            //on fait appel à la méthode getActivitiesAfterSearch dans activityDao qui nous retourne une liste d'id d'activités
            List<Integer> getIdActivitiesResults = activityDao.getActivitiesAfterSearch(periodsSearch, cityId, acceptedBudgetsToString, categoriesList);
            System.out.println("liste d'activités' : " + getIdActivitiesResults);

            //à partir des id des activités on récupère chaque objet activity correspondant à l'id récupéré grâce à un forEach et on enferme les activités dans une liste
            List<Activity> activitiesResults = new ArrayList<>();
            for (int id : getIdActivitiesResults) {
                Activity activity = activityDao.findById(id).orElse(null);
                activitiesResults.add(activity);
            }
            return activitiesResults;

        } else {
            System.out.println("user n'a pas été trouvé");
            return null;
        }

    }



}
