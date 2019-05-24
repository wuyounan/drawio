<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<x:base include="attachment,layout"/>
</head>
<body>
		<div class="ui-form">
			<div id="picker" style="width: 60px">选择文件</div>
			<x:fileList bizCode="temp" bizId="123456" id="testFileList" title="附件自定义标题"/>
			</br>
			<x:fileList bizCode="cs001" bizId="123" id="testTableFileList" isClass="true"/>
		</div>
		<script type="text/javascript">
		$(document).ready(function() {
			$('#testFileList').fileList();
			$('#testTableFileList').fileList();
			//$('#picker').JQWebUploader({param:{bizId:123456}}); 
			$('#picker').click(function(){
				$('#testTableFileList').fileList('disable');
			});
		});
		</script>
</body>
</html>