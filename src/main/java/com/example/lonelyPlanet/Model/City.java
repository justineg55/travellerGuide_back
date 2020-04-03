package com.example.lonelyPlanet.Model;

import com.example.lonelyPlanet.Model.Activity;
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
    private Integer id;
    private String cityName;
    private String countryName;
    private String urlPicture;

    @OneToMany(mappedBy="city")
    List<Activity> listActivity = new ArrayList<>();




}
