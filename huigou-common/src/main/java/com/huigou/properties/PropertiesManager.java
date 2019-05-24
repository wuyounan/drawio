package com.huigou.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import com.huigou.exception.ResourceLoadException;
import com.huigou.util.ResourceLoadManager;

/**
 * Properties 文件读取管理
 * 
 * @author xx
 */
public class PropertiesManager extends ResourceLoadManager<PropertiesModel> implements PropertiesReader {

    @Override
    public PropertiesModel loadConfigFile(String path) throws ResourceLoadException {
        Properties props = new Properties();
        InputStream inputStream = null;
        BufferedReader bf = null;
        try {
            ClassPathResource resource = getResource(path);
            inputStream = resource.getInputStream();
            /**
             * 由于字节流是无法读取中文的，所以采取reader把inputStream转换成reader用字符流来读取中文
             * 其中“UTF-8”，用于明确指定.properties文件的编码格式（不指定则默认使用OS的，这会造成同一份配置文件同一份代码，在linux和windows上、英文windows和中文windows之间的表现都不一致）
             **/
            bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            props.load(bf);
            PropertiesModel propertiesModel = new PropertiesModel(props);
            propertiesModel.setVersions(resource.lastModified());
            propertiesModel.setConfigFilePath(path);
            return propertiesModel;
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("读取配置文件[%s]失败:%s", path, e.getMessage()));
        } finally {
            try {
                if (null != bf) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
