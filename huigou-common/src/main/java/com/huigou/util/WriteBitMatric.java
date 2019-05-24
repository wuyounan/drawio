package com.huigou.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.huigou.exception.ApplicationException;

/**
 * google zxing 生成二维码 条形码
 * 
 * @author xx
 */
public class WriteBitMatric {
    private static final int BLACK = 0xFF000000;

    private static final int WHITE = 0xFFFFFFFF;

    private String codeContent;// 二维码内容

    private int width;// 二维码宽

    private int height;// 二维码高

    private String codeImgType;// 生成图片类型

    private String codeImgPath;// 图片生成路径

    private OutputStream codeOutput;// 图片生成文件流

    private BarcodeKind barcodeKind;// 类别

    public WriteBitMatric() {
        width = height = 300;// 默认生成图片大小
        codeImgType = "png";// 默认生成图片类型
        this.barcodeKind = BarcodeKind.QR_CODE;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }

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

    public String getCodeImgType() {
        return codeImgType;
    }

    public void setCodeImgType(String codeImgType) {
        this.codeImgType = codeImgType;
    }

    public String getCodeImgPath() {
        return codeImgPath;
    }

    public void setCodeImgPath(String codeImgPath) {
        this.codeImgPath = codeImgPath;
    }

    public OutputStream getCodeOutput() {
        return codeOutput;
    }

    public void setCodeOutput(OutputStream codeOutput) {
        this.codeOutput = codeOutput;
    }

    public BarcodeKind getBarcodeKind() {
        return barcodeKind;
    }

    public void setBarcodeKind(BarcodeKind barcodeKind) {
        this.barcodeKind = barcodeKind;
    }

    /**
     * 生成二维码
     * 
     * @Title: encoderCode
     * @author xiexin
     * @param @throws ApplicationException
     * @return void
     * @throws
     */
    public void encodeCode() throws ApplicationException {
        if (StringUtil.isBlank(codeContent)) {
            throw new ApplicationException("二维码内容为空无法生成!");
        }
        if (codeOutput == null && StringUtil.isBlank(codeImgPath)) {
            throw new ApplicationException("二维码输出方式不能为空!");
        }
        try {
            if (codeOutput != null) {
                this.encodeCodeToStream();
            }
            if (!StringUtil.isBlank(codeImgPath)) {
                this.encodeCodeToFile();
            }
        } catch (WriterException e) {
            LogHome.getLog(this).error("WriteBitMatric error:", e);
            throw new ApplicationException("生成二维码错误:" + e.getMessage());
        } catch (Exception e) {
            throw new ApplicationException("生成二维码错误:" + e.getMessage());
        }
    }

    private BufferedImage toBufferedImage(BitMatrix bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, bm.get(i, j) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private void writeBitMatricToFile(BitMatrix bm, String format, File file) {
        BufferedImage image = toBufferedImage(bm);
        try {
            if (!ImageIO.write(image, format, file)) {
                throw new RuntimeException("Can not write an image to file" + file);
            }
        } catch (IOException e) {
            LogHome.getLog(this).error("WriteBitMatric error:", e);
            throw new ApplicationException(e);
        }
    }

    private void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        try {
            if (!ImageIO.write(image, format, stream)) {
                throw new IOException("Could not write an image of format " + format);
            }
        } catch (IOException e) {
            LogHome.getLog(this).error("WriteBitMatric error:", e);
            throw new ApplicationException(e);
        }
    }

