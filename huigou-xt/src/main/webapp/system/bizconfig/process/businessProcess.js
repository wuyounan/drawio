var gridManager = null, refreshFlag = false, lastSelectedId = '0', yesorno = {"1": "是", "0": "否"};
$(document).ready(function () {
    initializeUI();
    initializeGrid();
    bindClick();
});

function initializeUI() {
    UICtrl.initDefaultLayout();
    $('#maintree').commonTree({
        loadTreesAction: 'bizBusinessProcess/queryBusinessProcesses.ajax',
        parentId: '',
        changeNodeIcon: function (data) {
            var isFinal = data.isFinal;
            if (isFinal == 1) {
                data[this.options.iconFieldName] = web_app.name + "/images/icons/application.png";
            }
        },
        onClick: function (data) {
            if (data && lastSelectedId != data.id) {
                onFolderTreeNodeClick(data.id, data.name, data.isFinal);
            }
        },
        IsShowMenu: false
    });
}

//点击树节点时加载表格
function onFolderTreeNodeClick(id, name, isFinal) {
    lastSelectedId = id;
    $('#layout').layout('setCenterTitle', "<font class='tomato-color'>[" + name + "]</font>流程定义表");
    if (BPMCUtil.isEditAttributePage() || isFinal == 1) {
        updateAttributeHandler(id);
    } else {
        reloadGridAndTree();
    }
}

//初始化表格
function initializeGrid() {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function () {
            var node = $('#maintree').commonTree('getSelected');
            if (!node) {
                Public.tip('请选择左侧父节点!');
                return;
            }
            var isFinal = node.isFinal;
            if (isFinal == 1) {
                Public.errorTip('当前节点为[流程],不能再执行新增操作!');
                return;
            }
            addHandler(node.id);
        },
        updateHandler: function () {
            var row = gridManager.getSelectedRow();
            if (!row) {
                Public.tip('请选择数据！');
                return;
            }
            updateAttributeHandler(row.id);
        },
        moveHandler: moveHandler,
        deleteHandler: deleteHandler,
        saveSortIDHandler: saveSortIDHandler,
        reLoadAll: {id: 'reLoadAll', text: '刷新', img: 'fa-rotate-right', click: reloadGridAll}
    });
    gridManager = UICtrl.grid('#maingrid', {
        columns: [
            {
                display: "", width: 30, minWidth: 30, align: "center",
                render: function (item) {
                	var className = item.count > 0 ? 'fa-folder' : 'fa-file-text-o';
                    var html=['<div class="ui-grid-operation">'];
                	html.push('<a href="javascript:void(0);" data_id="', item.id, '" data_name="', item.name, '"  data_isFinal="', item.isFinal, '"><i class="fa ',className,'"></i></a>');
                	html.push('</div>');
                	return html.join('');
                }
            },
            {display: "编码", name: "code", width: 150, minWidth: 60, type: "string", align: "left"},
            {display: "名称", name: "name", width: 150, minWidth: 60, type: "string", align: "left"},
            {display: "所有者", name: "ownerName", width: 150, minWidth: 60, type: "string", align: "left"},
            {
                display: "末端流程", name: "isFinal", width: 120, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return yesorno[item.isFinal]
                }
            },
            {
                display: "创建人", name: "createdByName", width: 150, minWidth: 60, type: "string", align: "left"
            },
            {
                display: "创建时间", name: "createdDate", width: 100, minWidth: 60, type: "date", align: "left"
            },
            {
                display: "备注", name: "remark", width: 200, minWidth: 60, type: "string", align: "left"
            },
            {
                display: "排序号", name: "sequence", width: 60, minWidth: 60, type: "string", align: "left",
                render: function (item) {
                    return UICtrl.sequenceRender(item);
                }
            }
        ],
        dataAction: 'server',
        url: web_app.name + '/bizBusinessProcess/slicedQueryBusinessProcesses.ajax',
        parms: {parentId: lastSelectedId},
        pageSize: 20,
        width: '100%',
        checkbox: true,
        height: '100%',
        heightDiff: -8,
        sortName: 'sequence',
        sortOrder: 'asc',
        toolbar: toolbarOptions,
        onDblClickRow: function (data, rowindex, rowobj) {
            updateAttributeHandler(data.id);
        }
    });
    UICtrl.setSearchAreaToggle(gridManager);
}

function bindClick() {
	$("#maingrid").on("click",function (e) {
    	var $clicked = $(e.target || e.srcElement);
        if ($clicked.hasClass('fa-folder')) {
        	$clicked=$clicked.parent();
        	var id = $clicked.attr('data_id');
        	var isFinal = $clicked.attr('data_isFinal');
            var name = $clicked.attr('data_name');
            $("#maintree").commonTree('expandById', id);
            onFolderTreeNodeClick(id, name, isFinal);
            return false;
        }
    });
}

//查询
function query(obj) {
    var param = $(obj).formToJSON();
    UICtrl.gridSearch(gridManager, param);
}

