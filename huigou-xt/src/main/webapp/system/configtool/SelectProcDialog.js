var treeManager, includeProcUnit;

$(document).ready(function () {
	getQueryParameters();
	
    loadProcTree();

    function getQueryParameters() {
    	includeProcUnit =  Public.getQueryStringByName("includeProcUnit") || 1;
	} 
});

function onBeforeExpand(node) {
    if (node.data.hasChildren) {
        if (!node.data.children || node.data.children.length == 0) {
            Public.ajax(web_app.name + "/procDefinition/queryProcDefinitions.ajax", {
                parentId: node.data.id,
                includeProcUnit: includeProcUnit
            }, function (data) {
                treeManager.append(node.target, data.Rows);
            });
        }
    }
}

function loadProcTree() {
    treeManager = UICtrl.tree("#maintree", {
        url: web_app.name + "/procDefinition/queryProcDefinitions.ajax",
        param: {
        	parentId: 1,
            includeProcUnit: includeProcUnit
        },
        idFieldName: "id",
        parentIDFieldName: "parentId",
        textFieldName: "name",
        checkbox: true,
        iconFieldName: "nodeIcon",
        btnClickToToggleOnly: true,
        autoCheckboxEven: true,
        nodeWidth: 180,
        isLeaf: function (data) {
            data.children = [];
            return parseInt(data.hasChildren) == 0;
        },
        dataRender: function (data) {
            return data.Rows;
        },
        delay: function (node) {
        	var param={
        			parentId: node.data.id,
                    includeProcUnit: includeProcUnit	
        	};
            return { url: web_app.name + '/procDefinition/queryProcDefinitions.ajax', parms: param};
        },
        onBeforeSelect: function(data){
        	return data.procId;
        }
    });
    
}

/**
* 得到选择的对象
*/
function getSelectedObj() {
    var data = treeManager.getChecked();
    var rows = [];
    $.each(data, function (i, o) {
        rows.push(o.data);
    });

    if (rows.length == 0){
    	Public.errorTip("请选择数据。");
    }
    return rows;
}


