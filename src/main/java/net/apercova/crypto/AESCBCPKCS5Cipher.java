package net.apercova.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implementación de cifrado AES/CBC/PKCS5Padding con key PBKDF2WithHmacSHA1
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 *
 */
public class AESCBCPKCS5Cipher extends BufferedPBEncryptable{
	
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String KEY_TRANSFORMATION = "PBKDF2WithHmacSHA1";
	private static final String KEY_ALGORITHM = "AES";
	private static final int KEY_LENGTH = 128;
	private static final int ROUNDS = 65536;
	
	/**
	 * Sha1("password") Utilizar password distinto.
	 */
	private static final String DEF_KEY = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
	
	private char[] password;
	private byte[] salt;
	
	public AESCBCPKCS5Cipher() {
		this(null,null);
	}
	
	public AESCBCPKCS5Cipher(char[] password) {
		this(password,null);
	}
	
	public AESCBCPKCS5Cipher(char[] password, byte[] salt) {
		this.password = (password == null)?DEF_KEY.toCharArray():password;
		this.salt = (salt == null)?DEF_KEY.getBytes():salt;
	}
				
	protected static SecretKey createSecretKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_TRANSFORMATION);
		KeySpec spec = new PBEKeySpec(password, salt, ROUNDS, KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
    }
	
	protected static byte[][] encrypt(byte[] bytes, char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException{
		SecretKey key = createSecretKey(password, salt);
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(TRANSFORMATION);
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
		AlgorithmParameters params = cipher.getParameters();
		final byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		final byte[] encrypted = cipher.doFinal(bytes);
		return new byte[][] {encrypted, iv};
	}
	
	protected static byte[] decrypt(byte[] bytes, char[] password, byte[] salt, byte[] iv) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKey key = createSecretKey(password, salt);
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(TRANSFORMATION);
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		return cipher.doFinal(bytes);
	}

	@Override
	public byte[] encrypt() throws Exception {
		if(content == null) {
			throw new IllegalStateException("content");
		}
		return encrypt(content);
	}

	@Override
	public byte[] decrypt() throws Exception {
		if(content == null) {
			throw new IllegalStateException("content");
		}
		return decrypt(content);
	}

	public byte[] encrypt(byte[] bytes) throws Exception {
		if(bytes.length <1) {
			return bytes;
		}
		byte[][] res = AESCBCPKCS5Cipher.encrypt(bytes, password, salt);
		iv = res[1];
		return res[0];
	}

	public byte[] decrypt(byte[] bytes) throws Exception {
		if(bytes.length <1) {
			return bytes;
		}
		if(iv == null) {
			throw new NullPointerException("iv");
		}
		return AESCBCPKCS5Cipher.decrypt(bytes, password, salt, iv);
	}

}
