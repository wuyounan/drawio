<input type = "hidden" name="${parameters.name?default("")?html}"<#rt/>
<#if parameters.id??>
 id="${parameters.id?html}"<#rt/>
</#if>
<#if parameters.nameValue??>
 value="${parameters.nameValue?default("")?html}"<#rt/>
</#if>
/><#rt/>
<div class="ui-text-label" id="${parameters.name?default("")?html}_view"
 title="<#rt/>
<#if parameters.showValue??>
	${parameters.showValue?html}<#rt/>         
<#else>
	<#if parameters.nameValue??>
	${parameters.nameValue?default("")?html}<#rt/>
	</#if>
</#if>
 "><#rt/>
<#if parameters.showValue??>
	${parameters.showValue?html}<#rt/>         
<#else>
	<#if parameters.nameValue??>
	${parameters.nameValue?default("")?html}<#rt/>
	</#if>
</#if>
</div><#rt/>