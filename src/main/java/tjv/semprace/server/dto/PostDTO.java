package tjv.semprace.server.dto;
import java.util.List;

public class PostDTO {
    private final int id;
    private final Integer authorId;
    private final String content;
    private final List<Integer> commentsIds;

    public PostDTO(int id, Integer authorId, String content, List<Integer> commentsIds) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.commentsIds = commentsIds;
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

    public List<Integer> getCommentsIds() {
        return commentsIds;
    }
}
