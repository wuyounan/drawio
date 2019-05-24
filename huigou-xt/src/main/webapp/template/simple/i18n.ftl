<#if parameters.name??>
	<script src='${parameters.webApp?default("")?html}/i18n.load?name=${parameters.name?html}<#if parameters.dictionary??>&dictionary=${parameters.dictionary?default("")?html}</#if>' type='text/javascript'></script><#rt/>
</#if>