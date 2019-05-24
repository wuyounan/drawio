<#if parameters.taskDetail?exists>
<div class="ui-hide" id="pageTaskDetailHidden">
	<#list parameters.taskDetail?keys as key>
	<input type="hidden" name="${key}" id="jobPage${key?cap_first}" value="${parameters.taskDetail[key]?default("")}"/>
    </#list>  
</div>
</#if>