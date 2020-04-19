package com.example.lonelyPlanet.dto;

import com.example.lonelyPlanet.Model.Enum.Period;
import lombok.Data;

import java.util.List;

@Data
public class SearchActivitiesDto {
    int cityId;
//    List<Period> period;
    List<String> period;
    int userId;

}
