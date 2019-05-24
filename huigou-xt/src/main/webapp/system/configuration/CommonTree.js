/*文件夹相关操作开始*/
var folderTreeManager, folderTreeMenu, folderDialog, folderId, kindId, refreshFlag;
$(function() {
	initializeFolderTree();

	function initializeFolderTree() {
        folderTreeMenu =UICtrl.menu({ top: 100, left: 100, width: 120, items: [
                { id: "menuAdd", text: '新建文件夹', click: folderMenuItemClick, icon: "add" },
                { id: "menuUpdate", text: '修改文件夹', click: folderMenuItemClick, icon: "edit" },
                { id: "menuDelete", text: '删除文件夹', click: folderMenuItemClick, icon: "delete" },
                { id: "menuRefresh", text: '刷新', click: folderMenuItemClick, icon: "refresh" },
                { id: "menuUp", text: '上移', click: folderMenuItemClick, icon: "arrow_up" },
                { id: "menuDown", text: '下移', click: folderMenuItemClick, icon: "arrow_down" },
                ]
        });
    }
});

function loadFolderTree(options) {
	var p = options || {};
	if (folderTreeManager)
		folderTreeManager.clear();
	var folderBeforeExpand = onFolderBeforeExpand;
	if (p.onFolderBeforeExpand)
		folderBeforeExpand = p.onFolderBeforeExpand;
	var selector=p.selector || '#maintree';
	Public.ajax(web_app.name + '/commonTree/queryCommonTrees.ajax',
				{ kindId : p.kindId },
				function(data) {
						folderTreeManager = UICtrl.tree(selector,
										{
											data : data.Rows,
											idFieldName : 'id',
											parentIDFieldName : 'parentId',
											textFieldName : "name",
											iconFieldName : "nodeIcon",
											checkbox : false,
											single : true,
											nodeWidth : 180,
											onBeforeExpand : folderBeforeExpand,
											isLeaf : function(data) {
												data.children = [];
												return parseInt(data.hasChildren) == 0;
											},
											onClick : function(node) {
												if (p.onClick)
													p.onClick(node);
											},
											onContextmenu : function(node, e) {
												if (!p.IsShowMenu)
							                        return false;
							                    if (node.data.nodeUrl && node.data.nodeUrl == 'Folder') {
							                        folderId = node.data.id;
							                        kindId = node.data.kindId;
							                        if (node.target.className == "l-first l-last l-onlychild ") {
							                            folderTreeMenu.setEnable("menuUp");
							                            folderTreeMenu.setDisable("menuDown");
							                        }
							                        if (node.target.className.indexOf('l-onlychild') >= 0) {
							                            folderTreeMenu.setDisable("menuUp");
							                            folderTreeMenu.setDisable("menuDown");
							                        }
							                        else if (node.target.className.indexOf('l-first') >= 0) {
							                            folderTreeMenu.setDisable("menuUp");
							                            folderTreeMenu.setEnable("menuDown");
							                        }
							                        else if (node.target.className.indexOf('l-last') >= 0) {
							                            folderTreeMenu.setEnable("menuUp");
							                            folderTreeMenu.setDisable("menuDown");
							                        }
							                        else {
							                            folderTreeMenu.setEnable("menuUp");
							                            folderTreeMenu.setEnable("menuDown");
							                        }
							                        folderTreeMenu.show({ top: e.pageY, left: e.pageX });

							                        var oldNode = folderTreeManager.getSelected();
							                        if (oldNode)
							                            folderTreeManager.cancelSelect(oldNode.target);

							                        folderTreeManager.selectNode(node.target);
							                    }
							                    return false;
							                }
										});
					});
}

function onFolderBeforeExpand(node) {
	if (node.data.hasChildren) {
		if (!node.data.children || node.data.children.length == 0) {
			Public.ajax(web_app.name
					+ '/commonTree/queryCommonTrees.ajax', {
				parentId : node.data.id
			}, function(data) {
				folderTreeManager.append(node.target, data.Rows);
			});
		}
	}
}

function addFolder() {
	UICtrl.showAjaxDialog({
		url : web_app.name
				+ "/commonTree/forwardCommonTreeDetail.load",
		param:{parentId: folderId},
		title: "添加文件夹",
		width: 400,
		ok: insertCommonTree,
		close: commonTreeDialogCloseHandler
	});
}

function updateFolder() {
	UICtrl.showAjaxDialog({
		url : web_app.name + "/commonTree/loadCommonTree.load",
		title : "修改文件夹",
		width : 400,
		ok : updateCommonTree,
		close : commonTreeDialogCloseHandler
	});
}

function insertCommonTree(doc) {
	var _self = this;
	$('form:first',doc).ajaxSubmit({
		url : web_app.name + '/commonTree/insertCommonTree.ajax',
		param : {
			parentId : folderId,
			kindId : kindId
		},
		success : function() {
			refreshFlag = true;
			_self.close();
		}
	});
}

