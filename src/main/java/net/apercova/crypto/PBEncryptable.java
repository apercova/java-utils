package net.apercova.crypto;

/**
 * Objeto con capacidad de cifrado basado en password
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 *
 */
public interface PBEncryptable extends Encryptable{

	/**
	 * Devuelve vector de inicialización
	 */
	byte[] getIv();
	
	/**
	 * Define vector de inicialización.
	 * @param iv Vector de inicialización.
	 */
	void setIv(byte[] iv);	
}
