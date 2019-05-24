<#if parameters.disabled?default(false)>
<#include "/simple/inputLable.ftl" />
<#else>
	<#if parameters.wrapper??>
	<div class="input-group ui-combox-wrap" id="${parameters.name?default("")?html}_click"><#rt/>
	</#if>
	<input<#rt/>
	 type="${parameters.type?default("text")?html}"<#rt/>
	 name="${parameters.name?default("")?html}"<#rt/>
	<#if parameters.size??>
	 size="${parameters.size?html}"<#rt/>
	</#if>
	<#if parameters.maxlength??>
	 maxlength="${parameters.maxlength?html}"<#rt/>
	</#if>
	<#if parameters.nameValue??>
	 value="${parameters.nameValue?default("")?html}"<#rt/>
	</#if>
	<#if parameters.readonly?default(false)>
	 readonly="readonly"<#rt/>
	</#if>
	<#if parameters.tabindex??>
	 tabindex="${parameters.tabindex?html}"<#rt/>
	</#if>
	<#if parameters.id??>
	 id="${parameters.id?html}"<#rt/>
	<#else>
	 id="${parameters.name?html}"<#rt/>
	</#if>
	<#include "/simple/css.ftl" />
	<#if parameters.mask??>
	 mask="${parameters.mask?html}"<#rt/>
	</#if>
	<#if parameters.spinner?default(false)>
	 spinner="true"<#rt/>
	</#if>
	<#if parameters.match??>
	 match="${parameters.match?html}"<#rt/>
	</#if>
	<#if parameters.required?default(false)>
	 required="true"<#rt/>
	</#if>
	<#if parameters.label??>
	 label="${parameters.label?default("")?html}"<#rt/>
	</#if>
	<#if parameters.dataOptions??>
	 dataOptions="${parameters.dataOptions?default("")?html}"<#rt/>
	</#if>
	<#if parameters.wrapper??>
	 ${parameters.wrapper?default("")?html}="true"<#rt/>
	 autocomplete="off" disableautocomplete<#rt/>
	</#if>
	/>
	<#if parameters.wrapper??>
	<span class="input-group-btn"><button type="button" class="btn btn-primary"><i class="fa ${parameters.fontAwesome?default("")?html}"></i></button></span><#rt/>
	</div><#rt/>
	</#if>
</#if>