package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.Category;
import com.example.lonelyPlanet.Model.Enum.Budget;
import com.example.lonelyPlanet.Model.Enum.Period;
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

    @PostMapping("/search")
    public List<Activity> getActivitiesAfterSearch(@RequestBody SearchActivitiesDto searchActivitiesDto) {
        int userId = searchActivitiesDto.getUserId();
        System.out.println("userid= "+userId);

        User user = userDao.findById(userId).orElse(null);
        // TODO : deal w/ NullPointer
        Budget budgetSearch = user.getBudget();

        List <Budget> acceptedBudgets=new ArrayList<>(Arrays.asList(Budget.values()));
        if(budgetSearch.ordinal()==1)
            acceptedBudgets.remove(2);
        else if (budgetSearch.ordinal()==0){
            acceptedBudgets.remove(2);
            acceptedBudgets.remove(1);
        }
        System.out.println("liste en budget : "+ acceptedBudgets);
        List<String> acceptedBudgetsToString;
        acceptedBudgetsToString = acceptedBudgets.stream().map(bud -> bud.toString()).collect(Collectors.toList());
        System.out.println("liste de strings budgets"+ acceptedBudgetsToString);

//        String budgetUser=budgetSearch.toString();

        Set<Category> categoriesSearch = user.getListCategory();
        List<String> categoriesList;
        categoriesList = categoriesSearch.stream().map(cat -> cat.getType()).collect(Collectors.toList());
        System.out.println("liste de categories :" + categoriesSearch);

        int cityId = searchActivitiesDto.getCityId();
        System.out.println("cityid : "+ cityId);

        List<String> periodsSearch = searchActivitiesDto.getPeriod();
        System.out.println("liste de period : "+ periodsSearch);

        List <Integer> getIdActivitiesResults =activityDao.getActivitiesAfterSearch(periodsSearch, cityId, acceptedBudgetsToString,categoriesList);
//        List <Integer> getIdActivitiesResults =activityDao.getActivitiesAfterSearch(categoriesSearch,periodsSearch, cityId);
        System.out.println("liste d'activités' : "+ getIdActivitiesResults);
        List<Activity> activitiesResults=new ArrayList<>();
        for(int id:getIdActivitiesResults){
            Activity activity=activityDao.findById(id).orElse(null);
            activitiesResults.add(activity);
        }
        return activitiesResults;

    }

}
