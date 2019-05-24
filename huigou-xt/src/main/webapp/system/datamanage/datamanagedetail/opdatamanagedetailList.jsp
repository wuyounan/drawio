<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <x:base include="dialog,layout,grid,tree,combox,commonTree,comboDialog"/>
    <x:script src="/system/datamanage/datamanagedetail/opdatamanagedetailList.js"/>
</head>
<body>
<div class="container-fluid">
    <div id="layout">
        <div position="left" title="类别树">
        	<div class="ui-grid-query-div" style="position:static;width: 100%;margin:5px 0 0 10px;">
        		<div class="input-group" id="queryTypeGroup">
				    <input type="text" value="" class="text ui-grid-query-input building-query-input">
				    <span class="input-group-btn">
				    	<button type="button" class="btn btn-sm ui-grid-query-clear"><i class="fa fa-times-circle"></i></button>
				    	<button type="button" class="btn btn-sm btn-info ui-grid-query-button"><i class="fa fa-search"></i></button>
				    </span>
				</div>
		    </div>
			<div class="blank_div clearfix"></div>
			<div class="dom-auto-height" id="typeTreeViewDiv">
				<ul id="maintree"></ul>
			</div>
			<div class="ui-hide" id="typeGridViewDiv">
				<div id="typeGridView"></div>
			</div>
        </div>
        <div position="center" title="数据管理权限列表">
            <x:title title="搜索" hideTable="queryMainForm" isHide="true"/>
            <form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
                <x:inputC name="code" required="false" label="权限编码" maxLength="16" labelCol="1"/>
                <x:inputC name="name" required="false" label="名称" maxLength="32" labelCol="1"/>
                <div class="col-xs-12 col-sm-4">
					<button type="button" class="btn btn-primary" onclick="query(this.form)">
						<i class="fa fa-search"></i>&nbsp;查 询
					</button>
					&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" onclick="resetForm(this.form)">
						<i class="fa fa-history"></i>&nbsp;重 置
					</button>
					&nbsp;&nbsp;
				</div>
            </form>
            <div class="blank_div clearfix"></div>
            <div id="maingrid" style="margin: 2px;"></div>
        </div>
    </div>
</div>
</body>
</html>
