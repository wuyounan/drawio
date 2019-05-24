//function adjustTreeAreaHeight(){
//	var divHeight = $('#divTreeArea').height();
//	$('#divTreeArea').height(divHeight - 120);
//}

function isShowVirtualOrg(){
	return $("#showVirtualOrg").is(":checked") ? 1 : 0;
}

function isShowDisabledOrg(){
	return $("#showDisabledOrg").is(":checked") ? 1 : 0;
}

function isShowMasterPsm(){
	return $("#showMasterPsm").is(":checked") ? 1 : 0;
}

function getOrgFilterCondition(){
	var params = {};
	
	params.showDisabledOrg = isShowDisabledOrg();
	params.showMasterPsm = isShowMasterPsm();
	params.showVirtualOrg = isShowVirtualOrg();
	
	return params;
}