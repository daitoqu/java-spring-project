package tjv.semprace.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tjv.semprace.server.dto.PostCreateDTO;
import tjv.semprace.server.dto.PostDTO;
import tjv.semprace.server.entity.Comment;
import tjv.semprace.server.entity.Post;
import tjv.semprace.server.entity.User;
import tjv.semprace.server.repository.PostRepository;
import tjv.semprace.server.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<PostDTO> findAll() {
        return postRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> findAllByUser(Integer userId) {
        List<PostDTO> allPosts = findAll();
        List<PostDTO> postByUser = Collections.emptyList();
        for (PostDTO post : allPosts) {
            if (post.getAuthorId().equals(userId)) {
                postByUser.add(post);
            }
        }
        return postByUser;
    }

    @Transactional
    public void deleteByUser(Integer userId) throws Exception {
        List<PostDTO> allPosts = findAll();
        for (PostDTO post : allPosts) {
            if (post.getAuthorId().equals(userId)) {
                delete(post.getId());
            }
        }
    }

    public List<Post> findAllByIds(List<Integer> ids) {
        return postRepository.findAllById(ids);
    }

    public Optional<Post> findById(Integer id) {
        return postRepository.findById(id);
    }

    public Optional<PostDTO> findByIdAsDTO(Integer id) {
        return toDTO(findById(id));
    }

    @Transactional
    public PostDTO create(PostCreateDTO postCreateDTO) throws Exception {
        Optional<User> author = userService.findById(postCreateDTO.getAuthorId());
        if (author.isEmpty())
            throw new Exception("No such user found");

        Post post = new Post(author.get(), postCreateDTO.getContent());

        return toDTO(postRepository.save(post));
    }

    @Transactional
    public PostDTO update(Integer id, PostCreateDTO postCreateDTO) throws Exception {
        Optional<Post> oldPost = postRepository.findById(id);
        if (oldPost.isEmpty())
            throw new Exception("No such post found");
        Optional<User> newAuthor = userService.findById(postCreateDTO.getAuthorId());
        if (newAuthor.isEmpty())
            throw new Exception("No such user found");
        Post post = oldPost.get();
        User author = newAuthor.get();

        post.setAuthor(author);
        post.setContent(postCreateDTO.getContent());

        return toDTO(post);
    }

    @Transactional
    public void delete(Integer id) throws Exception {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty())
            throw new Exception("No such post found");
        postRepository.delete(post.get());
    }

    private PostDTO toDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getAuthor().getId(),
                post.getContent());
    }

    private Optional<PostDTO> toDTO(Optional<Post> post) {
        if (post.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(post.get()));
    }
}
