package com.example.lonelyPlanet.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "period")
@EntityListeners(AuditingEntityListener.class)
//@Data
@Getter
@Setter
@NoArgsConstructor
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String moment;

    @JsonIgnore
    @ManyToMany(mappedBy = "listPeriod")
//    @JoinTable(name = "activity_period", joinColumns = @JoinColumn(name = "id_period", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_activity", referencedColumnName = "id"))
    Set<Activity> listActivity;


}
