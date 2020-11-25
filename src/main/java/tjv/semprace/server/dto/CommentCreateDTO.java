package tjv.semprace.server.dto;

public class CommentCreateDTO {
    private final Integer authorId;
    private final Integer rootPostId;
    private final String content;

    public CommentCreateDTO(Integer authorId, Integer rootPostId, String content) {
        this.authorId = authorId;
        this.rootPostId = rootPostId;
        this.content = content;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public Integer getRootPostId() {
        return rootPostId;
    }

    public String getContent() {
        return content;
    }
}
