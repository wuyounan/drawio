<#setting number_format="#">
<#-- 定义宏ProcUnitTask -->
<#macro ProcUnitTask task group index>
 <tr><#rt/>
 <#if index==0>
 	<td rowspan="${group.rowSpan}" class="title" style="text-align:center;">${group.groupName}</td><#rt/>
 </#if>
	<td  colspan="6"><#rt/>
    <div class="left">${task.opinion?html}</div><#rt/>
    <#-- 添加评论数据 -->
    <#if task.comments?? && task.comments?size gt 0>
    <#list task.comments as comment>
		<div class="comment" style="color:#555555;font-style:italic;"><b>&nbsp;@${comment.userId}&nbsp;${comment.time?string("yyyy-MM-dd HH:mm:ss")}&nbsp;:&nbsp;</b>${comment.fullMessage}</div><#rt/>
	</#list>
    </#if>
    <#if task.manuscript??>
    <img class="manuscript" style='height:60px;width:100%;' id="img_${task.id}" src="${task.manuscript?html}"></img><#rt/>
    </#if>
    </td><#rt/>
</tr><#rt/>
<tr style="<@getFontStyle task=task/>"><#rt/>
	<#if taskHasResult?default(true)>
		<td class="title">处理结果&nbsp;:</td><#rt/>
	    <td class="left">${task.resultDisplayName?html}</td><#rt/>
    </#if>
    <td class="title">处理人&nbsp;:</td><#rt/>
    <td class="left">${task.handlerName?html}</td><#rt/>
    <#if taskHandledDate?default(true)>
	    <td class="title">办理时间&nbsp;:</td><#rt/>
	    <td class="left">${task.handledDate?html}</td><#rt/>
	<#else>
		<td style="border-right:0px;">&nbsp;</td><#rt/>
		<td>&nbsp;</td><#rt/>
    </#if>
    <#if !taskHasResult?default(true)>
    	<td style="border-right:0px;">&nbsp;</td><#rt/>
		<td>&nbsp;</td><#rt/>
    </#if>
</tr><#rt/>
</#macro> 
<#-- 定义宏getFontStyle 修改字体显示颜色-->
<#macro getFontStyle task>
<#if task.cooperationModelId=='assistant'>
color:#666666;font-style:italic;
</#if>
</#macro> 
<#if parameters.taskExecutionList?? && parameters.taskExecutionList?size gt 0>
<table cellspacing="0px" cellpadding="0px" class="tableBorder"><#rt/>
<colgroup><col width='13%'/><col width='13%'/><col width='15%'/><col width='13%'/><col width='15%'/><col width='13%'/><col width='18%'/></colgroup>
<#list parameters.taskExecutionList as group>
<#list group.handlers as handler>
	<#if includeSubProcUnit(handler.subProcUnitId)==1 && excludeSubProcUnit(handler.subProcUnitId)==0>
		<@ProcUnitTask task=handler group=group index=handler_index/>
	</#if>
</#list>
</#list>
</table><#rt/>
</#if>
<#-- 需要包含显示的子环节id-->
<#function includeSubProcUnit subProcUnitId>
    <#if includeSubProcUnitIds?? && includeSubProcUnitIds!="">
    	<#if subProcUnitId?? && subProcUnitId!="">
    		 <#list includeSubProcUnitIds?split(",") as includeId>
		         <#if (subProcUnitId!"defaultValue") == includeId>
		         	<#return 1>
		         </#if>
		     </#list>
    	</#if>
    	<#return 0>
    </#if>
    <#return 1>
</#function>
<#-- 需要排除显示的子环节id-->
<#function excludeSubProcUnit subProcUnitId>
    <#if excludeSubProcUnitIds?? && excludeSubProcUnitIds!="">
    	<#if subProcUnitId?? && subProcUnitId!="">
    		 <#list excludeSubProcUnitIds?split(",") as excludeId>
		         <#if (subProcUnitId!"defaultValue") == excludeId>
		         	<#return 1>
		         </#if>
		     </#list>
    	</#if>
    	<#return 0>
    </#if>
    <#return 0>
</#function>