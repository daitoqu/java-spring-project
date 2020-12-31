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
public class CommentServicesTest {
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

    CommentCreateDTO cmnt1;
    int cmnt1id;
    CommentCreateDTO cmnt2;
    int cmnt2id;
    CommentCreateDTO cmnt3;
    int cmnt3id;

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

        cmnt1 = new CommentCreateDTO(user1id, post1id, "Comment 1");
        cmnt2 = new CommentCreateDTO(user2id, post2id, "Comment 2");
        cmnt3 = new CommentCreateDTO(user1id, post3id, "Comment 3");

        cmnt1id = commentService.create(cmnt1).getId();
        cmnt2id = commentService.create(cmnt2).getId();
        cmnt3id = commentService.create(cmnt3).getId();
    }

    @Test
    public void CreateRead() throws Exception {
        Optional<CommentDTO> findComment = commentService.findByIdAsDTO(cmnt1id);
        assertThat(findComment.isEmpty()).isFalse();
        assertThat(findComment.get()).isEqualToComparingFieldByField(new CommentDTO(cmnt1id, cmnt1));
        findComment = commentService.findByIdAsDTO(cmnt2id);
        assertThat(findComment.isEmpty()).isFalse();
        assertThat(findComment.get()).isEqualToComparingFieldByField(new CommentDTO(cmnt2id, cmnt2));
        findComment = commentService.findByIdAsDTO(cmnt3id);
        assertThat(findComment.isEmpty()).isFalse();
        assertThat(findComment.get()).isEqualToComparingFieldByField(new CommentDTO(cmnt3id, cmnt3));
    }

    @Test
    public void Update() throws Exception {
        CommentCreateDTO cmnt3new = new CommentCreateDTO(user1id, post3id, "Comment 3 but different");
        commentService.update(cmnt3id, cmnt3new);
        Optional<CommentDTO> findComment = commentService.findByIdAsDTO(cmnt3id);
        assertThat(findComment.isEmpty()).isFalse();
        assertThat(findComment.get()).isEqualToComparingFieldByField(new CommentDTO(cmnt3id, cmnt3new));
    }

    @Test
    public void Delete() throws Exception {
        commentService.delete(cmnt1id);
        Optional<CommentDTO> findComment = commentService.findByIdAsDTO(cmnt1id);
        assertThat(findComment.isEmpty()).isTrue();
    }
}
