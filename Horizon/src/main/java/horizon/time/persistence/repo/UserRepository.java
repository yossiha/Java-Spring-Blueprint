package horizon.time.persistence.repo;

import horizon.time.persistence.model.User;
import horizon.time.persistence.model.enums.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	boolean existsByUsername(String username);

	List<User> findAllByStatus(UserStatus status);
}
