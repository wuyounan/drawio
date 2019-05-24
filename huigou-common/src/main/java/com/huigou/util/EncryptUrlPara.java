package com.huigou.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.mail.internet.MimeUtility;
/**
 * URL参数加密解密
 * 
 * @author xx
 */
public class EncryptUrlPara {
    private final static int ITERATIONS = 20;

    private final static String KEY = "PBEWithMD5AndDES";

    public static byte[] encode(byte[] b) throws Exception {
        ByteArrayOutputStream baos = null;
        OutputStream b64os = null;
        try {
            baos = new ByteArrayOutputStream();
            b64os = MimeUtility.encode(baos, "base64");
            b64os.write(b);
            b64os.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
            } catch (Exception e) {
            }
            try {
                if (b64os != null) {
                    b64os.close();
                    b64os = null;
                }
            } catch (Exception e) {
            }
        }
    }

    public static byte[] decode(byte[] b) throws Exception {
        ByteArrayInputStream bais = null;
        InputStream b64is = null;
        try {
            bais = new ByteArrayInputStream(b);
            b64is = MimeUtility.decode(bais, "base64");
            byte[] tmp = new byte[b.length];
            int n = b64is.read(tmp);
            byte[] res = new byte[n];
            System.arraycopy(tmp, 0, res, 0, n);
            return res;
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                    bais = null;
                }
            } catch (Exception e) {
            }
            try {
                if (b64is != null) {
                    b64is.close();
                    b64is = null;
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 加密
     * 
     * @author 
     * @param key
     * @param plainText
     * @return
     * @throws Exception
     * @throws
     */
    public static String encrypt(String key, String plainText) throws Exception {
        try {
            byte[] salt = new byte[8];
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            for (int i = 0; i < 8; i++) {
                salt[i] = digest[i];
            }
            PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY);
            SecretKey skey = keyFactory.generateSecret(pbeKeySpec);
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
            Cipher cipher = Cipher.getInstance(KEY);
            cipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            String saltString = new String(encode(salt));
            String ciphertextString = new String(encode(cipherText));
            return saltString + ciphertextString;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Encrypt Text Error:" + e.getMessage(), e);
        }
    }

    /**
     * 解密
     * 
     * @author 
     * @param key
     * @param encryptTxt
     * @return
     * @throws Exception
     * @throws
     */
    public static String decrypt(String key, String encryptTxt) throws Exception {
        int saltLength = 12;
        try {
            String salt = encryptTxt.substring(0, saltLength);
            String ciphertext = encryptTxt.substring(saltLength, encryptTxt.length());
            byte[] saltarray = decode(salt.getBytes());
            byte[] ciphertextArray = decode(ciphertext.getBytes());
            PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY);
            SecretKey skey = keyFactory.generateSecret(keySpec);
            PBEParameterSpec paramSpec = new PBEParameterSpec(saltarray, ITERATIONS);
            Cipher cipher = Cipher.getInstance(KEY);
            cipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
            byte[] plaintextArray = cipher.doFinal(ciphertextArray);
            return new String(plaintextArray);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
