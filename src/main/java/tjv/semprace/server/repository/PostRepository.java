package tjv.semprace.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tjv.semprace.server.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
