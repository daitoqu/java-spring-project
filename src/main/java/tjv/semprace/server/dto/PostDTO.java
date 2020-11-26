package tjv.semprace.server.dto;
import java.util.List;

public class PostDTO {
    private final int id;
    private final Integer authorId;
    private final String content;

    public PostDTO(int id, Integer authorId, String content) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
    }

    public PostDTO(int id, PostCreateDTO postCreateDTO) {
        this.id = id;
        this.authorId = postCreateDTO.getAuthorId();
        this.content = postCreateDTO.getContent();
    }

    public int getId() {
        return id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }
}
