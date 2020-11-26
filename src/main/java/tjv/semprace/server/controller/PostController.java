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
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/all")
    List<PostDTO> all() {
        return postService.findAll();
    }

    @GetMapping("/post/{id}")
    PostDTO byId(@PathVariable int id) {
        return postService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/new_post")
    PostDTO save(@RequestBody PostCreateDTO post) throws Exception {
        return postService.create(post);
    }

    @PutMapping("/edit_post/{id}")
    PostDTO save(@PathVariable int id, @RequestBody PostCreateDTO post) throws Exception {
        return postService.update(id, post);
    }
}
