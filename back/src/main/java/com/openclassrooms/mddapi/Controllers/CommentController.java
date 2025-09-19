package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.CommentCreateDto;
import com.openclassrooms.mddapi.Dto.CommentDto;
import com.openclassrooms.mddapi.Dto.CommentsResponseDto;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Services.CommentService;
import com.openclassrooms.mddapi.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new comment", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentCreateDto commentRequest, Principal principal) {
        try {
            User user = userService.getCurrentUser(principal.getName());
            CommentDto createdComment = commentService.createCommentDto(commentRequest, user.getId());
            return ResponseEntity.ok(createdComment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get comments by post id")
    public ResponseEntity<CommentsResponseDto> getCommentsByPostId(@PathVariable Integer postId) {
        CommentsResponseDto comments = commentService.getCommentsByPostIdDto(postId);
        return ResponseEntity.ok(comments);
    }
}