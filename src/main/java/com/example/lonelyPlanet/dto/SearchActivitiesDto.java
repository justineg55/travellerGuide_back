package com.example.lonelyPlanet.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchActivitiesDto {
    int cityId;
    List<String> period;
    int userId;

}
