package com.openclassrooms.mddapi.Models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "topic")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotEmpty
    String title;

    @NotEmpty
    String description;

    @CreationTimestamp
    LocalDateTime createdAt;
}
