<#if parameters.list?? && parameters.list?size gt 0> 
<ul class="hg-form-list inline"><#rt/>
	<#list parameters.list?keys as listKey>
      <#assign itemKey = listKey?string />
      <#assign itemValue = parameters.list[listKey]?string />
      <li><label><#rt/>
		<input type="radio"<#rt/>
		<#if parameters.name??>
		 name="${parameters.name?html}"<#rt/>
		</#if>
		 id="${parameters.id?html}${itemKey?html}"<#rt/>
		<#if parameters.nameValue??>
		  <#if parameters.nameValue==itemKey>
	 		checked="checked"<#rt/>
		  </#if>
		</#if>
		<#if itemKey??>
		 value="${itemKey?html}"<#rt/>
		</#if>
		<#if parameters.disabled?default(false)>
		 disabled="disabled"<#rt/>
		</#if>
		<#include "/simple/css.ftl" />
		/>&nbsp;<#rt/>
		${itemValue}<#rt/>
		</label></li>
	</#list> 
</ul><#rt/>
</#if>