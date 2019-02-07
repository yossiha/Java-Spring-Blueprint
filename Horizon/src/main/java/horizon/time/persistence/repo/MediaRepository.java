package horizon.time.persistence.repo;

import horizon.time.persistence.model.Media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	Optional<Media> findByObjectIdAndUserIdAndObjectTypeAndObjectTypePart(Long businessId, long loggedUserId,
			String string, String type);

	Optional<Media> findByIdAndUserId(Long imageId, long userId);
}
