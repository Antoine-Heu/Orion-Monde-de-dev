package com.openclassrooms.mddapi.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "topic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotEmpty
    String title;

    @NotEmpty
    String description;

    @OneToMany(mappedBy = "topic")
    private List<Post> posts;

    @ManyToMany(mappedBy = "subscribedTopics")
    private List<User> subscribers;
}
