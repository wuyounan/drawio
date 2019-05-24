package com.huigou.uasp.bmp.common.easysearch;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import com.huigou.exception.NotFoundException;
import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.common.easysearch.domain.EasySearchLoadInterface;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchMappingModel;
import com.huigou.uasp.bmp.easysearch.EasySearchMappingsDocument;
import com.huigou.util.ResourceLoadManager;

/**
 * 快捷查询配置文件加载管理类
 * 
 * @author xx
 */
public class EasySearchManager extends ResourceLoadManager<EasySearchMappingModel> implements EasySearchLoadInterface {

    /**
     * 加载配置文件
     */
    public EasySearchMappingModel loadConfigFile(String type) throws NotFoundException {
        String path = String.format(XML_PATH, type);
        InputStream inputStream = null;
        try {
            ClassPathResource resource = getResource(path);
            inputStream = resource.getInputStream();
            EasySearchMappingsDocument doc = EasySearchMappingsDocument.Factory.parse(inputStream);
            EasySearchMappingModel mappingModel = new EasySearchMappingModel(doc.getEasySearchMappings());
            mappingModel.setVersions(resource.lastModified());
            mappingModel.setConfigFilePath(path);
            return mappingModel;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceLoadException(String.format("读取配置文件[%s]失败:%s", path, e.getMessage()));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}