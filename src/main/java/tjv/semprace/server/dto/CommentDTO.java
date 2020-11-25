package tjv.semprace.server.dto;

public class CommentDTO {
    private final int id;
    private final Integer authorId;
    private final Integer rootPostId;
    private final String content;

    public CommentDTO(int id, Integer authorId, Integer rootPostId, String content) {
        this.id = id;
        this.authorId = authorId;
        this.rootPostId = rootPostId;
        this.content = content;
    }

    public int getId() {
        return id;
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
