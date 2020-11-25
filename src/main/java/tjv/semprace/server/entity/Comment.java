package tjv.semprace.server.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post rootPost;

    @NotNull
    private String content;

    public Comment() {
    }

    public Comment(User author, Post rootPost, String content) {
        this.author = author;
        this.rootPost = rootPost;
        this.content = content;
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

    public Post getRootPost() {
        return rootPost;
    }

    public void setRootPost(Post rootPost) {
        this.rootPost = rootPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
