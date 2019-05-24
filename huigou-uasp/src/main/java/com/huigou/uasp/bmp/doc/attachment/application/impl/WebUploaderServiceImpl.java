package com.huigou.uasp.bmp.doc.attachment.application.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.cache.SystemCache;
import com.huigou.data.domain.model.Creator;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentApplication;
import com.huigou.uasp.bmp.doc.attachment.application.WebUploaderService;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.doc.attachment.domain.model.FileInfo;
import com.huigou.uasp.bmp.doc.attachment.domain.model.FileLock;
import com.huigou.util.ClassHelper;
import com.huigou.util.DateUtil;
import com.huigou.util.FileHelper;
import com.huigou.util.LogHome;

@Service("webUploaderService")
public class WebUploaderServiceImpl extends BaseApplication implements WebUploaderService {

    @Autowired
    private AttachmentApplication attachmentApplication;

    /**
     * 文件上传路径更新为指定文件信息签名后的临时文件夹，用于后期合并
     * 
     * @param info
     * @return
     */
    private String getFileSaveTempPath() {
        String path = FileHelper.getFileTempPath();
        StringBuffer sb = new StringBuffer();
        sb.append(path);
        sb.append(FileHelper.FILE_SEPARATOR);
        sb.append("tmp");
        sb.append(DateUtil.getDateFormat(new Date(), "yyyyMM"));
        sb.append(FileHelper.FILE_SEPARATOR);
        return sb.toString();
    }

    /**
     * 获取文件保存目录
     * 
     * @return
     */
    private String getFileSavePath(FileInfo info) {
        String bizCode = info.getBizCode();
        StringBuffer sb = new StringBuffer();
        sb.append(FileHelper.FILE_SEPARATOR);
        sb.append(ClassHelper.convert(bizCode, String.class, "temp"));
        sb.append(FileHelper.FILE_SEPARATOR);
        sb.append(DateUtil.getDateFormat(new Date(), "yyyyMM"));
        sb.append(FileHelper.FILE_SEPARATOR);
        // toDo 需要保存公司名字
        return sb.toString();
    }

