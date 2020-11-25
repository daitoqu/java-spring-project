package tjv.semprace.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tjv.semprace.server.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
