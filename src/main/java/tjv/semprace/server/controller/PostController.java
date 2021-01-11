package tjv.semprace.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tjv.semprace.server.dto.PostCreateDTO;
import tjv.semprace.server.dto.PostDTO;
import tjv.semprace.server.entity.Post;
import tjv.semprace.server.service.CommentService;
import tjv.semprace.server.service.PostService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping()
    List<PostDTO> all() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    PostDTO byId(@PathVariable int id) {
        return postService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    PostDTO create(@RequestBody PostCreateDTO post) throws Exception {
        try {
            return postService.create(post);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    PostDTO edit(@PathVariable int id, @RequestBody PostCreateDTO post) throws Exception {
        try {
            return postService.update(id, post);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) throws Exception {
        Optional<Post> post = postService.findById(id);
        if (post.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.");
        commentService.deleteByPost(post.get().getId());
        try {
            postService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
