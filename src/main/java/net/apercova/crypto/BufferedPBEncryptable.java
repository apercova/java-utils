package net.apercova.crypto;

/**
 * Objeto con capacidad de cifrado basado en password.
 * Implementacion con buffer actualizable.
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.12
 *
 */
public abstract class BufferedPBEncryptable implements PBEncryptable{
	
	protected byte[] content;
	protected byte[] iv;
	
	protected BufferedPBEncryptable() {
		content = new byte[0];
	}
	
	protected void reset() {
		content = new byte[0];
	}
	
	/**
	 * Actualiza contenido de buffer.
	 * @param update
	 */
	public void update(byte[] update){
		if(update != null && update.length>0) {
			if(content == null) {
				content = new byte[0];
			}
			byte[] buff = new byte[content.length+update.length];
			System.arraycopy(content, 0, buff, 0, content.length);
			System.arraycopy(update, 0, buff, content.length, update.length);
			content = buff;
		}
	}

	public byte[] getContent() {
		return content;
	}
	
	public byte[] getIv() {
		return iv;
	}
	
	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
	/**
	 * Implementación de cifrado de buffer
	 * @return Información de buffer cifrada
	 * @throws Exception
	 */
	public abstract byte[] encrypt() throws Exception;
	
	/**
	 * Implementación de descifrado de buffer
	 * @return Información de buffer descifrada
	 * @throws Exception
	 */
	public abstract byte[] decrypt() throws Exception;
}
