<#setting number_format="#">
<#assign pageIsReadOnly=false />
<#if parameters.isReadOnly?? && parameters.isReadOnly>
<#assign pageIsReadOnly=true />
</#if>
<#-- 定义宏ProcUnitTask -->
<#macro ProcUnitTask task>
<#if !pageIsReadOnly&&!task.readonly?default(true)>
<div class="div-bordered-row<@getBackGroundColor task=task/>"><#rt/>
	<div class="col-xs-12 white-bg"><#rt/>
	 	<textarea id="handleOpinion" style='height:36px;' maxLength="500">${task.opinion?html}<#rt/></textarea><#rt/>
	 	<input type="hidden" id="currentHandleId" name="id" value="${task.id}"/>
	    <input type="hidden" id="currentHandleGroupId" name="groupId" value="${task.groupId}"/>
	    <input type="hidden" id="currentHandleSequence" name="sequence" value="${task.sequence}"/>
	    <input type="hidden" id="currentHandleCooperationModelId" name="cooperationModelId" value="${task.cooperationModelId}"/>
	    <input type="hidden" id="currentHandleTaskExecuteModeId" name="taskExecuteModeId" value="${task.taskExecuteModeId}"/>
	    <input type="hidden" id="currentHandleAllowAdd" name="allowAdd" value="${task.allowAdd}"/>
	    <input type="hidden" id="currentHandleAllowTransfer" name="allowTransfer" value="${task.allowTransfer}"/>
	    <input type="hidden" id="currentHandleAllowAbort" name="allowAbort" value="${task.allowAbort}"/>
	    <input type="hidden" id="currentHandleHelpSection" name="helpSection" value="${task.helpSection}"/>
	    <input type="hidden" id="currentHandleChiefId" name="chiefId" value="${task.chiefId}"/>
	    <input type="hidden" id="currentApprovalRuleId" name="approvalRuleId" value="${task.approvalRuleId}"/>
	    <input type="hidden" id="currentSubProcUnitId" name="handleKindId" value="${task.subProcUnitId}"/>
	    <#-- 添加评论数据 -->
	    <#if task.comments?? && task.comments?size gt 0>
		    <hr/>
		    <#list task.comments as comment>
				<div class="comment"><b>&nbsp;${comment.userId}&nbsp;[${comment.time?string("yyyy-MM-dd HH:mm:ss")}]&nbsp;:&nbsp;</b>${comment.fullMessage}</div><#rt/>
			</#list>
	    </#if>
	    <#if task.manuscript??>
	    	<div><img  class="manuscript" id="img_${task.id}" src="${task.manuscript?html}"></img></div><#rt/>
	    </#if>
	 </div><#rt/>
</div><#rt/>
<#else>
<div class="div-bordered-row<@getBackGroundColor task=task/><#if task.statusId??&&task.statusId!='ready'> opinionTextRow</#if>" id="opinionTextRow${task.id?html}" handlerName="${task.handlerName?html}"><#rt/>
   	<div class="col-xs-12"><#rt/>
	    <div class="ui-textarea-label">${task.opinion?html}</div><#rt/>
	    <input type="hidden" id="handleId" name="id" value="${task.id?html}"/>
	    <#-- 添加评论数据 -->
	    <#if task.comments?? && task.comments?size gt 0>
		    <#list task.comments as comment>
				<div class="comment"><b>&nbsp;${comment.userId}&nbsp;[${comment.time?string("yyyy-MM-dd HH:mm:ss")}]&nbsp;:&nbsp;</b>${comment.fullMessage}</div><#rt/>
			</#list>
	    </#if>
	    <#if task.manuscript??>
	    	<div><img class="manuscript" id="img_${task.id}" src="${task.manuscript?html}"></img></div><#rt/>
	    </#if>
    </div><#rt/>