function updateCommonTree(doc) {
	var _self = this;
	$('form:first',doc).ajaxSubmit({
		url : web_app.name + '/commonTree/updateCommonTree.ajax',
		param : {
			id : folderId
		},
		success : function(data) {
			refreshFlag = true;
			_self.close();
		}
	});
}

function deleteFolder(folderId) {
	UICtrl.confirm('确定删除吗?', function() {
		Public.ajax(
				web_app.name + '/commonTree/deleteCommonTree.ajax', {
					id : folderId,kindId : kindId}, function() {
					Public.tip('删除文件夹成功!');
					if (folderTreeManager) {
						var node = folderTreeManager.getSelected();
						if (node) {
							var parentNode = folderTreeManager
									.getParentTreeItem(node.target);
							refreshNode(parentNode);
						} else
							alert('请先选择节点');
					}

				});
	});
}

function moveUpFolder() {
	if (folderTreeManager) {
		var node = folderTreeManager.getSelected();
		if (node) {
			prevData = folderTreeManager.getNodeData($(node.target).prev());
			if (prevData){
				currentData = node.data;
				var params =  new PublicMap();
				params.put(currentData.id, prevData.sequence);
				params.put(prevData.id, currentData.sequence);
				Public.ajax(web_app.name + '/commonTree/updateCommonTreeSequence.ajax',{data:params.toString()} ,
						function(){
					Public.tip('上移文件夹成功!');
					var parentNode = folderTreeManager.getParentTreeItem(node.target);
			         refreshNode(parentNode);
				});
			}
		}
	}
}

function moveDownFolder() {
	if (folderTreeManager) {
		var node = folderTreeManager.getSelected();
		if (node) {
			nextData = folderTreeManager.getNodeData($(node.target).next());
			if (nextData){
				currentData = node.data;
				var params =  new PublicMap();
				params.put(currentData.id, nextData.sequence);
				params.put(nextData.id, currentData.sequence);
				Public.ajax(web_app.name + '/commonTree/updateCommonTreeSequence.ajax', {data:params.toString()},
						function(){
					Public.tip('下移文件夹成功!');
					var parentNode = folderTreeManager.getParentTreeItem(node.target);
			         refreshNode(parentNode);
				});
			}
		}
	}
}

function folderMenuItemClick(item, i) {
	if (item.id == "menuAdd") {
		addFolder(folderId, kindId);
	} else if (item.id == "menuUpdate") {
		updateFolder(folderId);
	} else if (item.id == "menuDelete") {
		deleteFolder(folderId);
	} else if (item.id == "menuRefresh") {
		var node = folderTreeManager.getSelected();
		if (node)
			refreshNode(node.target);
	} else if (item.id == "menuUp") {
		moveUpFolder();
	} else if (item.id == "menuDown") {
		moveDownFolder();
	}
}

function refreshNode(parentNode) {
	var parentData = folderTreeManager.getNodeData(parentNode);
	if (parentData) {
		if (parentData.children && parentData.children.length > 0) {
			for (var i = 0; i < parentData.children.length; i++) {
				folderTreeManager.remove(parentData.children[i].treedataindex);
			}
		}
		Public.ajax(web_app.name + '/commonTree/queryCommonTrees.ajax',
				{
					kindId : kindId,
					parentId : parentData.id

				},

				function(data) {
					if (!data.Rows || data.Rows.length == 0) {
						var pn = folderTreeManager
								.getParentTreeItem(parentNode);
						refreshNode(pn);
					} else {
						folderTreeManager.append(parentData, data.Rows);
					}
				});
	}
}

function commonTreeDialogCloseHandler() {
	if (refreshFlag) {
		var node = folderTreeManager.getSelected();
		if (node) {
			if (folderId > 1) {
				var parentNode = folderTreeManager
						.getParentTreeItem(node.target);
				refreshNode(parentNode);
			} else {
				refreshNode(node.target);
			}
		}
	}
}

/* 文件夹相关操作结束 */

/* 文件夹类型 */

var CommonTreeKind=CommonTreeKind||{};
/* 机构 */
CommonTreeKind.OrgType = 1;
/* 部门 */
CommonTreeKind.DeptType = 2;
/* 岗位 */
CommonTreeKind.PosType = 3;
/* 角色 */
CommonTreeKind.Role = 4;
/* 扩展属性定义 */
CommonTreeKind.FlexFieldDefine = 5;
/* 扩展属性 */
CommonTreeKind.FlexField = 6;
/* 指标模板 */
CommonTreeKind.IndexTemplate = 7;
/* 指标公式库 */
CommonTreeKind.IndexFormulaLibrary = 8;
/* 报表方案 */
CommonTreeKind.ReportScheme = 9;