package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.PostCreateDto;
import com.openclassrooms.mddapi.Dto.PostDto;
import com.openclassrooms.mddapi.Dto.PostsResponseDto;
import com.openclassrooms.mddapi.Services.PostService;
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

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Get all posts")
    public ResponseEntity<PostsResponseDto> getAllPosts() {
        PostsResponseDto posts = postService.getAllPostsDto();
        return ResponseEntity.ok(posts);
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
            Integer authorId = Integer.valueOf(principal.getName());
            PostDto createdPost = postService.createPostDto(postRequest, authorId);
            return ResponseEntity.ok(createdPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer id, @Valid @RequestBody PostCreateDto postDetails) {
        PostDto updatedPost = postService.updatePostDto(id, postDetails);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    @Operation(summary = "Get user's personalized feed based on subscriptions", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostsResponseDto> getUserFeed(Principal principal) {
        Integer userId = Integer.valueOf(principal.getName());
        PostsResponseDto feedPosts = postService.getPostsByUserSubscriptionsDto(userId);
        return ResponseEntity.ok(feedPosts);
    }
}