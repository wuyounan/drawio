<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<x:base include="dialog" />
<x:link href="/lib/webUploader/batchUpload/batchUpload.css"/>
<x:script src='/lib/webUploader/webuploader.js'/>
<x:script src='/lib/webUploader/batchUpload/batchUpload.js'/>
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="uploadFileType" />
		<div id="showUploadFileType" style="margin-top: 10px; margin-left: 10px;"></div>
		<div id="showUploadInfo" style="margin-top: 3px; margin-left: 10px;"></div>
		<div class="uploaderWrapper">
			<ul id="uploaderFileList"></ul>
			<span id="picker"><i class="fa fa-eject"></i>&nbsp;<x:message key="common.attachment.chooseUpload"/></span>
			<button id="startUpload" class="btn btn-primary">
				<i class="fa fa-forward"></i>&nbsp;<x:message key="common.attachment.startUpload"/>
			</button>
			<button id="stopUpload" class="btn btn-warning" style="display: none;">
				<i class="fa fa-pause"></i>&nbsp;<x:message key="common.attachment.stopUpload"/>
			</button>
			<button id="keepUpload" class="btn btn-success" style="display: none;">
				<i class="fa fa-play"></i>&nbsp;<x:message key="common.attachment.keepUpload"/>
			</button>
	</div>
</body>
</div>
</html>