</div><#rt/>
</#if>
<div class="div-bordered-row<@getFontStyle task=task/><@getBackGroundColor task=task/><@getCurrentProcUnit task=task/>"><#rt/>
	<#if parameters.hasResult?default(true)>
		<#if !pageIsReadOnly&&!task.readonly?default(true)>
			<div class="col-xs-4 col-sm-2 border-right"><#rt/>
				<label class="hg-form-label required">${MessageSourceContext.getMessage('common.field.handleresult')}&nbsp;:</label><#rt/>
			</div><#rt/>
			<div class="col-xs-8 col-sm-2 border-right white-bg"><#rt/>
				<#--取消 value="${task.result?html}" 的赋值解决打回或回退默认 不通过的问题  -->
				<input type="text" id="handleResult" name="jobPageResult" required="true" label="${MessageSourceContext.getMessage('common.field.handleresult')}" combo="true"  dataOptions="data:${parameters.handleResultData?default("")?html},readOnly:true"/><#rt/>
			</div><#rt/>
		<#else>
			<div class="col-xs-4 col-sm-2 border-right"><#rt/>
				<label class="hg-form-label">${MessageSourceContext.getMessage('common.field.handleresult')}&nbsp;:</label><#rt/>
			</div><#rt/>
			<div class="col-xs-8 col-sm-2 border-right"><#rt/>
				<div class='ui-text-label'>${task.resultDisplayName?html}</div><#rt/>
			</div><#rt/>
		</#if>
    </#if>
    <div class="col-xs-4 col-sm-2 border-right"><label class="hg-form-label">${MessageSourceContext.getMessage('common.field.handlername')}&nbsp;:</label></div><#rt/>
    <div class="col-xs-8 col-sm-2 border-right"><#rt/>
        <div class='ui-text-label'>${task.handlerName?html}</div><#rt/>
    </div><#rt/>
    <div class="col-xs-4 col-sm-2 border-right"><label class="hg-form-label">${MessageSourceContext.getMessage('common.field.handleddate')}&nbsp;:</label></div><#rt/>
    <div class="col-xs-8 col-sm-2"><#rt/>
        <div class='ui-text-label'>${task.handledDate?html}</div><#rt/>
    </div><#rt/>
    <#if !parameters.hasResult?default(true)>
    	<div class="col-xs-12 col-sm-4 hidden-xs">&nbsp;
    	<#if !pageIsReadOnly&&!task.readonly?default(true)><#rt/>
    		<input type="hidden" id="handleResult" name="jobPageResult" value="1"/>
    	</#if>
    	</div><#rt/>
    </#if>
</div><#rt/>
</#macro> 
<#-- 定义宏getFontStyle 修改字体显示颜色-->
<#macro getFontStyle task>
<#if task.cooperationModelId=='assistant'>
 comment<#rt/>
</#if>
</#macro> 
<#-- 定义宏getCurrentProcUnit当前处理环节样式-->
<#macro getCurrentProcUnit task>
<#if task.isCurrentProcUnit?default(false)>
 current-proc-unit<#rt/>
</#if>
</#macro>
<#-- 定义宏getBackGroundColor修改显示背景色-->
<#macro getBackGroundColor task>
 <#if task.taskIndex?default(0)%2==0>
 list-row-bg-a<#rt/>
 <#else>
 list-row-bg-b<#rt/>
 </#if>
</#macro>
<#if parameters.taskExecutionList?? && parameters.taskExecutionList?size gt 0>
<div class="job-task-execution div-bordered-cols border-left-top" data-proc-unit-id="${parameters.procUnitId?default("")?html}">
	<#list parameters.taskExecutionList as group>
		<div class="div-bordered-row border-bottom gray-bg"><#rt/>
			<div class="col-xs-2 div-bordered-title">${group.groupName}</div><#rt/>
			<div class="col-xs-10 border-left-right"><#rt/>
			<#list group.handlers as handler>
		  	 <@ProcUnitTask task=handler/>
			</#list>
			</div><#rt/>
		</div><#rt/>
	</#list>
</div><#rt/>
</#if>