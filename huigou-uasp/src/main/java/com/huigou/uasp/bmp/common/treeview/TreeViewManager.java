package com.huigou.uasp.bmp.common.treeview;

import com.huigou.exception.ApplicationException;
import com.huigou.exception.NotFoundException;
import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.common.treeview.domain.TreeViewLoadInterface;
import com.huigou.uasp.bmp.common.treeview.domain.model.TreeViewMappingModel;
import com.huigou.uasp.bmp.treeview.TreeMappingsDocument;
import com.huigou.util.ResourceLoadManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树配置管理
 *
 * @author Gerald
 */
public class TreeViewManager extends ResourceLoadManager<TreeViewMappingModel> implements TreeViewLoadInterface {

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
    public TreeViewMappingModel loadConfigFile(String type) throws NotFoundException {
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

            List<TreeMappingsDocument.TreeMappings> mappings = xmlStreams.stream().map(is -> {
                try {
                    return TreeMappingsDocument.Factory.parse(is).getTreeMappings();
                } catch (XmlException | IOException e) {
                    throw new ApplicationException(e);
                }
            }).collect(Collectors.toList());

            TreeViewMappingModel model = new TreeViewMappingModel(mappings);
            model.setVersions(maxLastModified(resources));
            model.setConfigFilePaths(paths);
            return model;
        } catch (Exception e) {
            throw new ResourceLoadException(String.format("读取配置文件[%s]失败:%s", defaultPath, e.getMessage()));
        } finally {
            xmlStreams.forEach(IOUtils::closeQuietly);
        }
    }
}