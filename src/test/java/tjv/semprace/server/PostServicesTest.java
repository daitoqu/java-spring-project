package tjv.semprace.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import tjv.semprace.server.dto.*;
import tjv.semprace.server.service.CommentService;
import tjv.semprace.server.service.PostService;
import tjv.semprace.server.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class PostServicesTest {
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

    PostCreateDTO post1;
    int post1id;
    PostCreateDTO post2;
    int post2id;
    PostCreateDTO post3;
    int post3id;

    @BeforeEach
    public void setUp() throws Exception {
        user1 = new UserCreateDTO("John", "Smith", Collections.emptyList());
        user2 = new UserCreateDTO("James", "Smith", Collections.emptyList());
        user3 = new UserCreateDTO("John", "Wayne", Collections.emptyList());

        user1id = userService.create(user1).getId();
        user2id = userService.create(user2).getId();
        user3id = userService.create(user3).getId();

        post1 = new PostCreateDTO(user1id, "This is post 1 by user 1");
        post2 = new PostCreateDTO(user1id, "This is post 2 by user 1");
        post3 = new PostCreateDTO(user3id, "This is post 1 by user 3");

        post1id = postService.create(post1).getId();
        post2id = postService.create(post2).getId();
        post3id = postService.create(post3).getId();
    }

    @Test
    public void CreateRead() throws Exception {
        Optional<PostDTO> findPost = postService.findByIdAsDTO(post1id);
        assertThat(findPost.isEmpty()).isFalse();
        assertThat(findPost.get()).isEqualToComparingFieldByField(new PostDTO(post1id, post1));
        findPost = postService.findByIdAsDTO(post2id);
        assertThat(findPost.isEmpty()).isFalse();
        assertThat(findPost.get()).isEqualToComparingFieldByField(new PostDTO(post2id, post2));
        findPost = postService.findByIdAsDTO(post3id);
        assertThat(findPost.isEmpty()).isFalse();
        assertThat(findPost.get()).isEqualToComparingFieldByField(new PostDTO(post3id, post3));
    }

    @Test
    public void Update() throws Exception {
        PostCreateDTO post3new = new PostCreateDTO( post3.getAuthorId(), "This is post 1 by user 3 but edited");
        postService.update(post3id, post3new);
        Optional<PostDTO> findPost = postService.findByIdAsDTO(post3id);
        assertThat(findPost.isEmpty()).isFalse();
        assertThat(findPost.get()).isEqualToComparingFieldByField(new PostDTO(post3id, post3new));
    }

    @Test
    public void Delete() throws Exception {
        commentService.deleteByPost(post1id);
        postService.delete(post1id);
        Optional<PostDTO> findPost = postService.findByIdAsDTO(post1id);
        assertThat(findPost.isEmpty()).isTrue();
    }
}