//刷新表格
function reloadGrid() {
    if (lastSelectedId) {
        $("#maintree").commonTree('refresh', lastSelectedId);
    }
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

function reloadGridAndTree() {
    if (!lastSelectedId) {
        return;
    }
    var params = $("#queryMainForm").formToJSON();
    params.parentId = lastSelectedId;
    UICtrl.gridSearch(gridManager, params);
}

function reloadGridByLastSelectedId() {
    if (!lastSelectedId) {
        return;
    }
    var isFinal = $('#detailPageIsFinal').val();
    if (isFinal == 0) {
        reloadGridAndTree();
    } else {
        var parentId = $('#detailPageParentId').val();
        var parentName = $('#detailPageParentName').val();
        $("#maintree").commonTree('selectNode', parentId);
        setTimeout(function () {
            parentName = parentName == '' ? '全部' : parentName;
            onFolderTreeNodeClick(parentId, parentName, 0);
        }, 0);
    }
}

function reloadGridAll() {
    $("#maintree").commonTree('reload');
    var params = $("#queryMainForm").formToJSON();
    UICtrl.gridSearch(gridManager, params);
}

//重置表单
function resetForm(obj) {
    $(obj).formClean();
}

//添加按钮
function addHandler(id) {
    var code = $('#maintree').commonTree('getSelected').code;
    if (code == "root") {
        code = "";
    }
    UICtrl.showAjaxDialog({
        title: "添加流程定义", width: 400,
        url: web_app.name + '/bizBusinessProcess/showInsertBusinessProcess.load',
        param: {parentId: id},
        ok: function () {
            var _self = this;
            insert.call(this, function (id) {
                _self.close();
            });
            return false;
        },
        okVal: '保存'
    });
}

//新增保存
function insert(callback) {
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/bizBusinessProcess/insertBusinessProcess.ajax',
        success: function (data) {
            reloadGrid();
            if ($.isFunction(callback)) {
                callback.call(window, data);
            }
        }
    });
}

//编辑按钮
function updateHandler(id) {
    UICtrl.showAjaxDialog({
        title: "修改流程定义",
        url: web_app.name + '/bizBusinessProcess/showUpdateBusinessProcess.load',
        param: {id: id},
        init: function (div) {
            var count= $("#hasChildren").val();
            if (count && count > 0) {
                $('input[name="code"]', div).attr("readonly", true);
            }
            $('input[name="code"]', div).subjectCode();
        },
        ok: update,
        width: 400
    });
}

//编辑保存
function update(doc) {
    var _self = this;
    $('#submitForm').ajaxSubmit({
        url: web_app.name + '/bizBusinessProcess/updateBusinessProcess.ajax',
        success: function () {
            reloadGrid();
            _self.close();
        }
    });
}

function moveHandler() {
    var excludeIds = DataUtil.getSelectedIds({
        gridManager: gridManager
    });
    if (!excludeIds || excludeIds.length < 1) {
        Public.tip('请选择数据！');
        return;
    }
    UICtrl.showDialog({
        title: '移动到...', width: 300,
        content: '<div style="overflow-x: hidden; overflow-y: auto; width:280px;height:250px;"><ul class="move-tree"></ul></div>',
        init: function (doc) {
            $('ul.move-tree', doc).commonTree({
                loadTreesAction: 'bizBusinessProcess/queryBusinessProcessesOnMove.ajax',
                parentId: "",
                getParam: function () {
                    //排除当前选中节点
                    return {excludeIds: excludeIds.join(',')};
                },
                IsShowMenu: false
            });
        },
        ok: function (doc) {
            var parentId = $('ul.move-tree', doc).commonTree('getSelectedId');
            if (!parentId) {
                Public.tip('请选择树节点！');
                return false;
            }
            var _self = this;
            DataUtil.updateById({
                action: 'bizBusinessProcess/moveBusinessProcesses.ajax',
                gridManager: gridManager, idFieldName: 'id', param: {parentId: parentId},
                onSuccess: function () {
                    reloadGridAll();
                    _self.close();
                }
            });
        },
        close: function (doc) {
            $('ul.move-tree', doc).removeAllNode();
            return true;
        }
    });
}

//删除按钮
function deleteHandler() {
    BPMCUtil.deleteObject({
        action: 'bizBusinessProcess/deleteBusinessProcesses.ajax',
        gridManager: gridManager, idFieldName: 'id',
        onSuccess: function () {
            reloadGrid();
        },
        onErrorClick: function (id) {
            updateAttributeHandler(id);
        }
    });
}

//保存排序号
function saveSortIDHandler() {
    var action = "bizBusinessProcess/updateBusinessProcessesSequence.ajax";
    DataUtil.updateSequence({
        action: action,
        param: {parentId: lastSelectedId},
        gridManager: gridManager,
        idFieldName: 'id',
        onSuccess: function () {
            reloadGrid();
        }
    });
    return false;
}

function updateAttributeHandler(id) {
    if (Public.isBlank(id) || id == '0') {
        return;
    }
    var url = web_app.name + '/bizBusinessProcess/showEditBusinessProcessAttribute.load';
    BPMCUtil.createEditAttributePage($('#mainInfoDiv'), url, {id: id}, function (div) {
        initializeEditPageUI(div);
    });
}
