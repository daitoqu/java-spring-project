package tjv.semprace.server.dto;
import java.util.List;

public class PostCreateDTO {
    private final Integer authorId;
    private final String content;
    private final List<Integer> commentsIds;

    public PostCreateDTO(Integer authorId, String content, List<Integer> commentsIds) {
        this.authorId = authorId;
        this.content = content;
        this.commentsIds = commentsIds;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public List<Integer> getCommentsIds() {
        return commentsIds;
    }
}
