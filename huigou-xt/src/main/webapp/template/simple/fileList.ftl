<#setting number_format="#">
<div class="ui-attachment-list" id="${parameters.id?default("")?html}"<#rt/>
<#if parameters.readonly?default(false)>
 readonly="true"<#rt/>
</#if>
 bizCode="${parameters.bizKindId?default("")?html}" bizId="${parameters.bizId?default("")?html}"><#rt/>
<#if parameters.isWrap?default(true)>
<div class="ui-attachment-title"><#rt/>
	 <h5><i class="fa fa-file-text"></i>&nbsp;${parameters.title?html}</h5><#rt/>
	 <div class="ui-attachment-tools"><#rt/>
         <a class="btn btn-warning btn-xs toggle"><i class="fa fa-chevron-up"></i></a><#rt/>
         <a class="btn btn-info btn-xs moreList" title="${MessageSourceContext.getMessage('common.attachment.more')}"><i class="fa fa-edit"></i>${MessageSourceContext.getMessage('common.button.view')}</a><#rt/>
         <a class="btn btn-primary btn-xs addFile" title="${MessageSourceContext.getMessage('common.attachment.tip')}"><i class="fa fa-plus-circle"></i>${MessageSourceContext.getMessage('common.button.upload')}</a><#rt/>
     	 <#if parameters.batchUpload?default(false)>
     	 <a class="btn btn-success btn-xs hidden-xs addBatchFile" title="${MessageSourceContext.getMessage('common.attachment.tip')}"><i class="fa fa-cloud-upload"></i>${MessageSourceContext.getMessage('common.button.batchupload')}</a><#rt/>
     	 </#if>
     </div><#rt/>
</div><#rt/>
</#if><#rt/>
<div class="ui-attachment-content fileListMain"><#rt/>
<#if parameters.fileList?? && parameters.fileList?size gt 0> 
<#list parameters.fileList as fileObj>
<div class="file" id="${fileObj.id?html}" fileKind="${fileObj.fileKind?html}" attachmentCode="${fileObj.bizSubKindId?html}" createdByName="${fileObj.creator.createdByName?html}" fileSize="${fileObj.fileSize?html}" createdDate="${fileObj.creator.createdDate?html}"><#rt/>
<span class="${fileObj.fileKind?html}">&nbsp;</span>&nbsp;<a href="javascript:void(0);" hidefocus title="${fileObj.fileName?html}">${fileObj.fileName?html}</a></div><#rt/>
</#list>
</#if>
</div><#rt/>
<iframe name="iframe_${parameters.id?default("")?html}" style="display:none;"></iframe><#rt/>
</div><#rt/>