package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.PostCreateDto;
import com.openclassrooms.mddapi.Dto.PostDto;
import com.openclassrooms.mddapi.Dto.PostsResponseDto;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Services.PostService;
import com.openclassrooms.mddapi.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get post by id")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer id) {
        PostDto post = postService.getPostByIdDto(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new post", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostCreateDto postRequest, Principal principal) {
        try {
            User user = userService.getCurrentUser(principal.getName());
            PostDto createdPost = postService.createPostDto(postRequest, user.getId());
            return ResponseEntity.ok(createdPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/feed")
    @Operation(summary = "Get user's personalized feed based on subscriptions", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostsResponseDto> getUserFeed(Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        PostsResponseDto feedPosts = postService.getPostsByUserSubscriptionsDto(user.getId());
        return ResponseEntity.ok(feedPosts);
    }
}