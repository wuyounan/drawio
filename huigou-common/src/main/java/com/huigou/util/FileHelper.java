package com.huigou.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.huigou.cache.SystemCache;

/**
 * 服务器文件管理帮助类
 * 
 * @ClassName: FileHelper
 * @author xx
 * @date 2014-1-4 上午12:07:00
 * @version V1.0
 */
public class FileHelper {
    /**
     * 系统路径分割符
     */
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

    /**
     * 系统临时文件夹路径
     * 
     * @author
     * @return String
     */
    public static String getTmpdir() {
        String systemTmpdir = SystemCache.getParameter(SystemCache.SYSTEM_TMPDIR, String.class);
        if (StringUtil.isBlank(systemTmpdir)) {
            return System.getProperty("java.io.tmpdir");
        }
        File tmpdir = new File(systemTmpdir);
        if (!tmpdir.exists() || !tmpdir.isDirectory()) {
            return System.getProperty("java.io.tmpdir");
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(FileHelper.FILE_SEPARATOR);
            sb.append(DateUtil.getDateFormat(new Date(), "yyyyMMdd"));
            sb.append(FileHelper.FILE_SEPARATOR);
            systemTmpdir = String.format("%s%s", tmpdir.getPath(), sb.toString());
            File parentDir = new File(systemTmpdir);
            if (!parentDir.exists() || !parentDir.isDirectory()) {
                parentDir.mkdirs();
            }
            systemTmpdir = parentDir.getPath();
        }
        return systemTmpdir;
    }

    /**
     * 系统临时文件夹路径
     * 
     * @author
     * @return String
     */
    public static String getTmpdirFilePath(String fileExt) {
        return getTmpdir() + FileHelper.FILE_SEPARATOR + System.currentTimeMillis() + "_" + new Random().nextInt(1000) + "." + fileExt;
    }

    /**
     * 方法说明:取文件扩展名，没有扩展名返回空""
     * 
     * @param filePath
     * @return
     */
    public static String getFileExtName(String filePath) {
        if (StringUtil.isBlank(filePath)) return "";
        int pos = filePath.lastIndexOf(".");
        if (-1 == pos) return "";
        return filePath.substring(pos + 1).toLowerCase();
    }

