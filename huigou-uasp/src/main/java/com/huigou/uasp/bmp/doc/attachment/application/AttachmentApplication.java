package com.huigou.uasp.bmp.doc.attachment.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.doc.attachment.domain.model.AttachmentConfiguration;
import com.huigou.uasp.bmp.doc.attachment.domain.query.AttachmentConfigurationDesc;

public interface AttachmentApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/attachment.xml";

    /**
     * 保存附件配置
     * 
     * @param attachmentConfiguration
     *            附件配置实体
     * @return
     *         附件配置ID
     */
    String saveAttachmentConfiguration(AttachmentConfiguration attachmentConfiguration);

    /**
     * 加载附件配置
     * 
     * @param id
     *            附件配置ID
     * @return
     */
    AttachmentConfiguration loadAttachmentConfiguration(String id);

    /**
     * 删除附件配置
     * 
     * @param ids
     *            附件配置ID列表
     */
    void deleteAttachmentConfigurations(List<String> ids);

    /**
     * 移动附件配置
     * 
     * @param ids
     *            附件配置ID列表
     * @param folderId
     *            文件夹ID
     */
    void moveAttachmentConfigurations(List<String> ids, String folderId);

    /**
     * 分页查询附件配置
     * 
     * @param queryRequest
     *            查询参数
     * @return
     */
    Map<String, Object> slicedQueryAttachmentConfigurations(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 删除附件配置明细
     * 
     * @param attachmentConfigurationId
     *            附件配置ID
     * @param ids
     *            附件配置明细ID列表
     */
    void deleteAttachmentConfigurationDetails(String attachmentConfigurationId, List<String> ids);

    /**
     * 分页查询附件配置 明细
     * 
     * @param queryRequest
     *            查询请求参数
     * @return
     */
    Map<String, Object> slicedQueryAttachmentConfigurationDetails(ParentIdQueryRequest queryRequest);

    /**
     * 保存附件
     * 
     * @param attachment
     * @return
     */
    String saveAttachment(Attachment attachment);

    /**
     * 删除附件
     * 
     * @param id
     *            附件ID
     * @param verifyCreator
     *            验证创建人员
     */
    void deleteAttachment(String id, Boolean verifyCreator);

    /**
     * 根据ID列表删除附件
     * 
     * @param ids
     *            附件ID列表
     * @param verifyCreator
     *            验证创建人员
     */
    void deleteAttachmentsByIds(List<String> ids, Boolean verifyCreator);

    /**
     * 通过业务ID删除附件
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizId
     *            业务ID
     * @param verifyCreator
     *            验证创建人员
     */
    void deleteAttachmentsByBizId(String bizKindId, String bizId, boolean verifyCreator);

    /**
     * 加载附件
     * 
     * @param id
     *            附件ID
     * @return
     */
    Attachment loadAttachment(String id);

    /**
     * 保存附件排序号
     * 
     * @param params
     */
    void updateAttachmentsSequence(Map<String, Integer> params);

    /**
     * 查询附件
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizId
     *            业务ID
     * @return
     */
    List<Attachment> queryAttachments(String bizKindId, String bizId);

    /**
     * 查询分组的附件
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizId
     *            业务ID
     * @return
     */
    List<AttachmentConfigurationDesc> queryGroupedAttachments(String bizKindId, String bizId);

    /**
     * 附件是否存在
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizSubKindId
     *            子业务类别ID
     * @param bizId
     *            业务ID
     * @return
     */
    boolean attachmentExists(String bizKindId, String bizSubKindId, String bizId);

    /**
     * 附件是否存在
     * 
     * @param bizKindId
     * @param bizId
     * @return
     */
    boolean attachmentExists(String bizKindId, String bizId);

    /**
     * 按照附件ID附件复制
     * 
     * @param fileId
     * @param toBizCode
     * @param toBizIds
     * @param newOperator
     *            false使用附件本身的上传用户
     * @param isCopyFile
     *            是否拷贝一份新文件
     */
    void copyAttachmentById(String fileId, String toBizCode, List<String> toBizIds, boolean newOperator, boolean isCopyFile);

    /**
     * 按照附件ID附件复制
     * 
     * @param fileId
     * @param toBizCode
     * @param toBizId
     */
    void copyAttachmentById(String fileId, String toBizCode, String toBizId);

    /**
     * 附件复制
     * 
     * @param fromBizCode
     * @param formBizId
     * @param toBizCode
     * @param toBizId
     * @param newOperator
     *            false使用附件本身的上传用户
     * @param isCopyFile
     *            是否拷贝一份新文件
     */
    void copyAttachment(String fromBizCode, String formBizId, String toBizCode, String toBizId, boolean newOperator, boolean isCopyFile);

    /**
     * 附件复制
     * 
     * @param fromBizCode
     * @param formBizId
     * @param toBizCode
     * @param toBizId
     */
    void copyAttachment(String fromBizCode, String formBizId, String toBizCode, String toBizId);

}
