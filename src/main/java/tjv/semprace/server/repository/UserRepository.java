package tjv.semprace.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tjv.semprace.server.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
