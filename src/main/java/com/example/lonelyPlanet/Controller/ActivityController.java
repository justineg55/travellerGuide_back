package com.example.lonelyPlanet.Controller;

import com.example.lonelyPlanet.Model.Budget;
import com.example.lonelyPlanet.Model.Period;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="activity")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class ActivityController {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private String openingTimes;
    private String urlPicture;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('€','€€','€€€')")
    private Budget budget;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('morning','noon','afternoon','evening','night')")
    private Period period;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private CityController city;

    @ManyToMany
    @JoinTable(name = "activity_category",joinColumns = @JoinColumn(name = "id_activity"),inverseJoinColumns = @JoinColumn(name = "id_category"))
    Set<CategoryController> listCategory;



}
