package com.epri.metric_calculator.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Bouncy Castle RSA Manager
 * 
 * @author JoWookJae
 *
 */
@Deprecated
public class BcRsaManager {

	/**
	 * key file extension
	 */
	public static final String KEY_FILE_EXTENSION = "key";

	private static BcRsaManager INSTANCE;

	public static BcRsaManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BcRsaManager();
		}

		return INSTANCE;
	}

	private BcRsaManager() {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Load private key from file
	 * 
	 * @param file
	 *            File that wrote the byte stream of the private key.
	 * @return private key instance
	 * @throws IOException
	 *             failed to read file
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * 
	 */
	public PrivateKey loadPrivateKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes;
		keyBytes = Files.readAllBytes(file.toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	/**
	 * Load public key from file
	 * 
	 * @param file
	 *            File that wrote the byte stream of the public key.
	 * @return public key instance
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public PublicKey loadPublicKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(file.toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
}
