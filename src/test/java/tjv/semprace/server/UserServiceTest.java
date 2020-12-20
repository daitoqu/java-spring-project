package tjv.semprace.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import tjv.semprace.server.dto.*;
import tjv.semprace.server.entity.Post;
import tjv.semprace.server.service.CommentService;
import tjv.semprace.server.service.PostService;
import tjv.semprace.server.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    UserCreateDTO user1;
    Integer user1id;
    UserCreateDTO user2;
    Integer user2id;
    UserCreateDTO user3;
    Integer user3id;

    @BeforeEach
    public void setUp() throws Exception {
        user1 = new UserCreateDTO("John", "Smith", Collections.emptyList());
        user2 = new UserCreateDTO("James", "Smith", Collections.emptyList());
        user3 = new UserCreateDTO("John", "Wayne", Collections.emptyList());

        user1id = userService.create(user1).getId();
        user2id = userService.create(user2).getId();
        user3id = userService.create(user3).getId();
    }

    @Test
    public void CreateRead() {
        Optional<UserDTO> findUser = userService.findByIdAsDTO(user1id);
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(new UserDTO(user1id, user1));
        findUser = userService.findByIdAsDTO(user2id);
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(new UserDTO(user2id, user2));
        findUser = userService.findByIdAsDTO(user3id);
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(new UserDTO(user3id, user3));
    }

    @Test
    public void Update() throws Exception {
        UserCreateDTO user2new = new UserCreateDTO(user2.getFirstName(), "Smith the Second", Collections.emptyList());
        userService.update(user2id, user2new);
        Optional<UserDTO> findUser = userService.findByIdAsDTO(user2id);
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(new UserDTO(user2id, user2new));
    }

    @Test
    public void Delete() throws Exception {
        userService.delete(user1id);
        Optional<UserDTO> findUser = userService.findByIdAsDTO(user1id);
        assertThat(findUser.isEmpty()).isTrue();
    }
}
