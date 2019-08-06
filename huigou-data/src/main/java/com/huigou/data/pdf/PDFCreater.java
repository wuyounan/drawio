package com.huigou.data.pdf;

import com.huigou.data.exception.ExportExcelException;
import com.huigou.exception.ApplicationException;
import com.huigou.freemarker.FreemarkerUtil;
import com.huigou.util.FileHelper;
import com.huigou.util.LogHome;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * 根据HTML生成PDF文件
 * 
 * @author gongmm
 */
public class PDFCreater {
    /**
     * 创建PDF文件
     * 
     * @author
     * @param data
     * @return String
     */
    public static String createPDF(String data) {
        String filePath = FileHelper.createTmpFilePath("pdf");
        ClassPathResource simsun = null;
        ClassPathResource yahei = null;
        OutputStream os = null;
        try {
            simsun = new ClassPathResource("/font/simsun.ttc");
            yahei = new ClassPathResource("/font/yahei.ttf");
            os = new FileOutputStream(filePath);
            ITextRenderer renderer = new ITextRenderer();
            // 解决中文支持问题
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(simsun.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            fontResolver.addFont(yahei.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.setDocumentFromString(data);
            // 支持base64编码
            renderer.getSharedContext().setReplacedElementFactory(new B64ImgReplacedElementFactory());
            // 注意代码位置
            /****** linux 下这种方式无法读取图片 修改为 通过url读取 *******/
            // String path = Singleton.getRealPath();
            // 把路径中的反斜杠转成正斜杠
            // path = path.replaceAll("\\\\", "/");
            // renderer.getSharedContext().setBaseURL("file:" + path + "/themes/default/images/");
            renderer.layout();
            renderer.createPDF(os);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
            }
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file.getName();
        } else {
            throw new ExportExcelException("文件生成失败!");
        }
    }

    /**
     * 读取文件中的内容到string
     * 
     * @author
     * @param bodyHTML
     * @return String
     */
    public static String createByBodyHTML(String bodyHTML) {
        StringBuffer document = new StringBuffer();
        document.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        document.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">").append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        document.append("<style type=\"text/css\" mce_bogus=\"1\">");
        document.append("body { font-family: SimSun;   font-size:12px;}");
        document.append("a {text-decoration:none;color:#000000;}");
        document.append("table {border: 1px solid #000000;border-width: 1px 0 0 1px;}");
        document.append("table td{border: 1px solid #000000;border-width: 0 1px 1px 0;height:25px;}");
        document.append("</style>");
        document.append("</head>").append("<body>");
        document.append(bodyHTML);
        document.append("</body></html>");
        return createPDF(document.toString());
    }

    /**
     * 解析Freemarker生成html 再生成PDF
     * 
     * @author
     * @param template
     * @param variables
     * @return String
     */
    public static String createByFreemarker(String template, Map<String, Object> variables) {
        if (!template.endsWith(".ftl")) {
            template = "/print/" + template + ".ftl";
        }
        try {
            String htmlStr = FreemarkerUtil.generate(template, variables);
            return createPDF(htmlStr);
        } catch (Exception e) {
            LogHome.getLog(PDFCreater.class).error(e);
            throw new ExportExcelException("文件生成失败!");
        }
    }

    /**
     * 根据模板 生成word文件
     * 
     * @author
     * @param template
     * @param variables
     * @return
     * @throws
     */
    public static String createWord(String template, Map<String, Object> variables) {
        String filePath = FileHelper.createTmpFilePath("doc");
        ByteArrayInputStream bais = null;
        FileOutputStream fos = null;
        POIFSFileSystem poifs = null;
        if (!template.endsWith(".ftl")) {
            template = "/print/" + template + ".ftl";
        }
        try {
            String content = FreemarkerUtil.generate(template, variables);
            byte b[] = content.getBytes("UTF-8");
            bais = new ByteArrayInputStream(b);
            poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            directory.createDocument("WordDocument", bais);
            fos = new FileOutputStream(filePath);
            poifs.writeFilesystem(fos);
        } catch (Exception e) {
            LogHome.getLog(PDFCreater.class).error(e);
            throw new ExportExcelException("文件生成失败!");
        } finally {
            try {
                if (fos != null) fos.close();
                if (bais != null) bais.close();
            } catch (IOException e) {
                LogHome.getLog(PDFCreater.class).error(e);
            }
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file.getName();
        } else {
            throw new ExportExcelException("文件生成失败!");
        }
    }

    /**
     * 创建HTML文件
     * 
     * @param template
     * @param variables
     * @return
     */
    public static String createHtml(String template, Map<String, Object> variables) {
        String filePath = FileHelper.createTmpFilePath("html");
        if (!template.endsWith(".ftl")) {
            template = "/print/" + template + ".ftl";
        }
        FileOutputStream fos = null;
        OutputStreamWriter ow = null;
        try {
            String content = FreemarkerUtil.generate(template, variables);
            fos = new FileOutputStream(filePath);
            ow = new OutputStreamWriter(fos, "utf-8");
            ow.write(content);
            ow.flush();
        } catch (Exception e) {
            LogHome.getLog(PDFCreater.class).error(e);
            throw new ExportExcelException("文件生成失败!");
        } finally {
            try {
                if (fos != null) fos.close();
                if (ow != null) ow.close();
            } catch (IOException e) {
                LogHome.getLog(PDFCreater.class).error(e);
            }
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file.getName();
        } else {
            throw new ExportExcelException("文件生成失败!");
        }
    }
}
