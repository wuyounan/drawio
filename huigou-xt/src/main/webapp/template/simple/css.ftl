<#assign hasFieldErrors = parameters.name?? && fieldErrors?? && fieldErrors[parameters.name]??/>
<#if parameters.cssClass?? && !(hasFieldErrors && parameters.cssErrorClass??)>
 class="${parameters.cssClass?html}"<#rt/>
<#elseif parameters.cssClass?? && (hasFieldErrors && parameters.cssErrorClass??)>
 class="${parameters.cssClass?html} ${parameters.cssErrorClass?html}"<#rt/>
<#elseif !(parameters.cssClass??) && (hasFieldErrors && parameters.cssErrorClass??)>
 class="${parameters.cssErrorClass?html}"<#rt/>
</#if>
<#if parameters.cssStyle?? && !(hasFieldErrors && (parameters.cssErrorStyle?? || parameters.cssErrorClass??))>
 style="${parameters.cssStyle?html}"<#rt/>
<#elseif hasFieldErrors && parameters.cssErrorStyle??>
 style="${parameters.cssErrorStyle?html}"<#rt/>
</#if>