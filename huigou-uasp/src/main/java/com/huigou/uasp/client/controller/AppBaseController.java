package com.huigou.uasp.client.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.huigou.exception.ApplicationException;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
/**
 * 手机APP访问Controller基类
 * 
 * @author gongmm
 */
public class AppBaseController extends CommonController {

    /**
     * 继承父类方法，这里需要对输入的字符串压缩
     */
    public String blank(String msg) {
        HttpServletResponse response = this.getResponse();
        LogHome.getLog(this).debug(msg);
        byte[] stringBytes;
        try {
            // 数据压缩
            stringBytes = StringUtil.compress(msg);
            response.setContentLength(stringBytes.length);
            response.getOutputStream().write(stringBytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * 
     * @Title: stringToImage
     * @author
     * @Description: TODO
     * @param @param imgData
     * @param @param imgFilePath
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void stringToImage(String imgData, String imgFilePath) throws IOException {
        if (imgData == null) throw new ApplicationException("未读取到图片信息!");
        // Base64解码
        byte[] b;
        OutputStream out = null;
        try {
            b = Base64.decodeBase64(imgData);
            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LogHome.getLog(this).error("解码并生成图片错误：", e);
            throw e;
        } finally {
            if (out != null) out.close();
        }

    }
}
