<div class="col-sm-${parameters.col?default("4")?html} col-xs-12"><#rt/>
	<button type="button" class="btn ${parameters.queryType?default("btn-primary")?html}" onclick="query(this.form)"><#rt/>
		<i class="fa fa-search"></i>&nbsp;${MessageSourceContext.getMessage('common.button.query')}<#rt/>
	</button>&nbsp;&nbsp;<#rt/>
	<button type="button" class="btn ${parameters.resetType?default("btn-primary")?html}" onclick="resetForm(this.form)"><#rt/>
		<i class="fa fa-history"></i>&nbsp;${MessageSourceContext.getMessage('common.button.resetform')}<#rt/>
	</button>&nbsp;&nbsp;<#rt/>
</div><#rt/>