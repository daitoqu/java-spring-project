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
    UserDTO save(@RequestBody UserCreateNewDTO user) throws Exception {
        return userService.create(new UserCreateDTO(user.getFirstName(), user.getLastName(), Collections.emptyList()));
    }

    @PutMapping("/edit_user/{id}")
    UserDTO save(@PathVariable int id, @RequestBody UserCreateDTO user) throws Exception {
        return userService.update(id, user);
    }
}
