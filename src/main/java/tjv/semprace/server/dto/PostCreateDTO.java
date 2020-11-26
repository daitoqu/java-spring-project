package tjv.semprace.server.dto;
import java.util.List;

public class PostCreateDTO {
    private final Integer authorId;
    private final String content;

    public PostCreateDTO(Integer authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }
}
