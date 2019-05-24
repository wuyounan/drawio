package com.huigou.uasp.bmp.common.treeview;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import com.huigou.exception.NotFoundException;
import com.huigou.uasp.bmp.common.treeview.domain.TreeViewLoadInterface;
import com.huigou.uasp.bmp.common.treeview.domain.model.TreeViewMappingModel;
import com.huigou.uasp.bmp.treeview.TreeMappingsDocument;
import com.huigou.util.ResourceLoadManager;

/**
 * 树配置管理
 * @author Gerald
 *
 */
public class TreeViewManager extends ResourceLoadManager<TreeViewMappingModel> implements TreeViewLoadInterface {

    /**
     * 加载配置文件
     * 
     * @Title: loadConfigFile
     * @author 
     * @Description: TODO
     * @param @param fileName
     * @param @return
     * @param @throws Exception
     * @return Domain
     * @throws
     */
    public TreeViewMappingModel loadConfigFile(String type) throws NotFoundException {
        InputStream inputStream = null;
        String path = String.format(XML_PATH, type);
        try {
            ClassPathResource resource = getResource(path);
            inputStream = resource.getInputStream();
            TreeMappingsDocument doc = TreeMappingsDocument.Factory.parse(inputStream);
            TreeViewMappingModel mappingModel = new TreeViewMappingModel(doc.getTreeMappings());
            mappingModel.setVersions(resource.lastModified());
            mappingModel.setConfigFilePath(path);
            return mappingModel;
        } catch (Exception e) {
            throw new NotFoundException("读取配置文件失败:" + e.getMessage());
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