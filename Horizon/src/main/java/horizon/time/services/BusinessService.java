package horizon.time.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import horizon.time.dto.BusinessDto;
import horizon.time.persistence.model.Business;
import horizon.time.persistence.model.User;
import horizon.time.persistence.repo.BusinessRepository;

@Service
@Transactional
public class BusinessService {
	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	// TODO take care of this exception
	public Business registerNewBusiness(final BusinessDto dto) throws Exception {
		if (!userHasBusiness(dto.getId())) // Support one business per user for now.
			throw new Exception("User has object");

		Business business = new Business();

		business.setDescriptionShort(dto.getDescriptionShort());
		business.setName(dto.getName());

		User user = userService.getLoggedUser();

		if (user == null) {
			throw new Exception("User not logged");
		} else {
			business.setUser(user);
			return businessRepository.save(business);
		}
	}

	public boolean ownedByUser(long id, long userId) {
		return businessRepository.existsByIdAndUsersId(id, userId);
	}

	public boolean userHasBusiness(long id) {
		return businessRepository.existsByUsersId(id);
	}

	public boolean ownedByLoggedUser(long id) {
		return businessRepository.existsByIdAndUsersId(id, userService.getLoggedUserId());
	}

	public Business ownerUpdateBusiness(final BusinessDto dto) throws Exception {
		Business business = ownerGetFirstBusiness(dto.getId());
		modelMapper.map(dto, business); // business is updated automatically through Hibernate

		return business;
	}

	// TODO
	@PostAuthorize("returnObject.users[0].id == @userService.getLoggedUserId()")
	public Business ownerGetFirstBusiness(long businessId) throws Exception {
		Optional<Business> optBusiness = businessRepository.findByIdAndUsersId(businessId,
				userService.getLoggedUserId());

		if (optBusiness.isPresent()) {
			return optBusiness.get();
		} else {
			throw new Exception("Invalid object");
		}
	}
}
