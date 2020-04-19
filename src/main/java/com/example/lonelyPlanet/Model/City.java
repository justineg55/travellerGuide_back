package com.example.lonelyPlanet.Model;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.View.MyJsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="city")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(MyJsonView.VueActivity.class)
    private Integer id;

    @JsonView(MyJsonView.VueActivity.class)
    private String cityName;

    @JsonView(MyJsonView.VueActivity.class)
    private String countryName;

    @JsonView(MyJsonView.VueActivity.class)
    private String urlPicture;

    @JsonIgnore
    @OneToMany(mappedBy="city")
    List<Activity> listActivity = new ArrayList<>();




}
