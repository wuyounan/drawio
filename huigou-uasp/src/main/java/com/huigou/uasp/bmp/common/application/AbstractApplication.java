package com.huigou.uasp.bmp.common.application;


import java.util.List;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;

public class AbstractApplication {
    
    protected void checkIdNotBlank(String id){
        checkItemNotBlank(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
    }
    
    protected void checkIdsNotEmpty(List<String> ids){
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
    }
    
    protected void checkParentIdNotBlank(String parentId){
        checkItemNotBlank(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
    }
    
    protected void checkFolderIdNotBlank(String folderId){
        checkItemNotBlank(folderId, MessageSourceContext.getMessage(MessageConstants.FOLDER_ID_NOT_BLANK));
    }
    
    protected void checkItemNotBlank(String itemName, String message){
        Assert.hasText(itemName, message);
    }

    protected String getParamIsNullMessage(String paramName){
        return String.format(MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, paramName));
    }
    
}
