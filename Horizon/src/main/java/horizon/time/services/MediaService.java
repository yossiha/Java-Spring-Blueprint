package horizon.time.services;

import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import horizon.time.components.StorageComponent;
import horizon.time.persistence.model.Media;
import horizon.time.persistence.repo.BusinessRepository;
import horizon.time.persistence.repo.MediaRepository;

@Service
@Transactional
public class MediaService {
	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private StorageComponent storageComponent;

	@Autowired
	private MediaRepository mediaRepository;

// private static final String[] BUSINESS_IMAGE_TYPES = {"cover", "logo"};
//	private static final Map<String><Int> BUSINESS = {""};
	/**
	 * This variable holds the object type parts for the specific object type.
	 * Map<String,int[]> X_IMAGE_TYPES = MAP String = ImageTypePart ("logo",
	 * "profile", "cover", etc..) int[] = { width, height }
	 */
	private static final Map<String, int[]> BUSINESS_IMAGE_TYPES = Map
			.ofEntries(Map.entry("cover", new int[] { 1200, 400 }), Map.entry("logo", new int[] { 200, 200 }));

	/**
	 * Set Image in the database for a business entity
	 * 
	 * @param file       uploaded file
	 * @param type       object type part to set in the database (object_type_part),
	 *                   object type is business.
	 * @param businessId id of the object type (represented as object_id)
	 * @return
	 * @throws Exception
	 */
	public boolean setImage(MultipartFile file, String type, Long businessId) throws Exception {
		// Since object_id is a business, must make sure that the business exists.
		boolean businessExists = businessRepository.existsByIdAndUsersId(businessId, userService.getLoggedUserId());

		if (!BUSINESS_IMAGE_TYPES.containsKey(type))
			throw new Exception("Invalid media part type for Entity");

		if (businessExists) {
			try {
				storageComponent.init();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Map<String, String> fileMap = storageComponent.createImageFile(file, BUSINESS_IMAGE_TYPES.get(type));
			Media media;

			Optional<Media> optMedia = mediaRepository.findByObjectIdAndUserIdAndObjectTypeAndObjectTypePart(businessId,
					userService.getLoggedUserId(), "business", type);

			String oldFilename = "";

			if (optMedia.isPresent()) {
				media = optMedia.get();
				oldFilename = media.getFilename();
				media.setFilename(fileMap.get("filename"));
				media.setExt(fileMap.get("ext"));
				media.setMimetype(file.getContentType());
				media.setDirectory(fileMap.get("baseUploadFolderMediaUserDate"));
				media.setDirectoryPrefix(fileMap.get("baseUploadFolderMedia"));
			} else {
				media = new Media("business", type, businessId, userService.getLoggedUser(), file.getContentType(),
						fileMap.get("filename"), fileMap.get("ext"), fileMap.get("baseUploadFolderMediaUserDate"),
						fileMap.get("baseUploadFolderMedia"));
			}

			media = mediaRepository.save(media);

			if (!oldFilename.equals("") // not empty
					&& !oldFilename.equals(media.getFilename()) // object was saved
					&& media.getFilename().equals(fileMap.get("filename"))) { // new filename was saved
				storageComponent.deleteFile(fileMap.get("baseUploadFolderMedia") + "/"
						+ fileMap.get("baseUploadFolderMediaUserDate") + "/" + oldFilename);
				return true;
			} else
				return false;
		} else {
			throw new Exception("Invalid object");
		}
	}

	public void deleteImage(Long imageId) throws Exception {
		Optional<Media> optMedia = mediaRepository.findByIdAndUserId(imageId, userService.getLoggedUserId());
		if (optMedia.isPresent()) {
			Media media = optMedia.get();

			switch (media.getObjectType()) {
			case "business":
				if (!businessRepository.existsByIdAndUsersId(media.getObjectId(), userService.getLoggedUserId()))
					throw new Exception("Permission Denied");
				break;
			default:
				throw new Exception("Unknown Object");
			}

			try {
				String filePath = media.getDirectoryPrefix() + "/" + media.getDirectory() + "/" + media.getFilename();
				mediaRepository.delete(media);
				storageComponent.deleteFile(filePath);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Object Not Deleted");
			}
		} else
			throw new Exception("Invalid object");
	}
}
