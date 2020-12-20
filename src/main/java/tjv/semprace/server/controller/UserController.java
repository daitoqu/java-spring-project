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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    void delFiend(@PathVariable int id, @RequestBody UserDTO friend) throws Exception {
        try {
            userService.deleteFriend(id, friend.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    void delUser(@PathVariable int id) throws Exception {
        try {
            userService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