    private String writeBitMatricToBase64(BitMatrix bm, String format) {
        BufferedImage image = toBufferedImage(bm);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            if (!ImageIO.write(image, format, os)) {
                throw new IOException("Could not write an image of format " + format);
            }
            Base64 base64 = new Base64();
            String base64Img = new String(base64.encode(os.toByteArray()));
            return base64Img;
        } catch (IOException e) {
            LogHome.getLog(this).error("WriteBitMatric error:", e);
            throw new ApplicationException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LogHome.getLog(this).error("WriteBitMatric error:", e);
                    throw new ApplicationException(e);
                }
            }
        }
    }

    /**
     * 生成BitMatrix
     * 
     * @return
     * @throws WriterException
     */
    private BitMatrix createBitMatrix() throws WriterException {
        Map<EncodeHintType, String> hints = null;
        int codeWidth = width;
        if (this.barcodeKind == BarcodeKind.EAN_13) {
            codeWidth = 3 + // start guard
                        (7 * 6) + // left bars
                        5 + // middle guard
                        (7 * 6) + // right bars
                        3; // end guard
            codeWidth = Math.max(codeWidth, width);
        } else {
            hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        }
        return new MultiFormatWriter().encode(codeContent, barcodeKind.getFormat(), codeWidth, height, hints);
    }

    /**
     * 生成二维码(QRCode)图片
     * 
     * @throws WriterException
     */
    private void encodeCodeToFile() throws WriterException {
        BitMatrix bm = this.createBitMatrix();
        File imgFile = new File(codeImgPath);
        this.writeBitMatricToFile(bm, codeImgType, imgFile);
    }

    /**
     * 生成二维码(QRCode)图片
     * 
     * @throws WriterException
     * @throws IOException
     */
    private void encodeCodeToStream() throws WriterException, IOException {
        BitMatrix bm = this.createBitMatrix();
        this.writeToStream(bm, codeImgType, codeOutput);
    }

    /**
     * 通过BASE64编码生成图片
     * 
     * @return
     */
    public String encodeCodeToBase64() {
        if (StringUtil.isBlank(codeContent)) {
            throw new ApplicationException("二维码内容为空无法生成!");
        }
        try {
            BitMatrix bm = this.createBitMatrix();
            return this.writeBitMatricToBase64(bm, codeImgType);
        } catch (WriterException e) {
            LogHome.getLog(this).error("WriteBitMatric error:", e);
            throw new ApplicationException("生成二维码错误:" + e.getMessage());
        } catch (Exception e) {
            throw new ApplicationException("生成二维码错误:" + e.getMessage());
        }
    }

    public enum BarcodeKind {
        /**
         * BarcodeFormat.CODE_128; // 表示高密度数据， 字符串可变长，符号内含校验码
         * BarcodeFormat.CODE_39;
         * BarcodeFormat.CODE_93;
         * BarcodeFormat.CODABAR; // 可表示数字0 - 9，字符$、+、 -、还有只能用作起始/终止符的a,b,c d四个字符，可变长度，没有校验位
         * BarcodeFormat.DATA_MATRIX;
         * BarcodeFormat.EAN_8; //条形码
         * BarcodeFormat.EAN_13; //条形码
         * BarcodeFormat.ITF;
         * BarcodeFormat.PDF417; // 二维码
         * BarcodeFormat.QR_CODE; // 二维码
         * BarcodeFormat.RSS_EXPANDED;
         * BarcodeFormat.RSS14;
         * BarcodeFormat.UPC_E; // 统一产品代码E:7位数字,最后一位为校验位
         * BarcodeFormat.UPC_A; // 统一产品代码A:12位数字,最后一位为校验位
         * BarcodeFormat.UPC_EAN_EXTENSION;
         */
        CODE_128(BarcodeFormat.CODE_128),
        CODE_93(BarcodeFormat.CODE_93),
        CODE_39(BarcodeFormat.CODE_39),
        EAN_13(BarcodeFormat.EAN_13),
        QR_CODE(BarcodeFormat.QR_CODE);

        private final BarcodeFormat format;

        private BarcodeKind(BarcodeFormat format) {
            this.format = format;
        }

        public BarcodeFormat getFormat() {
            return format;
        }

    }

    public static void main(String[] args) {
        String imgPath = "D:/QRCode.png";
        String encoderContent = "RB201709080014";
        WriteBitMatric writeBitMatric = new WriteBitMatric();
        writeBitMatric.setCodeContent(encoderContent); 
        writeBitMatric.setCodeImgPath(imgPath);
        writeBitMatric.setWidth(115);
        writeBitMatric.setHeight(40);
        writeBitMatric.setBarcodeKind(BarcodeKind.CODE_128);
        writeBitMatric.encodeCode();
        //System.out.println(writeBitMatric.encodeCodeToBase64());
    }

}
