<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="mainPanel" style="padding:0;">
	<div class="orgSelectLayout" style="display:none">
		<div position="top" style="position:relative;">
			<div class="btn-group pull-left chooseTabs" style="margin-top:5px;margin-right:5px">
    			<button type="button" class="btn btn-success active" divExpr="chooseByOrgDiv"><i class="fa fa-vcard-o"></i>&nbsp;<x:message key="common.selectorg.kind.org"/></button></button>
    			<button type="button" class="btn btn-info" divExpr="chooseByGroup"><i class="fa fa-group"></i>&nbsp;<x:message key="common.selectorg.kind.group"/></button>
			</div>
			<div class="pull-left checkBoxDiv" style="margin-top:15px;margin-right:5px;width:500px;">
			 	<label style="font-weight: normal; line-height: 22px;"><input type="checkBox" class="chooseChildCheck" >&nbsp;&nbsp;<x:message key="common.selectorg.option.cascade"/></label>
				<label style="font-weight: normal; line-height: 22px;"><input type="checkBox" class="showVirtualOrg">&nbsp;&nbsp;<x:message key="common.selectorg.option.virtual"/></label> 
				<label style="font-weight: normal; line-height: 22px;"><input type="checkBox" class="showPosition">&nbsp;&nbsp;<x:message key="common.selectorg.option.position"/></label>
			</div>
		</div>
		<div position="left" title='<x:message key="common.button.search"/>' class="orgSelectLayoutLeft">
			<div class="ui-grid-query-div ui-hide queryConditionDiv">
	    		<div class="input-group">
	    			<input type="text" value="" class="text ui-grid-query-input">
	    			<span class="input-group-btn">
	    				<button type="button" class="btn btn-sm ui-grid-query-clear"><i class="fa fa-times-circle"></i></button>
	    				<button type="button" class="btn btn-sm btn-info ui-grid-query-button"><i class="fa fa-search"></i></button>
	    			</span>
	    		</div>
			</div>
			<div class="orgTreeDiv">
				<ul class="orgTree"></ul>
			</div>
			<div class="orgQueryGridDiv" style="display:none;height:100%;">
				<div class="orgQueryGrid" style="margin-top:2px"></div>
			</div>
			<div class="chooseByGroup" style="display:none;">
				<ul class="userGroupTree"></ul>
			</div>	
		</div>
		<div position="center">
			<div class="handlerGrid"></div>
		</div>
	</div>
</div>
