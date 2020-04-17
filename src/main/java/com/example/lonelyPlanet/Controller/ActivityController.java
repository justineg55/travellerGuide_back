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
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
public class ActivityController {
    private ActivityDao activityDao;


    @Autowired
    public ActivityController(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    @GetMapping("/activities/{id}")
    public Activity getActivity(@PathVariable int id){
        return activityDao.findById(id).orElse(null);
    }

    @GetMapping("/activities")
    @JsonView(MyJsonView.VueActivity.class)
    public List<Activity> getActivities(){
        return activityDao.findAll();
    }

//    @PostMapping("/recherche")
//    public List<Activity> getActivitiesAfterSearch(@RequestBody SearchActivitiesDto searchActivitiesDto){
//        int userId=searchActivitiesDto.getUserId();
//        UserDao userDao;
//        User user=userDao.findById(userId);
//        Budget budgetSearch=user.getBudget();
//        Set<Category> categoriesSearch=user.getListCategory();
//        int cityId=searchActivitiesDto.getCityId();
//        List <Period> periodSearch=searchActivitiesDto.getPeriod()

//    }

}
