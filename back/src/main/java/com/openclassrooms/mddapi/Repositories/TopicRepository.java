package com.openclassrooms.mddapi.Repositories;

import com.openclassrooms.mddapi.Models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("SELECT t FROM Topic t JOIN t.subscribers s WHERE s.id = :userId")
    List<Topic> findTopicsBySubscriberId(@Param("userId") Integer userId);
}