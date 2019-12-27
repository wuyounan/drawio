package com.huigou.uasp.bmp.common.easysearch;

import com.huigou.exception.ApplicationException;
import com.huigou.exception.NotFoundException;
import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.common.easysearch.domain.EasySearchLoadInterface;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchMappingModel;
import com.huigou.uasp.bmp.easysearch.EasySearchMappingsDocument;
import com.huigou.util.ResourceLoadManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 快捷查询配置文件加载管理类
 *
 * @author xx
 */
public class EasySearchManager extends ResourceLoadManager<EasySearchMappingModel> implements EasySearchLoadInterface {

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

    @Override
    public EasySearchMappingModel loadConfigFile(String type) throws NotFoundException {
        List<String> paths = new ArrayList<>(2);
        // 方言xml放在最前面，从而达到优先使用方言xml的目的
        if (StringUtils.isNotBlank(sqlDialect)) {
            paths.add(String.format(DIALECT_XML_PATH, sqlDialect, type));
        }
        String defaultPath = String.format(XML_PATH, type);
        paths.add(defaultPath);
        List<InputStream> xmlStreams = Collections.emptyList();
        try {
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

            List<EasySearchMappingsDocument.EasySearchMappings> mappings = xmlStreams.stream().map(is -> {
                try {
                    return EasySearchMappingsDocument.Factory.parse(is).getEasySearchMappings();
                } catch (XmlException | IOException e) {
                    throw new ApplicationException(e);
                }
            }).collect(Collectors.toList());

            EasySearchMappingModel mappingModel = new EasySearchMappingModel(mappings);
            mappingModel.setVersions(maxLastModified(resources));
            mappingModel.setConfigFilePaths(paths);
            return mappingModel;
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("读取配置文件[%s]失败:%s", defaultPath, e.getMessage()));
        } finally {
            xmlStreams.forEach(IOUtils::closeQuietly);
        }
    }

}