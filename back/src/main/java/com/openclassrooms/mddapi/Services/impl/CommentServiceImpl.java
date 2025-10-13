package com.openclassrooms.mddapi.Services.impl;

import com.openclassrooms.mddapi.Dto.CommentCreateDto;
import com.openclassrooms.mddapi.Dto.CommentDto;
import com.openclassrooms.mddapi.Dto.CommentsResponseDto;
import com.openclassrooms.mddapi.Models.Comment;
import com.openclassrooms.mddapi.Models.Post;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.CommentRepository;
import com.openclassrooms.mddapi.Repositories.PostRepository;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import com.openclassrooms.mddapi.Services.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createCommentDto(CommentCreateDto commentRequest, Integer authorId) {
        Optional<User> author = userRepository.findById(authorId);
        Optional<Post> post = postRepository.findById(commentRequest.getPostId());

        if (author.isPresent() && post.isPresent()) {
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            comment.setPost(post.get());
            comment.setAuthor(author.get());

            Comment savedComment = commentRepository.save(comment);
            return toCommentDto(savedComment);
        } else {
            throw new RuntimeException("Author or Post not found");
        }
    }

    @Override
    public CommentsResponseDto getCommentsByPostIdDto(Integer postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return toCommentsResponseDto(comments);
    }

    private CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getPost().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername()
        );
    }

    private CommentsResponseDto toCommentsResponseDto(List<Comment> comments) {
        List<CommentDto> commentDtos = comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());

        CommentsResponseDto response = new CommentsResponseDto();
        response.setComments(commentDtos);
        return response;
    }
}