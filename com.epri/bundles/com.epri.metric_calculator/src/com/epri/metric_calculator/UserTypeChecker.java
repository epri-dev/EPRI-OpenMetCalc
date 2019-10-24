package com.epri.metric_calculator;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;

/**
 * User type checker
 * 
 * @author JoWookJae
 *
 */
public class UserTypeChecker {

	/**
	 * Algorithm for encryption
	 */
	private static final String ALGORITHM = "AES";

	/**
	 * Encoding of file
	 */
	private static final String ENCODING = "UTF-8";

	/**
	 * Secret key
	 */
	private static File KEY_FILE;

	/**
	 * Encrypted file
	 */
	private static File ENCRYPTED_FILE;

	static {
		try {
			KEY_FILE = Activator.getFile(".key");
			ENCRYPTED_FILE = Activator.getFile(".encrypt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check user-type. If it returns true, user-type is expert.
	 * 
	 * @return
	 */
	public static boolean isExpert() {
		String encrypt;
		try {
			encrypt = new String(decrypt());
			if (encrypt.equals(Activator.getDefault().getBundle().getSymbolicName())) {
				return true;
			}
		} catch (Throwable e) {
			// Do nothing...
		}

		return false;
	}

	public static byte[] decrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] keyContents = FileUtils.readFileToByteArray(KEY_FILE);
		SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(keyContents), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);

		return cipher.doFinal(Base64.getDecoder().decode(FileUtils.readFileToByteArray(ENCRYPTED_FILE)));
	}

	public static void encrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] keyContents = FileUtils.readFileToByteArray(KEY_FILE);
		SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(keyContents), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] enc = cipher.doFinal(Activator.getDefault().getBundle().getSymbolicName().getBytes());
		FileUtils.write(ENCRYPTED_FILE, Base64.getEncoder().encodeToString(enc), ENCODING);
	}
}
