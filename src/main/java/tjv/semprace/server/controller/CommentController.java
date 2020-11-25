package tjv.semprace.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tjv.semprace.server.dto.CommentCreateDTO;
import tjv.semprace.server.dto.CommentDTO;
import tjv.semprace.server.service.CommentService;

import java.util.List;

public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comment/all")
    List<CommentDTO> all() {
        return commentService.findAll();
    }

    @GetMapping("/comment/{id}")
    CommentDTO byId(@PathVariable int id) {
        return commentService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/comment")
    CommentDTO save(@RequestBody CommentCreateDTO comment) throws Exception {
        return commentService.create(comment);
    }

    @PutMapping("/comment/{id}")
    CommentDTO save(@PathVariable int id, @RequestBody CommentCreateDTO comment) throws Exception {
        return commentService.update(id, comment);
    }
}
