package crypt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Crypt {
    // 암복호화 시 사용되는 키
    private static final String CRYPT_KEY = "CRYPT_TEST";
    // 암복호화 알고리즘
    private static final String CRYPT_ALGORITHM = "AES";
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {

        // 1: Encrypt
        // 2: Decrypt
        // 3: Encrypt And Decrypt
        Integer cryptFlag = 3;

        try {
            // 1: Encrypt
            if (cryptFlag == 1) {
                String testValue = "2196479";

                String encryptValue = encrypt(testValue.getBytes(CHARSET));
                System.out.println("1.Encrypt : " + encryptValue);
            }
            // 2: Decrypt
            if (cryptFlag == 2) {
                String testValue = "0C78E86BBE0C226B1DF8DA90080BDC42";

                String decryptValue = decrypt(testValue);
                System.out.println("2.Decrypt : " + decryptValue);
            }
            // 3: Encrypt And Decrypt
            if (cryptFlag == 3) {
                String testValue = "2196479";

                String encryptValue = encrypt(testValue.getBytes(CHARSET));
                System.out.println("1.Encrypt : " + encryptValue);
                String decryptValue = decrypt(encryptValue);
                System.out.println("2.Decrypt : " + decryptValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * encrypt
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encrypt(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKeySpec(CRYPT_KEY));
        return new String(Hex.encodeHex(cipher.doFinal(bytes)));
    }

    /**
     * decrypt
     * 
     * @param encodedValue
     * @return
     * @throws Exception
     */
    public static String decrypt(String encodedValue) throws Exception {
        Cipher decipher = Cipher.getInstance(CRYPT_ALGORITHM);
        decipher.init(Cipher.DECRYPT_MODE, generateSecretKeySpec(CRYPT_KEY));
        return new String(decipher.doFinal(Hex.decodeHex(encodedValue.toCharArray())), CHARSET);
    }

    // PRRIVATE
    /**
     * 암호화 키 생성
     * 16바이트(128비트), 24바이트(192비트), 32바이트(256비트)의 길이로 생성할 수 있다.
     * 
     * @param key
     * @return
     */
    private static SecretKeySpec generateSecretKeySpec(String key) {
        // 바이트 배열 초기화
        int byteLength = 16;
        final byte[] keyBytes = new byte[byteLength];

        int i = 0;
        for (byte b : key.getBytes(CHARSET)) {
            keyBytes[i++ % byteLength] ^= b;
        }
        return new SecretKeySpec(keyBytes, CRYPT_ALGORITHM);
    }
}
