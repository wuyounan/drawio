package com.huigou.uasp.bmp.common.query;

import com.huigou.data.query.QueryXmlLoadInterface;
import com.huigou.data.query.QueryXmlModel;
import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.query.QueryMappingsDocument;
import com.huigou.util.ResourceLoadManager;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 查询模型管理类
 *
 * @author xx
 */
public class QueryXmlManager extends ResourceLoadManager<QueryXmlModel> implements QueryXmlLoadInterface, InitializingBean {

    /**
     * SQL 方言。
     */
    private String sqlDialect;

    public void setSqlDialect(String sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    /**
     * 加载配置文件
     */
    @Override
    public QueryXmlModel loadConfigFile(String path) throws ResourceLoadException {
        InputStream inputStream = null, dialectXmlStream = null;
        try {
            // 默认SQL XML 文件
            ClassPathResource resource = getResource(path);
            // 方言SQL XML文件
            ClassPathResource dialectXml = getResource(getDialectXmlPath(path));
            Assert.isTrue(resource.exists() || dialectXml.exists(), "文件不存在");
            QueryMappingsDocument.QueryMappings queryMappings = null;
            if (resource.exists()) {
                inputStream = resource.getInputStream();
                queryMappings = QueryMappingsDocument.Factory.parse(inputStream).getQueryMappings();
            }
            QueryMappingsDocument.QueryMappings dialectQueryMappings = null;
            if (dialectXml.exists()) {
                dialectXmlStream = dialectXml.getInputStream();
                dialectQueryMappings = QueryMappingsDocument.Factory.parse(dialectXmlStream)
                        .getQueryMappings();
            }

            QueryXmlModel model = new QueryXmlModel(queryMappings, dialectQueryMappings);
            // 文件最后修改时间
            long version;
            if (resource.exists() && dialectXml.exists()) {
                version = resource.lastModified() > dialectXml.lastModified()
                        ? resource.lastModified()
                        : dialectXml.lastModified();
            } else {
                version = resource.exists() ? resource.lastModified() : dialectXml.lastModified();
            }
            model.setVersion(version);
            model.setConfigFilePath(path);
            return model;
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("读取配置文件[%s]失败:%s", path, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(dialectXmlStream);
        }
    }

    private String getDialectXmlPath(String path) {
        Path filePath = Paths.get(path);
        Path dialectXmlPath = filePath.getParent().resolve(sqlDialect).resolve(filePath.getFileName());
        return dialectXmlPath.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(sqlDialect, "sql方言不能为空");
    }
}