package com.example.lonelyPlanet.Model;

import com.example.lonelyPlanet.Model.Activity;
import com.example.lonelyPlanet.Model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="category")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;

    @ManyToMany(mappedBy = "listCategory")
    Set<User> listUser;

    @ManyToMany(mappedBy = "listCategory")
    Set<Activity> listActivity;



}
