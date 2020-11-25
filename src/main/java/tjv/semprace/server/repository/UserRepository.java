package tjv.semprace.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tjv.semprace.server.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
