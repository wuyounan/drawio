<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
   <head>
	<link rel="stylesheet" type="text/css" href='<c:url value="/system/personOwn/notes/style.css"/>'/>
	<x:base include="dialog,grid,date" />
	<script src='<c:url value="/system/personOwn/notes/noteList.js"/>' type="text/javascript"></script>
	<style type="text/css">
		body{background-image :none;}
		span.color{display:inline-block; width:50px; height:15px;background-image:none;margin-top:5px;}
	</style>
  </head>
  <body>
  	<div class="mainPanel">
	  	<div id="mainWrapperDiv">
	  		<x:title title="用户便签" hideTable="queryDiv"/>
			<form method="post" action="" id="queryMainForm">
			<div class="ui-form" id="queryDiv">
					<x:inputL name="content" required="false" label="内容"  readonly="false" width="456"/>
					<div class="clear"></div>
					<x:inputL name="dateBegin" required="false" label="创建日期(起)" wrapper="date"/>
					<x:inputL name="dateEnd" required="false" label="创建日期(止)"  wrapper="date"/>
					<dl>
						<x:button value="查 询" onclick="query(this.form)"/>&nbsp;&nbsp;
						<x:button value="重 置" onclick="resetForm(this.form)"/>&nbsp;&nbsp;
						<x:button value="图形显示" onclick="goPicView()"/>&nbsp;&nbsp;
					</dl>
			</div>
			</form>
			<div class="blank_div"></div>
			<div id="maingrid"></div>
		</div> 
	</div>
  </body>
</html>
