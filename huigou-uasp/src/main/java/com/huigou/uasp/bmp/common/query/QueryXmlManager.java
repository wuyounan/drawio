package com.huigou.uasp.bmp.common.query;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import com.huigou.data.query.QueryXmlLoadInterface;
import com.huigou.data.query.QueryXmlModel;
import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.query.QueryMappingsDocument;
import com.huigou.util.ResourceLoadManager;
/**
 * 查询模型管理类
 * 
 * @author xx
 */
public class QueryXmlManager extends ResourceLoadManager<QueryXmlModel> implements QueryXmlLoadInterface {

    /**
     * 加载配置文件
     */
    public QueryXmlModel loadConfigFile(String path) throws ResourceLoadException {
        InputStream inputStream = null;
        try {
            ClassPathResource resource = getResource(path);
            inputStream = resource.getInputStream();
            QueryMappingsDocument doc = QueryMappingsDocument.Factory.parse(inputStream);
            QueryXmlModel model = new QueryXmlModel(doc.getQueryMappings());
            model.setVersion(resource.lastModified());// 文件最后修改时间
            model.setConfigFilePath(path);
            return model;
        } catch (Exception e) {
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