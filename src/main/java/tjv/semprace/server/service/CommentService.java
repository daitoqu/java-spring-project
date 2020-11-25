package tjv.semprace.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tjv.semprace.server.dto.CommentCreateDTO;
import tjv.semprace.server.dto.CommentDTO;
import tjv.semprace.server.entity.Comment;
import tjv.semprace.server.entity.Post;
import tjv.semprace.server.entity.User;
import tjv.semprace.server.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;


    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<CommentDTO> findAll() {
        return commentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Comment> findAllByIds(List<Integer> ids) {
        return commentRepository.findAllById(ids);
    }

    public Optional<Comment> findById(Integer id) {
        return commentRepository.findById(id);
    }

    public Optional<CommentDTO> findByIdAsDTO(Integer id) {
        return toDTO(findById(id));
    }

    @Transactional
    public CommentDTO create(CommentCreateDTO commentCreateDTO) throws Exception {
        Optional<User> author = userService.findById(commentCreateDTO.getAuthorId());
        if (author.isEmpty())
            throw new Exception("No such user found");

        Optional<Post> post = postService.findById(commentCreateDTO.getRootPostId());
        if (post.isEmpty())
            throw new Exception("No such post found");

        Comment comment = new Comment(author.get(), post.get(), commentCreateDTO.getContent());

        return toDTO(commentRepository.save(comment));
    }

    @Transactional
    public CommentDTO update(Integer id, CommentCreateDTO commentCreateDTO) throws Exception {
        Optional<Comment> oldComment = commentRepository.findById(id);
        if (oldComment.isEmpty())
            throw new Exception("No such comment found");
        Optional<User> newAuthor = userService.findById(commentCreateDTO.getAuthorId());
        if (newAuthor.isEmpty())
            throw new Exception("No such user found");
        Optional<Post> newPost = postService.findById(commentCreateDTO.getRootPostId());
        if (newPost.isEmpty())
            throw new Exception("No such user found");

        Comment comment = oldComment.get();
        comment.setAuthor(newAuthor.get());
        comment.setRootPost(newPost.get());
        comment.setContent(commentCreateDTO.getContent());

        return toDTO(comment);
    }

    private CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getAuthor().getId(),
                comment.getRootPost().getId(),
                comment.getContent()
        );
    }

    private Optional<CommentDTO> toDTO(Optional<Comment> comment) {
        if (comment.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(comment.get()));
    }
}
