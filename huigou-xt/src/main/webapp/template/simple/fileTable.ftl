<#setting number_format="#">
	<div class="ui-attachment-list hg-form clear"  isClass="true" id="${parameters.id?default("")?html}"<#rt/>
	<#if parameters.readonly?default(false)>
		readonly="true"<#rt/>
	</#if>
	bizCode="${parameters.bizKindId?default("")?html}" bizId="${parameters.bizId?default("")?html}"><#rt/>
	<#if !parameters.inTable?default(false)>
	<div class="hg-form-cols"><#rt/>	
	</#if>
	<#if parameters.groupList?? && parameters.groupList?size gt 0>
		<#assign cols=12/>
		<#assign index=0 />
		<#assign labelspan=2 />
		<#list parameters.groupList as group>
			<#assign colspan=group.colspan />
			<#if colspan lt 2>
				<#assign colspan=2 />
			</#if>
			<#if index%cols==0>
				<#if index==cols>
					</div><#rt/>
					<#assign index=0 />
				</#if>
				<div class="hg-form-row"><#rt/>
			</#if>
			<#if (index+labelspan+colspan) gt cols>
				<#assign colspan=cols-labelspan-index />
			<#elseif (index+labelspan+colspan) == (cols-labelspan)>
				<#assign colspan=colspan+labelspan />
			</#if>
			<#assign index=index+labelspan+colspan />
			<#if index gt cols>
				<#assign index=cols /><#rt/>
			</#if>
			<div class="col-xs-4 col-sm-${labelspan}"><#rt/>
				<span class="addFieldFloat">
					<a class="btn btn-primary btn-xs addFieldGroup" <#if parameters.readonly?default(false)>style="display:none;"</#if> title="${MessageSourceContext.getMessage('common.attachment.tip')}" isMore="${group.allowMultiple?html}" attachmentCode="${group.code?html}" filetype="${group.fileKind?html}"><i class="fa fa-plus-circle"></i></a><#rt/>
				</span>
				<label class="hg-form-label" id="code_label">${group.name?html}&nbsp;:</label><#rt/>
			</div><#rt/>
			<div class="col-xs-8 col-sm-${colspan} col-white-bg"><#rt/>
				<div class="groupFileList ${group.code?html}" attachmentCode="${group.code?html}" isMore="${group.allowMultiple?html}" id="groupFileList${group.detailId}">
					<#if group.attachments?? && group.attachments?size gt 0>
					<div class="groupFileMore">
						<a class="btn btn-info btn-xs moreList" title="${MessageSourceContext.getMessage('common.attachment.more')}"><i class="fa fa-edit"></i>${MessageSourceContext.getMessage('common.button.view')}</a>
					</div>
					<#list group.attachments as attachment>
					<@getFileView attachment=attachment/>
					</#list>
					</#if>
				</div>
			</div><#rt/>
		</#list>
		<#if cols-index gt 0>
			<div class="col-xs-8 col-sm-${cols-index} hidden-xs">&nbsp;</div><#rt/>
		</#if>
		</div><#rt/>
	</#if>
	<#if !parameters.inTable?default(false)>
	</div><#rt/>	
	</#if>
	<iframe name="iframe_${parameters.id?default(" ")?html}" style="display:none;"></iframe><#rt/>
	</div><#rt/>
	<#-- 定义宏getFileView -->
	<#macro getFileView attachment> 
		<div class="file" id="${attachment.id?html}" fileKind="${attachment.fileKind?html}" attachmentCode="${attachment.bizSubKindId?html}" createdByName="${attachment.creator.createdByName?html}" fileSize="${attachment.fileSize?html}" createdDate="${attachment.creator.createdDate?html}"><#rt/>
			<span class="${attachment.fileKind?html}">&nbsp;</span><#rt/>
			<a href="javascript:void(0);" hidefocus title="${attachment.fileName?html}">${attachment.fileName?html}</a><#rt/>
		</div><#rt/>
	</#macro>