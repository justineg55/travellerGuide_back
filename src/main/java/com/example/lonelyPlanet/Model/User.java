package com.example.lonelyPlanet.Model;

import com.example.lonelyPlanet.Model.Enum.Budget;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="user")
@EntityListeners(AuditingEntityListener.class)
//lombok : @Data : ajout des setters et des getters, aussi toString... et on ajoute le constructeur sans argument noArgsConstructor: on ne les voit pas mais sont créés
@NoArgsConstructor
@Getter
@Setter
//@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String login;

    private String password;
    private boolean actif;

    private boolean isAdmin;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('€','€€','€€€')")
    private Budget budget;


    @ManyToMany
    @JoinTable(name = "user_category",joinColumns = @JoinColumn(name = "id_user",referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_category",referencedColumnName = "id"))
    Set<Category> listCategory;




}
