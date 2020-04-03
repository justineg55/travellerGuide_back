package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Budget;
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
public class CityController {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cityName;
    private String countryName;
    private String urlPicture;

    @OneToMany(mappedBy="city")
    List<ActivityController> listActivity = new ArrayList<>();




}
