package tjv.semprace.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tjv.semprace.server.dto.UserCreateDTO;
import tjv.semprace.server.dto.UserCreateNewDTO;
import tjv.semprace.server.dto.UserDTO;
import tjv.semprace.server.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/all")
    List<UserDTO> all() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    UserDTO byId(@PathVariable int id) {
        return userService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/new_user")
    UserDTO newUser(@RequestBody UserCreateNewDTO user) throws Exception {
        return userService.create(new UserCreateDTO(user.getFirstName(), user.getLastName(), Collections.emptyList()));
    }

    @PutMapping("/edit_name/{id}")
    UserDTO editName(@PathVariable int id, @RequestBody UserCreateNewDTO user) throws Exception {
        Optional<UserDTO> optUser = userService.findByIdAsDTO(id);
        if (optUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        UserCreateDTO editedUser = new UserCreateDTO(user.getFirstName(), user.getLastName(), optUser.get().getFriendsIds());
        return userService.update(id, editedUser);
    }

    @PutMapping("/add_friend/{user1id}/{user2id}")
    void addFriend(@PathVariable int user1id, @PathVariable int user2id) throws Exception {
        userService.addFriend(user1id, user2id);
    }

    @PutMapping("/del_friend/{user1id}/{user2id}")
    void delFiend(@PathVariable int user1id, @PathVariable int user2id) throws Exception {
        userService.deleteFriend(user1id, user2id);
    }
}
