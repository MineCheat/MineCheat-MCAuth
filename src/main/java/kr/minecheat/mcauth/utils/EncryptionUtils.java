package kr.minecheat.mcauth.utils;

import lombok.Getter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.*;

public class EncryptionUtils {
    @Getter
    private static final SecureRandom secureRandom = new SecureRandom();

    public static KeyPair generate1024RSAKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair kp = generator.generateKeyPair();
        return kp;
    }

    public static byte[] generateRandomByteArray(int length) {
        byte[] result = new byte[length];
        secureRandom.nextBytes(result);
        return result;
    }

    public static byte[] decrypt(PrivateKey pk, byte[] encrypted) throws Exception {
        Cipher decrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decrypt.init(Cipher.DECRYPT_MODE, pk);
        return decrypt.doFinal(encrypted);
    }

    public static String calculateAuthHash(byte[] sharedSecret, PublicKey pk) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update("".getBytes());
        md.update(sharedSecret);
        md.update(pk.getEncoded());
        byte[] result = md.digest();
        return new BigInteger(result).toString(16);
    }

    public static Cipher keyToCipher(int type, SecretKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(type, key, new IvParameterSpec(key.getEncoded()));
        return cipher;
    }


    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int symbolLength = symbols.length();
        for (int i = 0 ; i < length; i ++)
            sb.append(symbols.charAt(secureRandom.nextInt(symbolLength)));
        return sb.toString();
    }
}
