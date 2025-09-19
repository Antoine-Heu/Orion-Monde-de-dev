package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.TopicResponseDto;
import com.openclassrooms.mddapi.Dto.TopicsResponseDto;
import com.openclassrooms.mddapi.Models.Topic;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Services.TopicService;
import com.openclassrooms.mddapi.Services.UserService;
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
    private final UserService userService;

    public TopicController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all topics")
    public ResponseEntity<TopicsResponseDto> getAllTopics() {
        TopicsResponseDto topics = topicService.getAllTopicsDto();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get topic by id")
    public ResponseEntity<TopicResponseDto> getTopicById(@PathVariable Integer id) {
        TopicResponseDto topic = topicService.getTopicByIdDto(id);
        if (topic != null) {
            return ResponseEntity.ok(topic);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicResponseDto> createTopic(@Valid @RequestBody Topic topic) {
        TopicResponseDto createdTopic = topicService.createTopicDto(topic);
        return ResponseEntity.ok(createdTopic);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicResponseDto> updateTopic(@PathVariable Integer id, @Valid @RequestBody Topic topicDetails) {
        TopicResponseDto updatedTopic = topicService.updateTopicDto(id, topicDetails);
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
        User user = userService.getCurrentUser(principal.getName());
        TopicsResponseDto subscribedTopics = topicService.getSubscribedTopicsDto(user.getId());
        return ResponseEntity.ok(subscribedTopics);
    }

    @PostMapping("/{id}/subscribe")
    @Operation(summary = "Subscribe to a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> subscribeToTopic(@PathVariable Integer id, Principal principal) {
        try {
            User user = userService.getCurrentUser(principal.getName());
            topicService.subscribeToTopic(user.getId(), id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/subscribe")
    @Operation(summary = "Unsubscribe from a topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> unsubscribeFromTopic(@PathVariable Integer id, Principal principal) {
        try {
            User user = userService.getCurrentUser(principal.getName());
            topicService.unsubscribeFromTopic(user.getId(), id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
