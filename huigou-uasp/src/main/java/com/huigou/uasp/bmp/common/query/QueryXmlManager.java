package com.huigou.uasp.bmp.common.query;

import com.huigou.data.query.QueryXmlLoadInterface;
import com.huigou.data.query.QueryXmlModel;
import com.huigou.exception.ApplicationException;
import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.query.QueryMappingsDocument;
import com.huigou.util.ResourceLoadManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询模型管理类
 *
 * @author xx
 */
public class QueryXmlManager extends ResourceLoadManager<QueryXmlModel> implements QueryXmlLoadInterface {

    /**
     * SQL 方言。
     *
     * @since 1.1.3
     */
    private String sqlDialect;

    /**
     * 设置SQL方言。
     *
     * @since 1.1.3
     */
    public void setSqlDialect(String sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    /**
     * 加载配置文件
     */
    @Override
    public QueryXmlModel loadConfigFile(String path) throws ResourceLoadException {
        List<InputStream> xmlStreams = Collections.emptyList();
        try {
            List<String> paths = new ArrayList<>(2);
            // 方言xml放在最前面，从而达到优先使用方言xml的目的
            if (StringUtils.isNotBlank(sqlDialect)) {
                paths.add(getDialectXmlPath(path));
            }
            paths.add(path);
            List<Resource> resources = paths.stream()
                    .map(ClassPathResource::new)
                    .filter(Resource::exists)
                    .collect(Collectors.toList());

            xmlStreams = resources.stream()
                    .map(xml -> {
                        try {
                            return xml.getInputStream();
                        } catch (IOException ioe) {
                            throw new ApplicationException(ioe);
                        }
                    })
                    .collect(Collectors.toList());

            List<QueryMappingsDocument.QueryMappings> mappings = xmlStreams.stream().map(is -> {
                try {
                    return QueryMappingsDocument.Factory.parse(is).getQueryMappings();
                } catch (XmlException | IOException e) {
                    throw new ApplicationException(e);
                }
            }).collect(Collectors.toList());

            QueryXmlModel model = new QueryXmlModel(mappings);
            model.setVersion(maxLastModified(resources));
            model.setConfigFilePaths(paths);
            return model;
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("读取配置文件[%s]失败:%s", path, e.getMessage()));
        } finally {
            xmlStreams.forEach(IOUtils::closeQuietly);
        }
    }

    private String getDialectXmlPath(String path) {
        Path filePath = Paths.get(path);
        Path dialectXmlPath = filePath.getParent().resolve(sqlDialect).resolve(filePath.getFileName());
        return dialectXmlPath.toString();
    }
}