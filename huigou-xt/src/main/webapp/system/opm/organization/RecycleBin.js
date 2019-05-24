var gridManager;
$(function () {
    initGrid();
    initUI();

    function initUI() {
        UICtrl.setSearchAreaToggle(gridManager, false);
    }

    function initGrid() {        
        var toolbarOptions = {
            items: [
                { id: "delete", text: "清除", click: deleteHandler, img: "fa-trash-o" },
                { id: "restore", text: "还原", click: restoreHandler, img: "fa-reply" }
               ]
        };

        gridManager = UICtrl.grid("#maingrid", {
            columns: [
                { display: "类型", name: "orgKindId", width: 30, minWidth: 30, type: "string", align: "left",
                    render: function (item) {
                        return '<img src="'
                            + OpmUtil.getOrgImgUrl(item.orgKindId,
                                item.status) + '">';
                    }
                },
                { display: "编码", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "名称", name: "name", width: 100, minWidth: 60, type: "string", align: "left" },
                { display: "名称全路径", name: "fullName", width: 500, minWidth: 60, type: "string", align: "left" }
            ],
            dataAction: "server",
            url: web_app.name + "/org/slicedQueryOrgs.ajax",
            parms: { status: -1 },
            pageSize: 20,
            sortName: 'fullSequence',
            sortOrder: 'asc',
            toolbar: toolbarOptions,
            width: "99.8%",
            height: "100%",
            heightDiff: -8,
            fixedCellHeight: true,
            selectRowButtonOnly: true
        });
    }
    
    function getBizParams(operationName){
    	var row = gridManager.getSelected();
    	if (row == null){
    		Public.errorTip("请选择要“" + operationName + "”组织。");
    		return false;
    	}
    	var params = {};
    	params.id = row.id;
    	params.name = row.name;
    	return params;
    }
    
    function deleteHandler(){
    	var params = getBizParams("清除");
    	if (!params){
    		return;
    	}
    	var message = "您确认要清除“" + params.name + "”吗？<br/><br/><span style='color: red;'>“清除”操作会同时清除当前选中组织及其下属组织，清除后数据不可恢复。</span>";
    	delete params.name;
    	UICtrl.confirm(message, function () {
    		var url = web_app.name + "/org/physicalDeleteOrg.ajax";
    		Public.ajax(url, params, function(){
        		reloadGrid();
        	});
        });
    }
     
    function restoreHandler(){
    	var params = getBizParams("恢复");
    	if (!params){
    		return;
    	}    	
    	var message = "您确认要还原“" + params.name + "”吗？<br/><br/><span style='color: red;'>“还原”操作会同时还原当前选中组织的所有下属组织。</span>";
    	delete params.name;
    	UICtrl.confirm(message, function (){
    		var url = web_app.name + "/org/restoreOrg.ajax";
    		Public.ajax(url, params, function(){
        		reloadGrid();
        	});
    	});
    }
    	
    function reloadGrid() {
    	var params = $("#queryMainForm").formToJSON();
    	params.status = -1;
    	UICtrl.gridSearch(gridManager, params);
    }
});

// 查询
function query(obj) {
	var param = $(obj).formToJSON();
	UICtrl.gridSearch(gridManager, param);
}

function resetForm(obj) {
	$(obj).formClean();
}