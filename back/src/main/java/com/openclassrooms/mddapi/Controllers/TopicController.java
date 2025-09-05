package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.TopicDto;
import com.openclassrooms.mddapi.Dto.TopicsResponseDto;
import com.openclassrooms.mddapi.Models.Topic;
import com.openclassrooms.mddapi.Services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    @Operation(summary = "Get all topics")
    public ResponseEntity<TopicsResponseDto> getAllTopics() {
        TopicsResponseDto topics = topicService.getAllTopicsDto();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get topic by id")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Integer id) {
        TopicDto topic = topicService.getTopicByIdDto(id);
        if (topic != null) {
            return ResponseEntity.ok(topic);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody Topic topic) {
        TopicDto createdTopic = topicService.createTopicDto(topic);
        return ResponseEntity.ok(createdTopic);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Integer id, @Valid @RequestBody Topic topicDetails) {
        TopicDto updatedTopic = topicService.updateTopicDto(id, topicDetails);
        if (updatedTopic != null) {
            return ResponseEntity.ok(updatedTopic);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteTopic(@PathVariable Integer id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscriptions")
    @Operation(summary = "Get user's subscribed topics", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicsResponseDto> getSubscribedTopics(Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        TopicsResponseDto subscribedTopics = topicService.getSubscribedTopicsDto(userId);
        return ResponseEntity.ok(subscribedTopics);
    }

    @PostMapping("/{id}/subscribe")
    @Operation(summary = "Subscribe to a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> subscribeToTopic(@PathVariable Integer id, Principal principal) {
        try {
            Integer userId = Integer.valueOf(principal.getName());
            topicService.subscribeToTopic(userId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/subscribe")
    @Operation(summary = "Unsubscribe from a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> unsubscribeFromTopic(@PathVariable Integer id, Principal principal) {
        try {
            Integer userId = Integer.valueOf(principal.getName());
            topicService.unsubscribeFromTopic(userId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
