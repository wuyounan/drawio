var handlerArray = new Array();

$(function() {

	initUI();
	bindEvents();
	initData();

	function initUI() {
		
		if(UICtrl.isApplyProcUnit()){
			$('#handlerTr').show();
		};
		$('#functionName').treebox({
			name : 'opFunction',
			beforeChange : function(node) {
				$('#functionId').val(node.id);
				$('#functionName').val(node.name);
			}
		});

		$('#scopeOrgFullName').orgTree({
			checkbox : true,
			filter : 'ogn,dpt',
			param : {
				searchQueryCondition : "org_kind_id in('ogn','dpt')"
			},
			back : {
				text : '#scopeOrgFullName',
				value : '#scopeOrgFullId',
				id : '#scopeOrgFullId',
				name : '#scopeOrgFullName'
			}
		});
		$('#permissionCheckFileList').fileList();
		
		if(!Public.isReadOnly){
		 	setTimeout(function(){$('#permissionCheckFileList').fileList('enable');},0);
		}
	}
	
	function getFunctionId(){
		return $("#functionId").val();
	}
	
	function getScopeOrgFullId(){
		return $("#scopeOrgFullId").val();
	}
	
	function bindEvents(){
		$("#checkPermission").on('click', function (e) {
	        var $clicked = $(e.target || e.srcElement);
	        if ($clicked.is("a.tLink")) {
	        	var url, tabId;
	        	var kindId = $clicked.attr("kindId");
	            if (kindId == "fun"){
	            	url = "access/forwardQueryPermissionForFunction.do";
	            	tabId = "queryPermissionForFunction";
	            }else{
	            	url = "access/forwardQueryPermissionForOrg.do"; 
	            }
	            url+="?funId="  + getFunctionId() + "&inOrgFullId=" +getScopeOrgFullId();
	            UICtrl.addTabItem({ tabid: tabId, text: $clicked.text(), url: url});
	            return stopPropagation(e);
	        }
	    });
	}
	
	
	function initData() {
		var id = getId();
		if (!id){
			return;
		}
		Public.ajax(web_app.name + '/permissionCheck/queryHandler.ajax',
				{ id : getId() }, function(data) {
					handlerArray = data;
					handlerArray.sort(handlerArraySort);
					initHandlerDiv();
				});
	}

});

function handlerArraySort(o1, o2) {
	var g = o1['groupId'] * 100, q = o2['groupId'] * 100;
	var a = o1['sequence'], b = o2['sequence'];
	return (g + a) > (q + b) ? 1 : -1
}

function initHandlerDiv() {
	var handlerDiv = $('#handlerDiv');
	var html = [];
	$.each(handlerArray, function(i, o) {
		html.push('<span title="', o['fullName'], '">');
		html.push(o['orgUnitName']);
		html.push('</span">;&nbsp;');
	});
	handlerDiv.html(html.join(''));
}

function showChooseHandlerDialog() {
	var params = {};
	var selectOrgParams = OpmUtil.getSelectOrgDefaultParams();
	params = $.extend({}, params, selectOrgParams);
	UICtrl.showFrameDialog({
		title : '选择处理人',
		width : 800,
		height : 380,
		url : web_app.name + '/workflow/showCounterSignDialog.do',
		param : params,
		init : function() {
			var addFn = this.iframe.contentWindow.addData;
			if ($.isFunction(addFn)) {
				this.iframe.contentWindow.isInitializingData = true;
				$.each(handlerArray, function(i, d) {
					addFn.call(window, d);
				});
				this.iframe.contentWindow.isInitializingData = false;
			}
		},
		ok : function() {
			var fn = this.iframe.contentWindow.getChooseGridData;
			var params = fn();
			if (!params) {
				return;
			}
			// 清空数组
			handlerArray.splice(0, handlerArray.length);
			$.each(params, function(i, o) {
				o['orgUnitId'] = o['handlerId'];
				o['orgUnitName'] = o['handlerName'];
				o['id'] = o['handlerId'];
				o['name'] = o['handlerName'];
				o['kindId'] = 'handler';
				handlerArray.push(o);
			});
			// 处理人列表排序
			handlerArray.sort(handlerArraySort);
			initHandlerDiv();
			this.close();
		},
		cancelVal : '关闭',
		cancel : true
	});
}

function clearChooseArray() {
	handlerArray.splice(0, handlerArray.length);
	$('#handlerDiv').html('');
}

function getId() {
	return $("#id").val() || 0;
}

function setId(value) {
	$("#id").val(value);
	$('#permissionCheckFileList').fileList({
		bizId : value
	});
}

function checkConstraints() {
	if (UICtrl.isApplyProcUnit()) {
		if (handlerArray.length == 0) {
			Public.errorTip('请选择处理人。');
			return false;
		}
	}
	return true;
}

function getExtendedData() {
	var param = {};
	if (UICtrl.isApplyProcUnit()) {
		param['detailData'] = Public.encodeJSONURI(handlerArray);
	}
	return param;
}