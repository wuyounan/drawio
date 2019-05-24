<#assign fieldId = parameters.name?string />
<#if parameters.id??>
<#assign fieldId = parameters.id?string />
</#if>
<div class="col-xs-4 col-sm-${parameters.labelCol?default(2)?html}"<#rt/>
<#if parameters.colCssStyle??>
 style="${parameters.colCssStyle?html}"<#rt/>
</#if><#rt/>
><#rt/>
<label class="hg-form-label<#if parameters.required?default(false)> required</#if>"<#rt/>
 id="${fieldId?html}_label"<#rt/>
 title="${parameters.label?default("")?html}">${parameters.label?default("")?html}&nbsp;:</label></div>
<div class="col-xs-8 col-sm-${parameters.fieldCol?default(2)?html} col-warp<#rt/>
<#if parameters.automaticHeight?default(false)><#rt/>
 ui-automatic-height<#rt/>
</#if><#rt/>
<#if parameters.disabled?default(false)><#rt/>
 col-gray-bg<#rt/>
<#elseif parameters.readonly?default(false)>
 col-gray-bg<#rt/>
<#else><#rt/>
 col-white-bg<#rt/>
</#if><#rt/>"
<#if parameters.colCssStyle??>
 style="${parameters.colCssStyle?html}"
</#if><#rt/>
><#rt/>
 <#include "/simple/${parameters.templateName?default('')?html}.ftl" /><#rt/>
 </div><#rt/>