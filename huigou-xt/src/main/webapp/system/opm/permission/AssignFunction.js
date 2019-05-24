var treeManager, treeManager2, roleId, selectedData,refreshFlag=null;
$(function() {
	getQueryParameters();
	initializeUI();

	function getQueryParameters() {
		roleId = Public.getQueryStringByName("roleId");
	}
});

function initializeUI(){
	UICtrl.layout("#layout",{
		leftWidth:5,
		rightWidth:6,
		heightDiff:-50,
		allowLeftCollapse: false, 
		allowRightCollapse: false,
		allowLeftResize: false,
		allowRightResize: false
	});
	$("#layout").find('div.ui-layout-center>div.ui-layout-warp').css('border',0);
	//按钮
	$.getFormButton(
		[
	      {id:'saveDetail',name:'保 存',event:doAssignFunction},
	      {name:'关 闭',event:closeWindow}
	    ]
	);
	$('#permissionRoot').combox({onChange:function(o){
		$('#choosePermissionName').val('');
	}});
	$('#choosePermissionName').searchbox({
 		type: "sys", name: "queryPermission",
		back: { id: '', name: '#choosePermissionName'},
		getParam:function(){
			var parentId=$('#permissionRoot').val();
			if(parentId==''){
				Public.tip('请选择权限类别!');
				return false;
			}
			return {parentId:parentId};
		},
		onChange: function(values, data) {
			var old_id=$('#choosePermissionId').val();
			if(old_id != data.id){
				loadFunctionTree(data.id);
				loadSelectedFunctionTree(data.id);
				$('#choosePermissionId').val(data.id);
			}
		}
   });
	/*var rootFunction=$('#rootFunction');
	//下拉菜单数据改变后重新加载权限树
	rootFunction.combox({onChange:function(o){
		var old_id=$('#choosePermissionId').val();
		if(old_id != o.value){
			loadFunctionTree(o.value);
			loadSelectedFunctionTree(o.value);
			$('#choosePermissionId').val(o.value);
		}
	}});
	var id=$('#rootFunction').val();
	loadFunctionTree(id);
	loadSelectedFunctionTree(id);
	$('#choosePermissionId').val(id);*/
}

function closeWindow(){
	if(refreshFlag){
		UICtrl.closeCurrentTab();
	}else{
		UICtrl.closeCurrentTab();
	}
}
//保存授权
function doAssignFunction(){
	var data = selectedData, permissionIds = [];
	if (!data) return;
	$.each(data,function(i, o){
		if(o['id']){
			permissionIds.push(o['id']);
		}
	});
	var params = {roleId: roleId, parentId: $('#choosePermissionId').val()};
	params['permissionIds'] = $.toJSON(permissionIds);
	Public.ajax(web_app.name + "/access/allocateFunPermissions.ajax", params, function() {
		refreshFlag=true;
	});
}
function doDeleteAll(){
	selectedData=[];
	reloadSelectedFunctionTree();
}
function deleteData() {
	var rows = treeManager2.getChecked();
	if (!rows || rows.length < 1) {
		Public.tip('请选择右侧功能!');
		return;
	}
	for (var i = 0; i < rows.length; i++) {
		for (var j = 0; j < selectedData.length; j++) {
			if (selectedData[j].id == rows[i].data.id) {
				selectedData.splice(j, 1);
				break;
			}
		}
	}
	reloadSelectedFunctionTree();
}

function addData() {
	var rows = treeManager.getChecked();
	if (!rows || rows.length < 1) {
		Public.tip('请选择左侧功能!');
		return;
	}
	selectedData = selectedData || [];
	for (var i = 0; i < rows.length; i++) {
		var notAdded = true;
		for (var j = 0; j < selectedData.length; j++) {
			if (selectedData[j].id == rows[i].data.id) {
				notAdded = false;
				break;
			}
		}
		if (notAdded) {
			processParentData(rows[i].data);
			var data = {};
			data.id = rows[i].data.id;
			data.parentId = rows[i].data.parentId;
			data.name = rows[i].data.name;
			data.icon = rows[i].data.icon;
			data.hasChildren = rows[i].data.hasChildren;
			data.sequence = rows[i].data.sequence;
			data.type = rows[i].data.type;
			data.isexpand = true;
			data.roleId = roleId;
			selectedData[selectedData.length] = data;
		}
	}
	reloadSelectedFunctionTree();
}

