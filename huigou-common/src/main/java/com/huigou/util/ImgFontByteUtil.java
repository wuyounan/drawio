package com.huigou.util;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;

/**
 * 读取字体文件
 * 
 * @author xx
 */
public class ImgFontByteUtil {

    private Font baseFont;

    static class SingletonHolder {
        static ImgFontByteUtil instance = new ImgFontByteUtil();
    }

    public static ImgFontByteUtil getInstance() {
        return SingletonHolder.instance;
    }

    private ImgFontByteUtil() {
        this.createFont();
    }

    private byte[] hex2byte(String str) {
        if (str == null) return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1) return null;

        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    private String readFile() throws IOException {
        ClassPathResource file = new ClassPathResource("/font/imgFont.txt");
        StringBuilder buffer = new StringBuilder();
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()));
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    private void createFont() {
        try {
            this.baseFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(hex2byte(readFile())));
        } catch (Exception e) {
            LogHome.getLog(ImgFontByteUtil.class).error(e);
            this.baseFont = null;
        }
    }

    public Font deriveFont(int fontHeight) {
        if (this.baseFont == null) {
            return new Font("Arial", Font.PLAIN, fontHeight);
        }
        return this.baseFont.deriveFont(Font.PLAIN, fontHeight);
    }

    public static Font getFont(int fontHeight) {
        return getInstance().deriveFont(fontHeight);
    }
}
