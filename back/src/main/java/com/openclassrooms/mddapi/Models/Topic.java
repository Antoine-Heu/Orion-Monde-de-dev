package com.openclassrooms.mddapi.Models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotEmpty
    @Size(min = 3, message = "Le titre doit contenir au moins 3 caractères")
    String title;

    @NotEmpty
    @Size(min = 10, message = "La description doit contenir au moins 10 caractères")
    String description;

    @CreationTimestamp
    LocalDateTime createdAt;
}
