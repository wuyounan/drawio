<button type="button" class="btn ${parameters.type?default("btn-primary")?html}" 
<#if parameters.id??>
 id="${parameters.id?html}"<#rt/>
</#if>
<#if parameters.disabled?default(false)>
 disabled="disabled"<#rt/>
</#if>
<#if parameters.onclick??>
 onclick="${parameters.onclick?html}"<#rt/>
</#if>
><#rt/>
<#if parameters.icon??>
	<i class="fa ${parameters.icon?html}"></i>&nbsp;<#rt/>
</#if><#rt/>
${parameters.value?default("")?html}<#rt/>
</button><#rt/>