    /**
     * 获取正式文件保存路径信息
     * 
     * @return
     */
    private Map<String, String> getSaveFilePathInfo(FileInfo info) {
        Map<String, String> map = new HashMap<String, String>(3);
        String savePath = FileHelper.getFileSavePath();// 正式文件保存路径
        String saveUrl = getFileSavePath(info);
        File uploadDir = new File(savePath);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            LogHome.getLog(this).error("上传文件夹不存在：" + savePath);
            throw new ApplicationException("上传文件夹" + SystemCache.getParameter("uploadPath", String.class) + "不存在，请检查");
        }
        savePath += saveUrl;
        File dirFile = new File(savePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dirFile.mkdirs();
        }
        // 生成随机文件名
        String newFileName = this.randomFileName(info.getName());
        map.put("savePath", savePath);
        map.put("saveUrl", saveUrl + newFileName);
        map.put("newFileName", newFileName);
        return map;
    }

    @Override
    public File getReadySpace(FileInfo info) {
        String path = getFileSaveTempPath();
        // 创建上传文件所需的文件夹
        if (!this.createFileFolder(path, false)) {
            return null;
        }
        // 如果是分片上传，则需要为分片创建文件夹
        if (info.getChunks() > 0) {
            String newFileName = String.valueOf(info.getChunk());// 上传文件的新名称
            String fileFolder = info.getUniqueFileName();
            if (fileFolder == null) {
                return null;
            }
            path += fileFolder; // 文件上传路径更新为指定文件信息签名后的临时文件夹，用于后期合并
            if (!this.createFileFolder(path, true)) {
                return null;
            }
            return new File(path, newFileName);
        } else {// 不需要分片的直接存储到正式文件库
            Map<String, String> filePath = getSaveFilePathInfo(info);// 文件保存路径信息
            info.setSavePath(filePath.get("saveUrl"));
            return new File(filePath.get("savePath"), filePath.get("newFileName"));
        }
    }

    @Override
    public boolean chunkCheck(FileInfo info) {
        String path = getFileSaveTempPath();

        StringBuffer file = new StringBuffer(path);
        file.append(info.getUniqueName());
        file.append(FileHelper.FILE_SEPARATOR);
        file.append(info.getChunkIndex());
        Long size = Long.valueOf(info.getSize());
        // 检查目标分片是否存在且完整
        File target = new File(file.toString());
        if (target.isFile() && size == target.length()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public File chunksMerge(FileInfo info) {
        String path = getFileSaveTempPath();
        String folder = info.getUniqueName();
        String filePath = path + folder;// 文件路径
        int chunks = info.getChunks();
        // 检查是否满足合并条件：分片数量是否足够
        if (chunks == this.getChunksNum(filePath)) {
            // 同步指定合并的对象
            Lock lock = FileLock.getLock(folder);
            lock.lock();
            FileOutputStream fileOut = null;
            FileChannel outChannel = null;
            try {
                // 检查是否满足合并条件：分片数量是否足够
                File[] folders = this.getChunks(filePath);
                if (folders == null) {
                    return null;
                }
                Map<String, String> saveFilePath = getSaveFilePathInfo(info);// 文件保存路径信息
                List<File> files = new ArrayList<File>(Arrays.asList(folders));
                if (chunks == files.size()) {
                    // 按照名称排序文件，这里分片都是按照数字命名的
                    Collections.sort(files, new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            if (Integer.valueOf(o1.getName()) < Integer.valueOf(o2.getName())) {
                                return -1;
                            }
                            return 1;
                        }
                    });
                    File outputFile = new File(saveFilePath.get("savePath"), saveFilePath.get("newFileName"));
                    outputFile.createNewFile();
                    fileOut = new FileOutputStream(outputFile);
                    outChannel = fileOut.getChannel();
                    // 合并
                    FileChannel inChannel = null;
                    FileInputStream fileIn = null;
                    ByteBuffer bb = null;
                    for (File file : files) {
                        /*
                         * fileIn = new FileInputStream(file);
                         * inChannel = fileIn.getChannel();
                         * long a = inChannel.transferTo(0, inChannel.size(), outChannel);
                         * inChannel.close();
                         * fileIn.close();
                         * LogHome.getLog().info(file.getName() + "=" + a);
                         */
                        fileIn = new FileInputStream(file);
                        inChannel = fileIn.getChannel();
                        bb = ByteBuffer.allocate(1024 * 8);
                        while (inChannel.read(bb) != -1) {
                            bb.flip();
                            outChannel.write(bb);
                            bb.clear();
                        }
                        inChannel.close();
                        fileIn.close();
                        file.delete();// 删除分片
                    }
                    files = null;
                    // 清理：文件夹，tmp文件
                    this.cleanSpace(folder, path);
                    // 将MD5签名和合并后的文件path存入持久层
                    info.setSavePath(saveFilePath.get("saveUrl"));
                    return outputFile;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ApplicationException(ex);
            } finally {
                // 解锁
                lock.unlock();
                // 清理锁对象
                FileLock.removeLock(folder);
                if (outChannel != null) {
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOut != null) {
                    try {
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String saveFileMap(FileInfo fileInfo, File newFile) {
        if (newFile == null || !newFile.exists()) {
            throw new ApplicationException("文件上传失败。");
        }

        try {
            BigDecimal size = new BigDecimal(ClassHelper.convert(fileInfo.getSize(), Long.class));
            BigDecimal fileLength = new BigDecimal(newFile.length());
            if (size.compareTo(fileLength) != 0) {
                throw new ApplicationException("上传文件大小错误[" + size.toString() + "," + fileLength.toString() + "],请联系管理员!");
            }
            if (fileInfo.deleteOld()) {
                this.attachmentApplication.deleteAttachmentsByBizId(fileInfo.getBizCode(), fileInfo.getBizId(), false);
            }
            fileInfo.setSize(newFile.length() + "");

            Attachment attachment = new Attachment();

            attachment.setBizKindId(fileInfo.getBizCode());
            attachment.setBizSubKindId(fileInfo.getAttachmentCode());
            attachment.setBizId(fileInfo.getBizId());
            attachment.setPath(fileInfo.getSavePath());
            attachment.setFileName(fileInfo.getName());
            attachment.setFileSize(FileHelper.formetFileSize(ClassHelper.convert(fileInfo.getSize(), Long.class)));
            attachment.setFileKind(fileInfo.getExt());
            attachment.setCreator(Creator.newInstance());
            attachment.setStatus(Attachment.Status.NORMAL.getId());
            attachment.setUploadKind(Attachment.UploadKind.WEB.name());
            attachment.setIsMore(fileInfo.getIsMore());
            return this.attachmentApplication.saveAttachment(attachment);

        } catch (Exception ex) {
            // 删除物理文件
            FileHelper.deleteFile(fileInfo.getSavePath());
            throw new ApplicationException(ex);
        }
        // return null;
    }

    /**
     * 清理分片上传的相关数据
     * 文件夹，tmp文件
     * 
     * @param folder
     *            文件夹名称
     * @param path
     *            上传文件根路径
     * @return
     */
    private boolean cleanSpace(String folder, String path) {
        // 删除分片文件夹
        File garbage = new File(path + folder);
        if (!garbage.delete()) {
            return false;
        }
        // 删除tmp文件
        garbage = new File(path, folder + ".tmp");
        if (!garbage.delete()) {
            return false;
        }
        return true;
    }

    /**
     * 获取指定文件的所有分片
     * 
     * @param folder
     *            文件夹路径
     * @return
     */
    private File[] getChunks(String folder) {
        File targetFolder = new File(folder);
        if (!targetFolder.exists()) {
            return null;
        }
        if (!targetFolder.isDirectory()) {
            return null;
        }
        return targetFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                return true;
            }
        });
    }

    /**
     * 获取指定文件的分片数量
     * 
     * @param folder
     *            文件夹路径
     * @return
     */
    private int getChunksNum(String folder) {
        File[] filesList = this.getChunks(folder);
        if (filesList == null) {
            return 0;
        }
        return filesList.length;
    }

    /**
     * 创建存放上传的文件的文件夹
     * 
     * @param file
     *            文件夹路径
     * @return
     */
    private boolean createFileFolder(String file, boolean hasTmp) {
        // 创建存放分片文件的临时文件夹
        File tmpFile = new File(file);
        if (!tmpFile.exists()) {
            try {
                tmpFile.mkdir();
            } catch (SecurityException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        if (hasTmp) {
            // 创建一个对应的文件，用来记录上传分片文件的修改时间，用于清理长期未完成的垃圾分片
            tmpFile = new File(file + ".tmp");
            if (tmpFile.exists()) {
                tmpFile.setLastModified(System.currentTimeMillis());
            } else {
                try {
                    tmpFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 为上传的文件生成随机名称
     * 
     * @param ext
     *            文件的原始名称，主要用来获取文件的后缀名
     * @return
     */
    private String randomFileName(String extName) {
        String ext[] = extName.split("\\.");
        String newFileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + "." + ext[ext.length - 1];
        return newFileName;
    }
}