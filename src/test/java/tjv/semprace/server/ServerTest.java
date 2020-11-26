package tjv.semprace.server;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tjv.semprace.server.dto.UserCreateDTO;
import tjv.semprace.server.dto.UserDTO;
import tjv.semprace.server.service.CommentService;
import tjv.semprace.server.service.PostService;
import tjv.semprace.server.service.UserService;

import java.util.Collections;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
public class ServerTest {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    UserDTO user1 = new UserDTO(1, "John", "Smith", Collections.emptyList());
    UserDTO user2 = new UserDTO(2, "James", "Smith", Collections.emptyList());
    UserDTO user3 = new UserDTO(3, "John", "Wayne", Collections.emptyList());

    @BeforeEach
    public void setUp() throws Exception {
        userService.create(new UserCreateDTO(
                user1.getFirstName(),
                user1.getLastName(),
                user1.getFriendsIds())
        );
        userService.create(new UserCreateDTO(
                user2.getFirstName(),
                user2.getLastName(),
                user2.getFriendsIds())
        );
        userService.create(new UserCreateDTO(
                user3.getFirstName(),
                user3.getLastName(),
                user3.getFriendsIds())
        );
    }

    @Test
    public void UserServiceTest() throws Exception {
        Optional<UserDTO> findUser = userService.findByIdAsDTO(user1.getId());
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(user1);
        findUser = userService.findByIdAsDTO(user2.getId());
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(user2);
        findUser = userService.findByIdAsDTO(user3.getId());
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(user3);

        UserDTO user2new = new UserDTO(2, user2.getFirstName(), "Smith the Second", Collections.emptyList());
        userService.update(user2new.getId(), new UserCreateDTO(user2new.getFirstName(), user2new.getLastName(), user2new.getFriendsIds()));
        findUser = userService.findByIdAsDTO(user2new.getId());
        assertThat(findUser.isEmpty()).isFalse();
        assertThat(findUser.get()).isEqualToComparingFieldByField(user2new);

        userService.delete(1);
        findUser = userService.findByIdAsDTO(1);
        assertThat(findUser.isEmpty()).isTrue();
    }
}
