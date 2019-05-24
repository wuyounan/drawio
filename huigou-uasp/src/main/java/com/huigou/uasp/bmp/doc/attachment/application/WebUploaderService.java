package com.huigou.uasp.bmp.doc.attachment.application;

import java.io.File;

import com.huigou.uasp.bmp.doc.attachment.domain.model.FileInfo;


public interface WebUploaderService {

    /**
     * 为上传的文件创建对应的保存位置
     * 若上传的是分片，则会创建对应的文件夹结构和tmp文件
     * 
     * @return
     */
    File getReadySpace(FileInfo info);

    /**
     * 分片验证
     * 验证对应分片文件是否存在，大小是否吻合
     * 
     * @return
     */
    boolean chunkCheck(FileInfo info);

    /**
     * 分片合并操作
     * 要点:
     * > 合并: NIO
     * > 并发锁: 避免多线程同时触发合并操作
     * > 清理: 合并清理不再需要的分片文件、文件夹、tmp文件
     * 
     * @return
     */
    File chunksMerge(FileInfo info);

    /**
     * 目标文件path的映射关系存入持久层
     * 
     * @return
     */
    String saveFileMap(FileInfo info, File file);
}