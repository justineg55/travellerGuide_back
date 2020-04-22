package com.example.lonelyPlanet.Model;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="category")
@EntityListeners(AuditingEntityListener.class)
//@Data
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;

    @JsonIgnore
//    @ManyToMany(mappedBy = "listCategory")
    @ManyToMany
    @JoinTable(name = "user_category",joinColumns = @JoinColumn(name = "id_category",referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_user",referencedColumnName = "id"))
//    @EqualsAndHashCode.Exclude
    Set<User> listUser;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "activity_category",joinColumns = @JoinColumn(name = "id_category",referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_activity",referencedColumnName = "id"))
    Set<Activity> listActivity;



}
