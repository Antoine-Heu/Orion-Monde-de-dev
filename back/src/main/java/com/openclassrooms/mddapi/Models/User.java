package com.openclassrooms.mddapi.Models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotEmpty
    @Size(max = 50)
    String username;

    @Column(name="email", unique = true)
    @Email
    @NotEmpty
    @Size(max = 100)
    String email;

    @NotEmpty
    @Size(min = 8, max = 100)
    String password;

    @CreationTimestamp
    LocalDateTime createdAt;

    @CreationTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> subscribedTopics;
}
