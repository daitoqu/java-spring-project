package tjv.semprace.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tjv.semprace.server.dto.PostCreateDTO;
import tjv.semprace.server.dto.PostDTO;
import tjv.semprace.server.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
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
    PostDTO save(@RequestBody PostCreateDTO post) throws Exception {
        try {
            return postService.create(post);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    PostDTO save(@PathVariable int id, @RequestBody PostCreateDTO post) throws Exception {
        try {
            return postService.update(id, post);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) throws Exception {
        try {
            postService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
