<div id='${parameters.id?default("billTitle")?html}' class="bill-title">
	<#if parameters.title??>
	<div class="subject">${parameters.title?default("")?html}</div>
	</#if>
	<div class="bill-info">
	<#if parameters.needPerson?default(true)>
		<#if parameters.billCode??>
		<div class="col-xs-12 col-sm-3 bill-left-info">
		${MessageSourceContext.getMessage('common.field.billcode')}&nbsp;:&nbsp;${parameters.billCode?default("")?html}
		</div>
		</#if>
		<#if parameters.fillinDate??>
		<div class="col-xs-12 col-sm-3 bill-left-info">
		${MessageSourceContext.getMessage('common.field.fillindate')}&nbsp;:&nbsp;${parameters.fillinDate?default("")?html}
		</div> 
		</#if>
		<#if parameters.personMemberName??>
			<div class="col-xs-12 col-sm-4 bill-right-info">
			${MessageSourceContext.getMessage('common.field.createby')}&nbsp;:&nbsp;
			<#if parameters.organName??>
			${parameters.organName?default("")?html}.<#rt/>
			</#if>
			<#if parameters.deptName??>
			${parameters.deptName?default("")?html}.<#rt/>
			</#if>
			<#if parameters.positionName??>
			${parameters.positionName?default("")?html}.<#rt/>
			</#if>
			${parameters.personMemberName?default("")?html}<#rt/>
			</div>
		</#if>
		<#if parameters.needStatus?default(true)&&parameters.statusTextView??>
		<div class="col-xs-12 col-sm-2 bill-right-info">
		${MessageSourceContext.getMessage('common.field.status')}&nbsp;:&nbsp;${parameters.statusTextView?default("")?html}<#rt/>
		</div>
		</#if>
	<#else>
		<div class="col-xs-12 col-sm-4 bill-left-info">
		${MessageSourceContext.getMessage('common.field.billcode')}&nbsp;:&nbsp;${parameters.billCode?default("")?html}
		</div>
		<#if parameters.needStatus?default(true)&&parameters.statusTextView??>
		<div class="col-xs-12 col-sm-2 hidden-xs">&nbsp;</div>
		<#else>
		<div class="col-xs-12 col-sm-4 hidden-xs">&nbsp;</div>
		</#if>
		<div class="col-xs-12 col-sm-4 bill-left-info">
		${MessageSourceContext.getMessage('common.field.fillindate')}&nbsp;:&nbsp;${parameters.fillinDate?default("")?html}
		</div>
		<#if parameters.needStatus?default(true)&&parameters.statusTextView??>
		<div class="col-xs-12 col-sm-2 bill-right-info">
		${MessageSourceContext.getMessage('common.field.status')}&nbsp;:&nbsp;${parameters.statusTextView?default("")?html}<#rt/>
		</div>
		</#if>
	</#if>
	</div>
</div>
