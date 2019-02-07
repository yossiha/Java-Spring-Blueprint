package horizon.time.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import horizon.time.persistence.model.Business;
import horizon.time.persistence.repo.BusinessRepository;

@Service
@Transactional
public class BusinessProfileService {
	@Autowired
	private BusinessRepository businessRepository;

	public Business getBySlug(String businessSlug) throws Exception {
		Optional<Business> optBusiness = businessRepository.findBySlug(businessSlug);

		if (optBusiness.isPresent()) {
			Business business = optBusiness.get();
			business.getMedia();
			// business.getProfile();
			// business.getMinisite();
			return business;
		} else {
			throw new Exception("Invalid object");
		}
	}

	public Business getRandomBusinesses(String businessSlug) {
		// TODO stud method
		return null;
	}
}