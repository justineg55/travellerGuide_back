package com.example.lonelyPlanet.Model;

import com.example.lonelyPlanet.Model.Enum.Budget;
import com.example.lonelyPlanet.View.MyJsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(MyJsonView.VueActivity.class)
    private Integer id;

    @JsonView(MyJsonView.VueActivity.class)
    private String name;

    @JsonView(MyJsonView.VueActivity.class)
    private String address;

    @JsonView(MyJsonView.VueActivity.class)
    private String openingTimes;

    @JsonView(MyJsonView.VueActivity.class)
    private String urlPicture;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('€','€€','€€€')")
    @JsonView(MyJsonView.VueActivity.class)
    private Budget budget;

    //class Enum Period supprimé car activité peut avoir plusieurs périodes donc création d'une table Period et une table intermédiaire activity_period
//    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "ENUM('morning','noon','afternoon','evening','night')")
//    @JsonView(MyJsonView.VueActivity.class)
//    private Period period;

    @ManyToOne
    @JoinColumn(name = "id_city")
    @JsonView(MyJsonView.VueActivity.class)
    private City city;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "activity_category",joinColumns = @JoinColumn(name = "id_activity"),inverseJoinColumns = @JoinColumn(name = "id_category"))
    Set<Category> listCategory;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "activity_period",joinColumns = @JoinColumn(name = "id_activity"),inverseJoinColumns = @JoinColumn(name = "id_period"))
    Set<Period> listPeriod;


}
