<ul class="hg-form-list inline"><#rt/>
<#if parameters.list?? && parameters.list?size gt 0> 
	<#list parameters.list as item>
      <#assign itemKey = item['id']?string />
      <#assign itemName = item['name']?string />
      <#assign itemChecked = item['checked']?default(false) />
      <li><label><#rt/>
		<input type="checkbox"<#rt/>
		<#if parameters.name??>
		 name="${parameters.name?html}"<#rt/>
		</#if>
		 id="${parameters.id?html}${itemKey?html}"<#rt/>
		<#if itemChecked?default(false)>
	 	checked="checked"<#rt/>
		</#if>
		value="${itemKey?html}"<#rt/>
		<#if parameters.disabled?default(false)>
		 disabled="disabled"<#rt/>
		</#if>
		/>&nbsp;<#rt/>
		${itemName}<#rt/>  
		</label></li>
	</#list> 
</#if>
</ul><#rt/>