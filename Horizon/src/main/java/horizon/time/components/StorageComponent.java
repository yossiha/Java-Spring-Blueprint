package horizon.time.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageComponent {
	String baseApplicationFolder; // This needs to be the path to the local folder resource/static
	String baseUploadFolderMedia; // Media uploads folder
	String baseUploadFolderMediaUserDate; // Date after hash (year/month)
	String baseUploadFolder; // baseUploadFolderMedia + baseUploadFolderMediaUserDate

	Path baseUploadPath; // Path representation of baseApplicationFolder + baseUploadFolder

	@Autowired
	EncryptionComponent encryptionService;

	/**
	 * 
	 * @return file extension of the required type
	 * @throws Exception in case not a valid type
	 */
	public String checkFileExtentionGeneral(String required, String mimetype) throws Exception {
		String type = mimetype.split("/")[0];
		String imageType = mimetype.split("/")[1];

		if (!type.equals(required)) {
			new Exception("Invalid file type");
		}

		switch (required) {
		case "image": {
			if (imageType.equals("jpeg") || imageType.equals("jpg"))
				return "jpg";
			else if (!imageType.equals("png") && !imageType.equals("gif"))
				return imageType;
			else
				throw new Exception("Invalid image type");
		}
		default: {
			throw new Exception("Invalid file type");
		}
		}
	}

	/**
	 * Copy a media file to the upload directory
	 * 
	 * @param file is the multipart file uploaded
	 * @return a file map of the file's save parameters
	 * @throws Exception
	 */
	public Map<String, String> createFile(MultipartFile file) throws Exception {
		Map<String, String> fileMap = new HashMap<String, String>();
		String filename = encryptionService.hashRandom();
		String ext = file.getContentType().split("/")[1];
		filename += '.' + ext;

		Path path = Paths.get(StringUtils.cleanPath(baseUploadPath + "/" + filename)).toAbsolutePath().normalize();

		if (Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING) > 0) {
			fileMap.put("baseUploadFolderMedia", baseUploadFolderMedia);
			fileMap.put("baseUploadFolderMediaUserDate", baseUploadFolderMediaUserDate);
			fileMap.put("filename", filename);
			fileMap.put("ext", ext);
			return fileMap;
		} else
			throw new Exception("Unable to copy file");
	}

	/**
	 * Copy an image file to the upload directory
	 * 
	 * @param file      is the multipart file uploaded
	 * @param imageSize //int[] = { width, height }
	 * @return a file map of the file's save parameters
	 * @throws Exception
	 */
	public Map<String, String> createImageFile(MultipartFile file, int[] imageSize) throws Exception {
		int imageWidth = imageSize[0];
		int imageHeight = imageSize[1];
		Map<String, String> fileMap = new HashMap<String, String>();
		String filename = encryptionService.hashRandom();
		String ext = checkFileExtentionGeneral("image", file.getContentType());
		filename += '.' + ext;

		try {
			// Move to a different thread
			javaxt.io.Image image = new javaxt.io.Image(file.getBytes());
			if (image.getWidth() > imageWidth)
				image.setWidth(imageWidth);
			if (image.getHeight() > imageHeight) // Cut in the middle if it too high
				image.crop(0, (image.getHeight() - imageHeight) / 2, imageWidth, imageHeight);

			image.saveAs(StringUtils.cleanPath(baseUploadPath + "/" + filename));

			fileMap.put("baseUploadFolderMedia", baseUploadFolderMedia);
			fileMap.put("baseUploadFolderMediaUserDate", baseUploadFolderMediaUserDate);
			fileMap.put("filename", filename);
			fileMap.put("ext", ext);
			return fileMap;
		} catch (Exception e) {
			throw new Exception("Unable to copy file");
		}
	}

	public void deleteFile(String path) throws IOException {
		Path filePath = Paths.get(baseApplicationFolder + "/" + path).toAbsolutePath().normalize();
		Files.delete(filePath);
	}

	/**
	 * Create a folder in the system, or make sure it exists
	 * 
	 * @param stringPath
	 * @return true if successful, false if failed
	 * @throws Exception
	 */
	public boolean folderExistsOrCreate(String stringPath) throws Exception {
		Path path = Paths.get(stringPath).toAbsolutePath().normalize();

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		if (!Files.isReadable(path) || !Files.isWritable(path)) {
			return false;
		}
		return true;
	}

	/**
	 * Initialize the used folders in the system. TODO: 1) Change
	 * baseApplicationFolder to be configured in the system as a variable. 2)
	 * Future: GridFS in Spring Data MongoDB
	 * 
	 * @throws Exception on failure // no writing permissions for FS, etc
	 */
	public void init() {
		Calendar c = Calendar.getInstance();

		String currentYear = encryptionService.hashFixedString(String.valueOf(c.get(Calendar.YEAR))).substring(0, 6)
				.toLowerCase();
		String currentMonth = encryptionService.hashFixedString(String.valueOf(c.get(Calendar.MONTH) + 1))
				.substring(0, 6).toLowerCase();

		// attribute MUST start with "/" because it's a folder!
		baseApplicationFolder = "/Java/Projects/Horizon/src/main/resources/static";

		baseUploadFolderMedia = "media/uploads";
		baseUploadFolderMediaUserDate = currentYear + "/" + currentMonth;
		baseUploadFolder = "/" + baseUploadFolderMedia + "/" + baseUploadFolderMediaUserDate;

		baseUploadPath = Paths.get(baseApplicationFolder + baseUploadFolder).toAbsolutePath().normalize();

		try {
			folderExistsOrCreate(baseApplicationFolder + "/" + baseUploadFolder);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		}

	}
}
