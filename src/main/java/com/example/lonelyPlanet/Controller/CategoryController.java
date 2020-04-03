package com.example.lonelyPlanet.Controller;

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
public class CategoryController {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;

    @ManyToMany(mappedBy = "listCategory")
    Set<UserController> listUser;

    @ManyToMany(mappedBy = "listCategory")
    Set<ActivityController> listActivity;



}
