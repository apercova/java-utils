package net.apercova.crypto;

/**
 * Objeto con capacidad de cifrado
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 *
 */
public interface Encryptable {
	
	/**
	 * Implementación de cifrado
	 * @param bytes información en claro
	 * @throws Exception
	 */
	byte[] encrypt(byte[] bytes) throws Exception;
	
	/**
	 * Implementación de descifrado.
	 * @param bytes información cifrada
	 * @throws Exception
	 */
	byte[] decrypt(byte[] bytes) throws Exception;
}
