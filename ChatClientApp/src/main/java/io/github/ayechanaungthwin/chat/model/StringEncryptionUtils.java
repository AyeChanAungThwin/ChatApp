package io.github.ayechanaungthwin.chat.model;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringEncryptionUtils {

	public static String encrypt(String data, String secretKey) {
		String plainText = data;

		String encryptedText = null;
		try {
	        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	
	        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
	        encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        return encryptedText;
	}
	
	public static String decrypt(String encryptedData, String secretKey) {
		String encryptedText = encryptedData;

		String decryptedText = null;
        try {
        	SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            decryptedText = new String(decryptedBytes);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        
        return decryptedText;
	}
}
