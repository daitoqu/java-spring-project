package tjv.semprace.server.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @NotNull
    private String content;

    @OneToMany(mappedBy = "rootPost")
    private List<Comment> comments;

    public Post() {
    }

    public Post(User author, String content, List<Comment> comments) {
        this.author = author;
        this.content = content;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
