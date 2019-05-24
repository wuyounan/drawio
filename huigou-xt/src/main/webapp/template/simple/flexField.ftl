<#setting number_format="#">
<#if parameters.tagFlexFieldGroupList?? && parameters.tagFlexFieldGroupList?size gt 0> 
<#list parameters.tagFlexFieldGroupList as groupData>
	<div id='navTitle_${groupData.id}' class='navTitle'><#rt/>
		<a href='javascript:void(0);' hidefocus class='togglebtn' hideTable='#flexField_${groupData.id}' title='show or hide'><i class='fa fa-angle-double-down'></i></a><#rt/>
		<i class='fa fa-windows'></i>&nbsp;<span>${groupData.name?default("")?html}</span><#rt/>
	</div><#rt/>
	<div class='navline'></div><#rt/>
	<div class='flexFieldDiv hg-form' groupId='${groupData.id}'><#rt/>
		<#assign showModel=(groupData.showModel!'1')?number/>
		<#if showModel==1>
		<div class="hg-form-cols"><#rt/>			
		</#if>
		<@divLayout groupData=groupData/>				
		<#if showModel==1>
		</div><#rt/>			
		</#if>
	</div><#rt/>
</#list>
</#if>

<#macro divLayout groupData>
	<div class="row-fluid" id="flexField_${groupData.id}">
	<#if groupData.fields?? && groupData.fields?size gt 0>
		<#assign col=groupData.cols />
		<#assign index=0 />
		<#assign colspan=0 />
		<#list groupData.fields as fieldData>
			<#assign visible=(fieldData.visible!'1')?number/>
			<#if visible==1>
				<#assign controlWidth=(fieldData.controlWidth!'0')?number />
				<#assign labelWidth=(fieldData.labelWidth!'0')?number />
				<#if labelWidth gt 0 >
					<#assign labelWidth=2 />
				</#if>
				<#if controlWidth gt 0 >
					<#assign controlWidth=2 />
				</#if>
				<#if col gt 0>
					<#assign colspan=labelWidth+controlWidth />
					<#assign isNewLine=fieldData.newLine!0/>
					<#if isNewLine==1>
						<#if col gt index>
	            			<div class='col-xs-12 hidden-xs col-sm-${col-index}'>&nbsp;</div><#rt/>
	            		</#if>
		            	</div><#rt/>
		            	<div class='hg-form-row'><#rt/>
		            	<#assign index=colSpan />
					<#else>
						<#if index%col==0>
							<#if index==col>
								</div><#rt/>
								<#assign index=0 />
							</#if>
							<div class='hg-form-row'><#rt/>
						</#if>
						<#assign index=index+colSpan/>
						<#if index gt col>
							<#assign index=col/>
						</#if>
					</#if>
				</#if>
				<div class='col-xs-4 col-sm-${labelWidth}'><#rt/>
	                <label class='hg-form-label'><#rt/>
	               	 	${fieldData.description?html}&nbsp;:<#rt/>
	                </label><#rt/>
	            </div><#rt/>
                <div class='col-xs-8 col-white-bg col-sm-${controlWidth}' id='show_$(fieldData.flexFieldDefinitionId}'><#rt/>
                	<div class="textLabel">${fieldData.fieldNameValue?default("")?html}</div><#rt/>
                </div><#rt/>
			</#if>
		</#list>
		<#if col gt 0>
			<#if col gt index>
	           <div class='col-xs-12 hidden-xs col-sm-${col-index}'>&nbsp;</div><#rt/>
	        </#if>
		</div><#rt/>
		</#if>
	</#if>
	</div><#rt/>
</#macro> 