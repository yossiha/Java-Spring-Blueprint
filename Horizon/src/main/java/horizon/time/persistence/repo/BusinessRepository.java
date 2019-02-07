package horizon.time.persistence.repo;

import horizon.time.persistence.model.Business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
	List<Business> findByName(String name);

	List<Business> findByUsersId(Long userId);

	Optional<Business> findBySlug(String businessSlug);

	boolean existsByUsersId(Long userId);

	boolean existsByIdAndUsersId(long id, long userId);

	Optional<Business> findByIdAndUsersId(long id, long userId);
}
