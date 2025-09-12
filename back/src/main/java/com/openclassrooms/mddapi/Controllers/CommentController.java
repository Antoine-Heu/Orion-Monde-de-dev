package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.CommentCreateDto;
import com.openclassrooms.mddapi.Dto.CommentDto;
import com.openclassrooms.mddapi.Dto.CommentsResponseDto;
import com.openclassrooms.mddapi.Services.CommentService;
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

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @Operation(summary = "Get all comments")
    public ResponseEntity<CommentsResponseDto> getAllComments() {
        CommentsResponseDto comments = commentService.getAllCommentsDto();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get comment by id")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Integer id) {
        CommentDto comment = commentService.getCommentByIdDto(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new comment", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentCreateDto commentRequest, Principal principal) {
        try {
            Integer authorId = Integer.valueOf(principal.getName());
            CommentDto createdComment = commentService.createCommentDto(commentRequest, authorId);
            return ResponseEntity.ok(createdComment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a comment", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer id, @Valid @RequestBody CommentCreateDto commentDetails) {
        CommentDto updatedComment = commentService.updateCommentDto(id, commentDetails);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a comment", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get comments by post id")
    public ResponseEntity<CommentsResponseDto> getCommentsByPostId(@PathVariable Integer postId) {
        CommentsResponseDto comments = commentService.getCommentsByPostIdDto(postId);
        return ResponseEntity.ok(comments);
    }
}