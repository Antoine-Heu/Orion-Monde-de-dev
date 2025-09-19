package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.PostCreateDto;
import com.openclassrooms.mddapi.Dto.PostDto;
import com.openclassrooms.mddapi.Dto.PostsResponseDto;
import com.openclassrooms.mddapi.Models.Post;
import com.openclassrooms.mddapi.Models.Topic;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.PostRepository;
import com.openclassrooms.mddapi.Repositories.TopicRepository;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    private PostDto toPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getTopic().getId(),
                post.getTopic().getTitle(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername()
        );
    }

    private PostsResponseDto toPostsResponseDto(List<Post> posts) {
        List<PostDto> postDtos = posts.stream()
                .map(this::toPostDto)
                .collect(Collectors.toList());

        PostsResponseDto response = new PostsResponseDto();
        response.setPosts(postDtos);
        return response;
    }

    public PostDto getPostByIdDto(Integer id) {
        return postRepository.findById(id)
                .map(this::toPostDto)
                .orElse(null);
    }

    public PostDto createPostDto(PostCreateDto postRequest, Integer authorId) {
        Optional<User> author = userRepository.findById(authorId);
        Optional<Topic> topic = topicRepository.findById(postRequest.getTopicId());

        if (author.isPresent() && topic.isPresent()) {
            Post post = new Post();
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            post.setTopic(topic.get());
            post.setAuthor(author.get());

            Post savedPost = postRepository.save(post);
            return toPostDto(savedPost);
        } else {
            throw new RuntimeException("Author or Topic not found");
        }
    }

    public PostsResponseDto getPostsByUserSubscriptionsDto(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Integer> subscribedTopicIds = user.get().getSubscribedTopics().stream()
                    .map(Topic::getId)
                    .collect(Collectors.toList());
            
            if (!subscribedTopicIds.isEmpty()) {
                List<Post> posts = postRepository.findByTopicIdIn(subscribedTopicIds);
                return toPostsResponseDto(posts);
            }
        }
        return new PostsResponseDto();
    }
}
