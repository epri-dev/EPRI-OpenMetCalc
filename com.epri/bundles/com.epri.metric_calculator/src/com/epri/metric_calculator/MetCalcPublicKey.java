package com.epri.metric_calculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import com.epri.metric_calculator.util.BcRsaManager;

/**
 * MetCalc public key
 * 
 * @author JoWookJae
 *
 */
@Deprecated
public class MetCalcPublicKey {
	public static final String PUBLIC_KEY_PATH = "setting/MetCalc_public.key";

	private static MetCalcPublicKey INSTANCE;

	/**
	 * RSA public key from file
	 */
	private RSAPublicKey key;

	/**
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private MetCalcPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

		// Generate RSA public key from file
		byte[] keyBytes = Files.readAllBytes(new File(PUBLIC_KEY_PATH).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		key = (RSAPublicKey) kf.generatePublic(spec);
	}

	/**
	 * @return Single instance of MetCalcPublicKey
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public static MetCalcPublicKey getInstance() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		// Init BC
		BcRsaManager.getInstance();

		if (INSTANCE == null)
			INSTANCE = new MetCalcPublicKey();

		return INSTANCE;
	}

	/**
	 * @return RSA public key
	 */
	public RSAPublicKey getKey() {
		return key;
	}

	/**
	 * @return Bit length of public key
	 */
	public int getBitLength() {
		return key.getModulus().bitLength();
	}
}
