package tjv.semprace.server;

import com.google.gson.Gson;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tjv.semprace.server.controller.CommentController;
import tjv.semprace.server.dto.*;
import tjv.semprace.server.service.CommentService;
import tjv.semprace.server.service.PostService;
import tjv.semprace.server.service.UserService;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    UserCreateNewDTO user1;
    Integer user1id;
    UserCreateNewDTO user2;
    Integer user2id;
    UserCreateNewDTO user3;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @BeforeEach
    public void Create() throws Exception {
        user1 = new UserCreateNewDTO("John", "Smith");
        user2 = new UserCreateNewDTO("James", "Smith");
        user3 = new UserCreateNewDTO("John", "Wayne");

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user1))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        user1id = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class).getId();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user2))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        user2id = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class).getId();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user3))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        user3id = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class).getId();

        post1 = new PostCreateDTO(user1id, "This is post 1 by user 1");
        post2 = new PostCreateDTO(user1id, "This is post 2 by user 1");
        post3 = new PostCreateDTO(user3id, "This is post 1 by user 3");

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(post1))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        post1id = gson.fromJson(mvcResult.getResponse().getContentAsString(), PostDTO.class).getId();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(post2))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        post2id = gson.fromJson(mvcResult.getResponse().getContentAsString(), PostDTO.class).getId();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(post3))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        post3id = gson.fromJson(mvcResult.getResponse().getContentAsString(), PostDTO.class).getId();

        cmnt1 = new CommentCreateDTO(user1id, post1id, "Comment 1");
        cmnt2 = new CommentCreateDTO(user2id, post2id, "Comment 2");
        cmnt3 = new CommentCreateDTO(user1id, post3id, "Comment 3");

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(cmnt1))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        cmnt1id = gson.fromJson(mvcResult.getResponse().getContentAsString(), CommentDTO.class).getId();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(cmnt2))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        cmnt2id = gson.fromJson(mvcResult.getResponse().getContentAsString(), CommentDTO.class).getId();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(cmnt3))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        cmnt3id = gson.fromJson(mvcResult.getResponse().getContentAsString(), CommentDTO.class).getId();
    }

    @Test
    public void Read() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                .get("/comments/{id}", cmnt1id)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", CoreMatchers.is(cmnt1.getAuthorId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rootPostId", CoreMatchers.is(cmnt1.getRootPostId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(cmnt1.getContent())));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/comments/{id}", cmnt2id)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", CoreMatchers.is(cmnt2.getAuthorId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rootPostId", CoreMatchers.is(cmnt2.getRootPostId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(cmnt2.getContent())));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/comments/{id}", cmnt3id)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", CoreMatchers.is(cmnt3.getAuthorId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rootPostId", CoreMatchers.is(cmnt3.getRootPostId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(cmnt3.getContent())));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/comments/{id}", 100)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void Update() throws Exception {
        CommentCreateDTO cmnt3new = new CommentCreateDTO(user1id, post3id, "Comment 3 but different");
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/comments/{id}", cmnt3id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(cmnt3new))
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/comments/{id}", cmnt3id)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", CoreMatchers.is(cmnt3new.getAuthorId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rootPostId", CoreMatchers.is(cmnt3new.getRootPostId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(cmnt3new.getContent())));
    }

    @Test
    public void Delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/comments/{id}", cmnt1id)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/comments/{id}", cmnt1id)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
