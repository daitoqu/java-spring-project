package tjv.semprace.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tjv.semprace.server.dto.PostDTO;
import tjv.semprace.server.dto.UserCreateDTO;
import tjv.semprace.server.dto.UserCreateNewDTO;
import tjv.semprace.server.dto.UserDTO;
import tjv.semprace.server.service.CommentService;
import tjv.semprace.server.service.PostService;
import tjv.semprace.server.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final PostService postService;

    private final CommentService commentService;

    @Autowired
    public UserController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping()
    List<UserDTO> all() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    UserDTO byId(@PathVariable int id) {
        return userService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    UserDTO newUser(@RequestBody UserCreateNewDTO user) throws Exception {
        try {
            return userService.create(new UserCreateDTO(user.getFirstName(), user.getLastName(), Collections.emptyList()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    UserDTO editName(@PathVariable int id, @RequestBody UserCreateNewDTO user) throws Exception {
        Optional<UserDTO> optUser = userService.findByIdAsDTO(id);
        if (optUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        UserCreateDTO editedUser = new UserCreateDTO(user.getFirstName(), user.getLastName(), optUser.get().getFriendsIds());
        return userService.update(id, editedUser);
    }

    @PutMapping("/{id}/friends")
    void addFriend(@PathVariable int id, @RequestBody UserDTO friend) throws Exception {
        try {
            userService.addFriend(id, friend.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/friends")
    void delFriend(@PathVariable int id, @RequestBody UserDTO friend) throws Exception {
        try {
            userService.deleteFriend(id, friend.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    void delUser(@PathVariable int id) throws Exception {
        try {
            commentService.deleteByUser(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        List<PostDTO> userPosts = postService.findAllByUser(id);
        for (PostDTO post : userPosts) {
            commentService.deleteByPost(post.getId());
            postService.delete(post.getId());
        }

        try {
            userService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
