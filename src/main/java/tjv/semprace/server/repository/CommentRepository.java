package tjv.semprace.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tjv.semprace.server.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
