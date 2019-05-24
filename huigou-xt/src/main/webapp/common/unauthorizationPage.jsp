<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<div id="show_error_info">
	<div class="error_top"></div>
	<div class="error_con">
		<div class="error_bg">
			<div class="error_tle"><x:message key="error.title"/></div>
			<div class="error_info font_str"><x:message key="error.info"/></div>
			<div class="error_dtl font_r">
				<font color="red" style="font-size: 11pt; line-height: 16pt"> 
				 	<x:message key="error.page.nopermission"/>
				</font>
			</div>
		</div>
	</div>
	<div class="error_bot"></div>
</div>