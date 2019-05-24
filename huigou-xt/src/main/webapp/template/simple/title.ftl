<#if parameters.title??>
<div id='${parameters.id?default("navTitle")?html}' class="navTitle row-fluid"><#rt/>
<#if parameters.hideTable??||parameters.hideIndex??>
<a href='javascript:void(0);' hidefocus class='togglebtn' hideTable='${parameters.hideTable?default("")?html}' hideIndex='${parameters.hideIndex?default("")?html}'  title='show or hide'><#rt/>
<i class="fa ${parameters.hideIcon?default("fa-angle-double-down")?html}"></i><#rt/>
</a><#rt/>
</#if>
<i class='fa fa-${parameters.name?default("search")?html}'></i>&nbsp;<span class="titleSpan">${parameters.title?default("")?html}</span><#rt/>
</div><#rt/>
<#if parameters.needLine?default(true)>
<div class="navline"></div><#rt/>
</#if>
</#if>