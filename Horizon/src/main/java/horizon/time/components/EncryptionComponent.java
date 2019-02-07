package horizon.time.components;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

@Component
public class EncryptionComponent {
	MessageDigest md; // Used to create the MD5 hash
	String fixedSeed = "NEEdMkDU3fuPAgbKjYzu"; // Seed used for folder creation, it must never be changed.
	String fluidSeed = "Bib9sm2AlcodAldv9qEl"; // Seed can be randomly used

	public EncryptionComponent() throws Exception {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Invalid algorithm");
		}
	}

	/**
	 * Generation of a random string
	 * 
	 * @return random String of 8 characters
	 */
	public String hashRandom() {
		return hashRandom(8);
	}

	public String hashRandom(int length) {
		return hashFixedString(UUID.randomUUID().toString()).substring(0, length).toLowerCase();
	}

	/**
	 * MD5 hash a string with a fixed random seed.
	 * 
	 * @param input string
	 * @return hashed string mixed with a random seed
	 */
	public String hashFixedString(String input) {
		md.reset();

		input += fixedSeed;
		md.update(input.getBytes());

		byte[] digest = md.digest();

		return DatatypeConverter.printHexBinary(digest);
	}

}
