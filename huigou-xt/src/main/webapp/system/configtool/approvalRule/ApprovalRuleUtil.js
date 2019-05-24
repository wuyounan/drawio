var treeManager;

function loadProcTreeView() {
    if (treeManager){
    	treeManager.clear();
    }        
    treeManager = $('#maintree').commonTree({
        loadTreesAction: '/procDefinition/queryProcDefinitions.ajax',
        idFieldName: 'id',
        parentIDFieldName: "parentId",
        textFieldName: "name",
        onClick: function (data) {
            if (!data)
                return;
            procNodeClick(data);
        },
        IsShowMenu: false,
        getParam : function(e){
			if(e){
				return {inculdeProcUnit: 1};
			}
			//return {inculdeProcUnit: 1};
		}
    });
}

function refreshNode(options) {
    var parentData;
    if (options.pNode)
        parentData = options.pNode;
    else
        parentData = options.manager.getDataByID(options.params.parentId);
    if (parentData) {
        if (parentData.children && parentData.children.length > 0) {
            for (var i = 0; i < parentData.children.length; i++) {
                options.manager.remove(parentData.children[i].treedataindex);
            }
        }
        var params = $.extend(options.params, { parentId: parentData.id });
        Public.ajax(web_app.name + options.queryAction, params,
            function (data) {
                if (!data.Rows || data.Rows.length == 0) {
                    var pn = options.manager.getParent(parentData.treedataindex);
                    if (pn)
                        refreshNode({manager: options.manager, queryAction: options.queryAction,
                            params: options.params, pNode: pn });
                } else {
                    options.manager.append(parentData, data.Rows);
                }
            });
    }
}