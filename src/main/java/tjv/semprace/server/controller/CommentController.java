package tjv.semprace.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tjv.semprace.server.dto.CommentCreateDTO;
import tjv.semprace.server.dto.CommentDTO;
import tjv.semprace.server.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    List<CommentDTO> all() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    CommentDTO byId(@PathVariable int id) {
        return commentService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    CommentDTO newComment(@RequestBody CommentCreateDTO comment) throws Exception {
        try {
            return commentService.create(comment);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    CommentDTO editComment(@PathVariable int id, @RequestBody CommentCreateDTO comment) throws Exception {
        try {
            return commentService.update(id, comment);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteComment(@PathVariable int id) throws Exception {
        try {
            commentService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
