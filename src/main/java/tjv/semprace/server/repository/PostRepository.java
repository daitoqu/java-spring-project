package tjv.semprace.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tjv.semprace.server.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
