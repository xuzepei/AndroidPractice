package com.apps.radio;

import android.util.Base64;
import android.view.View;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xuzepei on 2018/2/5.
 */

public class Tool {

    private static final Tool instance = new Tool();
    private static byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};



    private Tool() {} //一定要有私有构造
    public static Tool getInstance() {
        return instance;
    }

    public static boolean isOpenAll() {
        return true;
    }

    public static String encode(String plainText, String secretKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes());
        return Base64.encodeToString(encryptedData, 1);
    }

    public static String decode(String encryptText, String secretKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText, 1));
        return new String(decryptData);
    }



}
