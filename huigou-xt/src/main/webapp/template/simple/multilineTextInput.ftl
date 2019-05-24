<#setting number_format="#">
<textarea<#rt/>
 name="${parameters.name?default("")?html}"<#rt/>
 cols="${parameters.cols?default("")?html}"<#rt/>
<#if parameters.wrap??>
 wrap="${parameters.wrap?html}"<#rt/>
</#if>
<#if parameters.maxlength??>
 maxlength="${parameters.maxlength?html}"<#rt/>
</#if>
<#if parameters.rows??>
 rows="${parameters.rows?html}"<#rt/>
</#if>
<#if parameters.disabled?default(false)>
 readonly="readonly"<#rt/>
</#if>
<#if parameters.required?default(false)>
 required="true"<#rt/>
</#if>
<#if parameters.label??>
 label="${parameters.label?default("")?html}"<#rt/>
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
><#rt/>
<#if parameters.nameValue??>
${parameters.nameValue?default("")?html}<#rt/>
</#if>
</textarea>