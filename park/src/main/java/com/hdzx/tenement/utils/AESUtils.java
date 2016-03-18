package com.hdzx.tenement.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Created by anchendong on 15/6/30.
 */
public class AESUtils {

    public static final String TAG = "AESUtils";

    private static byte[] iv= {0x4f, 0x4d, 0x19, 0x5c, 0x6f, 0x0b, 0x2a, 0x6b, 0x04, 0x0e, 0x1e, 0x4f, 0x3a, 0x5d, 0x3e, 0x2f};

    public static String encrypt(String seed, String clearText) {
        // Log.d(TAG, "加密前的seed=" + seed + ",内容为:" + clearText);
        byte[] result = null;
        try {
//            byte[] rawkey = getRawKey(seed.getBytes());
//            byte[] rawkey = seed.getBytes();
            byte[] rawkey = bwkey(seed.getBytes());
            result = encrypt(rawkey, clearText.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String content = toHex(result);
        String content = Base64.encodeBytes(result);
        // Log.d(TAG, "加密后的内容为:" + content);
        return content;

    }

    public static String decrypt(String seed, String encrypted) {
        // Log.d(TAG, "解密前的seed=" + seed + ",内容为:" + encrypted);
        byte[] rawKey;
        try {
//            rawKey = getRawKey(seed.getBytes());
            rawKey = bwkey(seed.getBytes());
//            rawKey = seed.getBytes();
//            byte[] enc = toByte(encrypted);
            byte[] enc = Base64.decode(encrypted);
            byte[] result = decrypt(rawKey, enc);
            String coentn = new String(result);
            // Log.d(TAG, "解密后的内容为:" + coentn);
            return coentn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static byte[] bwkey(byte[] key){
        int len=key.length;
        int i=16-len;
        byte [] result=new byte[16];
        System.arraycopy(key, 0, result, 0, len);
        for (int j=len;j<i;j++){
            result[j]=0x00;
        }
        return result;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr);// 192 and 256 bits may not be available
        SecretKey sKey = kgen.generateKey();
        byte[] raw = sKey.getEncoded();

        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
//                new byte[cipher.getBlockSize()]));
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
//                new byte[cipher.getBlockSize()]));
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        final String HEX = "0123456789ABCDEF";
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
