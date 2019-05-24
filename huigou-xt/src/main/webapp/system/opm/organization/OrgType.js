var gridManager, orgKindId, folderId = 0, refreshFlag, moveDialog, closeDetail;

$(function () {
    getQueryParameters();
    initializeUI();
    initializeTree();
    initializeGrid();

    function getQueryParameters() {
        orgKindId = Public.getQueryStringByName("orgKindId");
    }

    function initializeUI() {
        UICtrl.initDefaultLayout();
        $('#layout').layout('setLeftTitle',getTabCaption() + "树");
        $('#layout').layout('setCenterTitle',getTabCaption() + "列表");
    }

    function initializeTree() {
        $('#orgTypeTree').commonTree(
				{
				    kindId: getCommonTreeKindId(),
				    onClick: function (data) {
				        onFolderTreeNodeClick(data);
				    },
				    IsShowMenu: true
				});
    }

    function initializeGrid() {
        var toolbarParam = { 
        		addHandler: showInsertDialog, 
        		updateHandler: showUpdateDialog,
                deleteHandler: deleteOrgType, 
                saveSortIDHandler: updateOrgTypeSequence, 
                moveHandler: moveOrgType
        };
        var toolbarOptions = UICtrl.getDefaultToolbarOptions(toolbarParam);
        gridManager = UICtrl.grid("#maingrid", {
            columns: [
					{ display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
					{ display: "名称", name: "name", width: 200, minWidth: 60, type: "string", align: "left" },
					{ display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
					    render: function (item) {
					        return "<input type='text' id='txtSequence_" + item.id + "' class='textbox' value='" + item.sequence + "' />";
					    }
					},
					{ display: "状态", name: "status", width: 60, minWidth: 60, type: "string", align: "center",
					    render: UICtrl.getStatusInfo
					}],
            dataAction: "server",
            url: web_app.name + '/orgType/slicedQueryOrgTypes.ajax',
            parms: { orgKindId: orgKindId },
            pageSize: 20,
            sortName: 'sequence',
            sortOrder: 'asc',
            toolbar: toolbarOptions,
            width: "99.8%",
            height: "100%",
            heightDiff: -8,
            checkbox: true,
            fixedCellHeight: true,
            selectRowButtonOnly: true,
            onDblClickRow: function (data, rowIndex, rowObj) {
                doShowUpdateDialog(data.id);
            }
        });
        UICtrl.setSearchAreaToggle(gridManager, false);
    }
});
function getId() {
	return $("#editId").val();
}

function getSequence() {
    return parseInt($("#editSequence").val() || 0);
}

var addedButton = [{
    id: 'saveInsert',
    name: '保存新增',
    callback: saveInsert
}];

function initDetail() {
    $("#editId").val("");
    $("#editCode").val("");
    $("#editName").val("");
    $("#editSequence").val(getSequence() + 1);
}

function saveInsert() {
    closeDetail = false;
    doSaveOrgType.call(this);
    return false;
}

function getTitle(addOrUpdate) {
    var result = (addOrUpdate == "add") ? "添加" : "修改";
    switch (orgKindId) {
        case "ogn":
            result += "机构类型";
            break;
        case "dpt":
            result += "部门类型";
            break;
        case "pos":
            result += "岗位类型";
            break;
    }
    return result;
}

function showInsertDialog() {
    if (!folderId || folderId == 0) {
        Public.tip("请选择父节点。");
        return;
    }

    UICtrl.showAjaxDialog({
        title: getTitle("add"),
        param: { folderId: folderId, orgKindId: orgKindId },
        width: 400,
        url: web_app.name + '/orgType/showOrgTypeDetail.load',
        ok: saveOrgType,
        init: initDetailDialog,
        close: onDialogCloseHandler,
        button: addedButton
    });
}

function doShowUpdateDialog(id) {
	if(!id){
		var id = DataUtil.getUpdateRowId(gridManager);
		if (!id){ return; }
	}
    closeDetail = true;
    UICtrl.showAjaxDialog({
        url: web_app.name + '/orgType/loadOrgType.load',
        param: { id: id },
        title: getTitle("update"),
        width: 400,
        init: initDetailDialog,
        ok: doSaveOrgType,
        close: onDialogCloseHandler
    });
}

function initDetailDialog(){
	 $("#editName").click(function(){
     	 var code = $.chineseLetter($("#editName").val());
         var oldCode = $("#editCode").val();
         if (!oldCode) {
             $("#editCode").val(code);
         }
 	});
}

function showUpdateDialog() {
    doShowUpdateDialog();
}

function saveOrgType() {
    closeDetail = true;
    doSaveOrgType.call(this);
}

function doSaveOrgType() {
    var _self = this;
    var id = getId();
    $('#submitForm').ajaxSubmit({
        url: web_app.name + (id ? '/orgType/updateOrgType.ajax' : '/orgType/insertOrgType.ajax'),
        success: function () {
            refreshFlag = true;
            if (closeDetail) {
                _self.close();
            }
            else {
                initDetail();
            }
        }
    });
}

function deleteOrgType() {
    var action = "orgType/deleteOrgTypes.ajax";
    DataUtil.del({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

function updateOrgTypeSequence() {
    var action = "orgType/updateOrgTypeSequence.ajax";
    DataUtil.updateSequence({ action: action, gridManager: gridManager, onSuccess: reloadGrid });
}

function moveOrgType() {
	UICtrl.showMoveTreeDialog({
		gridManager:gridManager,title:'移动组织类型', kindId: getCommonTreeKindId(),
		save:function(parentId){
			DataUtil.updateById({action: "/orgType/moveOrgType.ajax",
				gridManager:gridManager,idFieldName:'id',param:{folderId:parentId},
				onSuccess:function(){
					reloadGrid();
				}
			});
		}
	});	
}

function onFolderTreeNodeClick(data) {
    if (folderId != data.id) {
        if (!data.parentId || data.parentId == '0') {
        	$("#layout").layout("setCenterTitle", getTabCaption() + "列表");
            folderId = 0;
        } else {
        	$("#layout").layout("setCenterTitle", "<span class='tomato-color'>["
					+ data.name + "]</span>" + getTabCaption()  + "列表");
            folderId = data.id;
        }
        reloadGrid();
    }
}


function reloadGrid() {
	var param = $("#queryMainForm").formToJSON();
	param = $.extend(param,  { folderId: folderId });
    UICtrl.gridSearch(gridManager, { folderId: folderId });
}

function getTabCaption(action) {
    var result;
    switch (orgKindId) {
        case "ogn":
            result = "机构类型";
            break;
        case "dpt":
            result = "部门类型";
            break;
        case "pos":
            result = "岗位类型";
            break;
        case "prj":
            result = "项目组类型";
        default:
            result = "";
    }
    switch (action) {
        case "Add":
            result = "添加" + result;
            break;
        case "Update":
            result = "修改" + result;
            break;
    }
    return result;
}

function getCommonTreeKindId() {
    switch (orgKindId) {
        case "ogn":
            result = 1;
            break;
        case "dpt":
            result = 2;
            break;
        case "pos":
            result = 3;
            break;
        case "prj":
            result = 4;
            break;
        default:
            result = -1;
    }
    return result;
}

function onDialogCloseHandler() {
    if (refreshFlag) {
        reloadGrid();
        refreshFlag = false;
    }
}

function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}