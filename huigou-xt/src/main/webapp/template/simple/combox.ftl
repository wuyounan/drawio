<#setting number_format="#">
<#if parameters.disabled?default(false)>
<#include "/simple/inputLable.ftl" />
<#else>
	<#setting number_format="#.#####">
	<select<#rt/>
	 name="${parameters.name?default("")?html}"<#rt/>
	<#if parameters.size??>
	 size="${parameters.size?html}"<#rt/>
	</#if>
	<#if parameters.disabled?default(false)>
	 disabled="disabled"<#rt/>
	</#if>
	<#if parameters.id??>
	 id="${parameters.id?html}"<#rt/>
	<#else>
	 id="${parameters.name?html}"<#rt/>
	</#if>
	<#include "/simple/css.ftl" />
	<#if parameters.required?default(false)>
	 required="true"<#rt/>
	</#if>
	<#if parameters.label??>
	 label="${parameters.label?default("")?html}"<#rt/>
	</#if>
	>
	<#if parameters.emptyOption?default(false)>
	    <option value=""></option>
	</#if>
	<#if parameters.list?? && parameters.list?size gt 0> 
	<#list parameters.list?keys as listKey>
      <#assign itemKey = listKey?string />
      <#assign itemValue = parameters.list[listKey]?string />
	  <option value="${itemKey?html}"<#rt/>
	  	<#if parameters.nameValue??>
		<#if parameters.nameValue==itemKey>
	 		selected="selected"<#rt/>
		</#if>
		</#if>
	  >${itemValue?html}</option><#lt/>
	</#list> 
	</#if>
	</select>
</#if>