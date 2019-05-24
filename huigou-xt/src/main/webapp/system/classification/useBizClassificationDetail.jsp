<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<x:hidden name="orgId" id="mainOrgId"/>
<x:hidden name="bizClassificationId"/>
<div id="layoutDetail">
	<div position="left" title="业务分类">
        <ul id="maintree"></ul>
    </div>
    <div position="center" id="layoutDetailCenter" style="padding:0;">
    	<div id="bizClassificationDetails" style="color:#aaaaaa;font-weight:700;font-size:30px;padding:50px;text-align:center;">请选择业务分类!</div>
    </div>
</div>
