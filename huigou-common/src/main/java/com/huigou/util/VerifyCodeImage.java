package com.huigou.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import com.huigou.exception.ApplicationException;

/**
 * 验证码图片生成工具
 * 
 * @author xiex
 */
public class VerifyCodeImage {
    // 图片的宽度。
    private int width = 120;

    // 图片的高度。
    private int height = 40;

    // 验证码字符个数
    private int codeCount = 4;

    // 验证码干扰线数
    private int lineCount = 50;

    // 验证码
    private String code = null;

    // 验证码图片Buffer
    private BufferedImage buffImg = null;

    // 生成随机数
    private Random random = new Random();

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(int codeCount) {
        this.codeCount = codeCount;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code;
    }

    public VerifyCodeImage() {
    }

    /**
     * @param width
     *            图片宽
     * @param height
     *            图片高
     */
    public VerifyCodeImage(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @param width
     *            图片宽
     * @param height
     *            图片高
     * @param codeCount
     *            字符个数
     * @param lineCount
     *            干扰线条数
     */
    public VerifyCodeImage(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
    }

    public void createBufferedImage() {
        int codeX = 0;
        int fontHeight = 0;
        fontHeight = height - 5;// 字体的高度
        codeX = width / (codeCount + 2);// 每个字符的宽度

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 创建字体
        Font font = ImgFontByteUtil.getFont(fontHeight);
        g.setFont(font);

        // 绘制干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs = getRandomNumber(width);
            int ys = getRandomNumber(height);
            int xe = xs + getRandomNumber(width / 8);
            int ye = ys + getRandomNumber(height / 8);
            g.setColor(getRandomColor());
            g.drawLine(xs, ys, xe, ye);
        }

        // 获取随机数
        if (StringUtil.isBlank(code)) {
            code = RandomString.getInstance().getRandomString(codeCount, "iu");
        }

        // 随机产生验证码字符
        for (int i = 0; i < codeCount; i++) {
            String strRand = code.substring(i, i + 1);
            // 设置字体颜色
            g.setColor(getRandomColor());
            // 设置字体位置
            g.drawString(strRand, (i + 1) * codeX, getRandomNumber(height / 2) + 25);
        }
    }

    /** 获取随机颜色 */
    private Color getRandomColor() {
        int r = getRandomNumber(255);
        int g = getRandomNumber(255);
        int b = getRandomNumber(255);
        return new Color(r, g, b);
    }

    /** 获取随机数 */
    private int getRandomNumber(int number) {
        return random.nextInt(number);
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        if (buffImg == null) {
            throw new ApplicationException("buffImg can not be null!");
        }
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

}