function processParentData(node) {
	var parentData = treeManager.getParent(node);
	if (parentData) {
		var notAdded = true;
		for (var i = 0; i < selectedData.length; i++) {
			if (selectedData[i].id == parentData.id) {
				notAdded = false;
				break;
			}
		}
		if (notAdded) {
			var data = {};	
			data.id = parentData.id;
			data.parentId = parentData.parentId;
			data.name = parentData.name;
			data.nodeIcon = parentData.nodeIcon;
			data.hasChildren = parentData.hasChildren;
			data.sequence = parentData.sequence;
			data.type = parentData.type;
			data.isexpand = true;
			data.roleId = roleId;
			selectedData[selectedData.length] = data;
			processParentData(parentData);
		}
	}
}
function isFunctionTreeLeaf(data){
	data.children = [];
    return data.hasChildren == 0;
}

function loadFunctionTree(parentId) {
	if (treeManager){
		var parentDiv=$('#functionTree').parent();
		$('#functionTree').removeAllNode();
		parentDiv.append('<ul id="functionTree"></ul>');
	}
	if(Public.isBlank(parentId)){
		return;
	}
	var loadUrl=web_app.name + "/access/queryAllPermissionsByParentId.ajax";
	
	treeManager=UICtrl.tree("#functionTree",{
		url:loadUrl,
		param:{parentId: parentId},
		idFieldName : 'id',
		parentIDFieldName : 'parentId',
		textFieldName : "name",
		iconFieldName : "nodeIcon",
		checkbox : true,
		btnClickToToggleOnly : true,
        nodeWidth : 250,
        isExpand:2,
        onBeforeCancelSelect:function(node){
        	if(node.data.type!='fun'){//不是功能权限不执行自动取消
        		return false;
        	}
        },
        onClick: treeNodeOnclick
    });
}
function treeNodeOnclick(node,obj){
	if($(obj).hasClass('l-checkbox')){
		return;
	}
	if (node.data && node.data.type) {
        if(node.data.type=='field'){
        	showPermissionField(node.data.id,node.data.name);
        }
    }
}
function showPermissionField(id,name){
	UICtrl.showFrameDialog({
		url : web_app.name + "/system/opm/permissionField/showPermissionField.jsp",
		param : {functionFieldGroupId : id},
		title : name,
		width : 650,
		height : 400,
		cancelVal: '关闭',
		ok :false,
		cancel:true
	});
}
function loadSelectedFunctionTree(parentId) {
	if (treeManager2){
		var parentDiv=$('#functionTree2').parent();
		$('#functionTree2').removeAllNode();
		parentDiv.append('<ul id="functionTree2"></ul>');
	}
	if(Public.isBlank(parentId)){
		return;
	}
	var loadUrl=web_app.name + "/access/queryAllocatedPermissions.ajax";
	
	treeManager2=UICtrl.tree("#functionTree2",{
		url:loadUrl,
		param:{roleId:roleId,parentId: parentId},
		idFieldName : 'id',
		parentIDFieldName : 'parentId',
		textFieldName : "name",
		iconFieldName : "nodeIcon",
		checkbox : true,
		nodeWidth : 250,
		btnClickToToggleOnly : true,
		onBeforeSelect:function(node){
			var obj=node.obj;
        	if($(obj).hasClass("l-checkbox")&&node.data.type!='fun'){//不是功能权限不执行自动取消
        		return false;
        	}
        },
        dataRender:function(data){
        	selectedData=data;
        	return data;
        },
        onClick: treeNodeOnclick
    });
}
//排序
function bubbleSort(array) {
	try {
		var len = array.length, d;
		for (var i = 0; i < len; i++) {
			for (var j = 0; j < len; j++) {
				if (array[i].sequence < array[j].sequence) {
					d = array[j];
					array[j] = array[i];
					array[i] = d;
				}
			}
		}
	} catch (e) {
	}
	return array;
}
function reloadSelectedFunctionTree() {
	if (treeManager2){
		var parentDiv=$('#functionTree2').parent();
		$('#functionTree2').removeAllNode();
		parentDiv.append('<ul id="functionTree2"></ul>');
	}
	for (var i = 0; i < selectedData.length; i++) {
		selectedData[i].treedataindex = undefined;
		if (selectedData[i] && selectedData[i].children)
			selectedData[i].children = [];
	}
	selectedData = bubbleSort(selectedData);
	treeManager2 = UICtrl.tree("#functionTree2", {
		data : selectedData,
		idFieldName : "id",
		parentIDFieldName : "parentId",
		textFieldName : "name",
		iconFieldName : "nodeIcon",
		checkbox : true,
		btnClickToToggleOnly : true,
		nodeWidth : 250,
		onBeforeSelect:function(node){
			var obj=node.obj;
        	if($(obj).hasClass("l-checkbox")&&node.data.type!='fun'){//不是功能权限不执行自动取消
        		return false;
        	}
        },
		isLeaf : function(data) {
			return !(data.children && data.children.length > 0);
		},
		onClick: treeNodeOnclick
	});
	Public.tips({type:0, content: '功能移动成功。'}); 
}