    /**
     * 获取文件名
     * 
     * @author
     * @param filePath
     * @return
     * @throws
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isBlank(filePath)) return "";
        int pos = filePath.lastIndexOf(".");
        if (-1 == pos) return "";
        return filePath.substring(0, pos);
    }

    /**
     * 获取文件保存路径
     * 
     * @return
     */
    public static String getFileSavePath() {
        String saveUrl = SystemCache.getParameter(SystemCache.UPLOAD_PATH, String.class);
        String savePath = saveUrl != null && !saveUrl.equals("") ? saveUrl : (FileHelper.FILE_SEPARATOR + "uploadPath" + FileHelper.FILE_SEPARATOR);
        // 检查目录
        File uploadDir = new File(savePath);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            savePath = SystemCache.getRealPath() + saveUrl;
        }
        return savePath;
    }

    /**
     * 获取文件上传临时路径
     * 
     * @return
     */
    public static String getFileTempPath() {
        String saveUrl = SystemCache.getParameter("uploadTempPath", String.class);
        String savePath = saveUrl != null && !saveUrl.equals("") ? saveUrl : (FileHelper.FILE_SEPARATOR + "uploadTempPath" + FileHelper.FILE_SEPARATOR);
        // 检查目录
        File uploadDir = new File(savePath);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            savePath = SystemCache.getRealPath() + saveUrl;
        }
        return savePath;
    }

    public static String getConvertOutputPath() {
        return SystemCache.getParameter("convertOutputPath", String.class);
    }

    /**
     * 方法说明:检测是否是文件，不存在该文件或目录，返回false
     * 
     * @param filePath
     * @return
     */
    public static boolean isFile(String filePath) {
        if (filePath == null || filePath.equals("")) return false;
        filePath = getFileSavePath() + filePath;
        File f = new File(filePath);
        if (!f.exists()) return false;
        return f.isFile();
    }

    /**
     * 方法说明:检测文件或目录是否存在
     * 
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        if (filePath == null || filePath.equals("")) return false;
        filePath = getFileSavePath() + filePath;
        File f = new File(filePath);
        return f.exists();
    }

    /**
     * 方法说明:删除文件
     * 
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return;
        }

        filePath = getFileSavePath() + filePath;
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            // f.delete();
        }
    }

    /**
     * 删除被转换的文件
     * 
     * @author
     * @param fileId
     * @throws
     */
    public static void delConvertFiles(String fileId) {
        String filePath = getConvertOutputPath();
        if (filePath == null || filePath.equals("")) return;
        File dir = new File(filePath + fileId + File.separator);
        if (dir.exists() && dir.isDirectory()) {
            File[] fs = dir.listFiles();
            for (File f : fs) {
                if (f.exists()) f.delete();
            }
            dir.delete();
        }
    }

    /**
     * 方法说明:清空文件夹中的所有文件
     * 
     * @param folder
     */
    public static void cleanFile(String folder) {
        if (folder == null || folder.equals("")) return;
        folder = getFileSavePath() + folder;
        File f = new File(folder);
        if (f.isDirectory()) {
            File[] fs = f.listFiles();
            for (int i = 0; i < fs.length; i++) {
                // if (fs[i].exists()) fs[i].delete();
            }
        }
    }

    public static File getFile(String filePath) {
        if (filePath == null || filePath.equals("")) return null;
        filePath = getFileSavePath() + filePath;
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            return f;
        }
        return null;
    }

    public static File getRealPathFile(String filePath) {
        if (filePath == null || filePath.equals("")) return null;
        filePath = SystemCache.getRealPath() + filePath;
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            return f;
        }
        return null;
    }

    /**
     * 读取文件到字节数组
     * 
     * @author
     * @param f
     * @return
     * @throws Exception
     *             byte[]
     */
    public static byte[] readFileToByteArray(File f) throws Exception {
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        byte[] fileb = null;
        InputStream is = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            is = new FileInputStream(f);
            byte[] b = new byte[1024];
            int n;
            while ((n = is.read(b)) != -1) {
                out.write(b, 0, n);
            }
            fileb = out.toByteArray();
        } catch (Exception e) {
            throw new Exception("readFileToByteArray", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        return fileb;
    }

    /**
     * 根据byte数组，生成文件
     * 
     * @author
     * @param bfile
     * @param filePath
     * @param fileName
     *            void
     */
    public static void writeByteToFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath, fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件
     * 
     * @author
     * @param fromFile
     * @param filePath
     * @return
     * @throws
     */
    public static boolean copyFile(File fromFile, String filePath) {
        if (filePath == null || filePath.equals("")) return false;
        filePath = getFileSavePath() + filePath;
        File toFile = new File(filePath);
        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException e) {
            LogHome.getLog(FileHelper.class).error("复制文件错误:", e);
            return false;
        }
        return true;
    }

    public static boolean copyFileTo(File fromFile, String filePath) {
        if (filePath == null || filePath.equals("")) return false;
        File toFile = new File(filePath);
        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException e) {
            LogHome.getLog(FileHelper.class).error("复制文件错误:", e);
            return false;
        }
        return true;
    }

    public static boolean copyFile(File fromFile, File toFile) {
        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException e) {
            LogHome.getLog(FileHelper.class).error("复制文件错误:", e);
            return false;
        }
        return true;
    }

    /**
     * 复制文件
     * 
     * @author
     * @param fromPath
     * @param filePath
     * @return
     * @throws
     */
    public static boolean copyFile(String fromPath, String filePath) {
        File fromFile = getFile(fromPath);
        if (null == fromFile) return false;
        return copyFile(fromFile, filePath);
    }

    public static String copyFileByCode(String fromPath, String code) {
        File fromFile = getFile(fromPath);
        if (null == fromFile) return null;
        String newFileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + "." + getFileExtName(fromPath);
        StringBuffer sb = new StringBuffer();
        sb.append(FileHelper.FILE_SEPARATOR);
        sb.append(ClassHelper.convert(code, String.class, "temp"));
        sb.append(FileHelper.FILE_SEPARATOR);
        sb.append(DateUtil.getDateFormat(new Date(), "yyyyMM"));
        sb.append(FileHelper.FILE_SEPARATOR);
        sb.append(newFileName);
        boolean flag = copyFile(fromFile, sb.toString());
        if (flag) {
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * 转换文件大小
     * 
     * @author
     * @param fileSize
     * @return String
     */
    public static String formetFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 文件路径是否存在 不存在新建
     * 
     * @param path
     * @return void
     */
    public static void checkDir(String pathName) {
        File filePath = new File(pathName);
        if (!filePath.isDirectory()) {
            filePath.mkdirs();
        }
    }

    /**
     * 文件是否存在 不存在新建
     * 
     * @param filePathName
     * @throws IOException
     * @return void
     */
    public static void checkFile(String filePathName) throws IOException {
        File file = new File(filePathName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * 生成临时文件名
     * 
     * @author
     * @param fileExt
     * @return String
     */
    public static String createTmpFilePath(String fileExt) {
        StringBuffer filePath = new StringBuffer();
        filePath.append(FileHelper.getTmpdir()).append(FileHelper.FILE_SEPARATOR);
        filePath.append(System.currentTimeMillis()).append("_").append(new Random().nextInt(1000));
        filePath.append(".").append(fileExt);
        return filePath.toString();
    }

    /**
     * 系统临时文件
     * 
     * @param fileExt
     * @return
     */
    public static String createSystemIoTmpFilePath(String fileExt) {
        StringBuffer filePath = new StringBuffer();
        filePath.append(System.getProperty("java.io.tmpdir")).append(FileHelper.FILE_SEPARATOR);
        filePath.append(System.currentTimeMillis()).append("_").append(new Random().nextInt(1000));
        filePath.append(".").append(fileExt);
        return filePath.toString();
    }

    /**
     * 帮助文件保存路径
     * 
     * @author
     * @return String
     */
    public static String getHelpFileSavePath() {
        String saveUrl = SystemCache.getParameter("helpFilePath", String.class);
        String savePath = saveUrl != null && !saveUrl.equals("") ? saveUrl : (FileHelper.FILE_SEPARATOR + "helpFilePath" + FileHelper.FILE_SEPARATOR);
        // 检查目录
        File uploadDir = new File(savePath);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            savePath = SystemCache.getRealPath() + saveUrl;
        }
        return savePath;
    }

    /**
     * 获取帮助文件(和上传文件夹不在同一目录)
     * 
     * @author
     * @param filePath
     * @return File
     */
    public static File getHelpFile(String filePath) {
        if (filePath == null || filePath.equals("")) return null;
        filePath = getHelpFileSavePath() + FileHelper.FILE_SEPARATOR + filePath;
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            return f;
        }
        return null;
    }

    /**
     * 根据文件名判断是否为图片
     * 
     * @author
     * @param filePath
     * @return
     * @throws
     */
    public static boolean isPicture(String filePath) {
        if (filePath == null || filePath.equals("")) return false;
        String fileKind = filePath.toLowerCase();
        if (fileKind.endsWith("gif") || fileKind.endsWith("jpg") || fileKind.endsWith("jpeg") || fileKind.endsWith("png") || fileKind.endsWith("bmp")) {
            return true;
        }
        return false;
    }

    /**
     * 组合文件预览URL
     * 
     * @author
     * @param id
     * @return
     * @throws
     */
    public static String getConvertUrl(Serializable id, boolean isReadOnly) {
        String converUrl = SystemCache.getParameter("SYS.Convert.URL", String.class);
        String method = "%s/attachment.do?method=convertAttachment&attachmentId=%s&isReadOnly=%s&a=%s";
        return String.format(method, converUrl, id, isReadOnly ? "true" : "false", System.currentTimeMillis());
    }
}
