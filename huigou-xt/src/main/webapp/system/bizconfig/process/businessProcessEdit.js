function initializeEditPageUI(div) {
    var hasChildren = parseInt($('input[name="hasChildren"]', div).val());
    var isFinal = parseInt($('#detailPageIsFinal').val());
    var buttons = [];
    if (hasChildren == 0 && isFinal == 1) {
        buttons.push({
            id: 'addChar', name: '编辑流程图',icon:'fa-sitemap', event: function () {
                showFlowChart()
            }
        });
        buttons.push({
            id: 'deleteChar', name: '删除流程图',icon:'fa-trash-o', event: function () {
                deleteFlowChart()
            }
        });
        buttons.push({
            id: 'viewChar', name: '预览流程图',icon:'fa-address-book-o', event: function () {
                viewFlowChart()
            }
        });
    }
    buttons.push({
        id: 'saveDetail', name: '保 存',icon:'fa-save', event: function () {
            doSave();
        }
    });
    buttons.push({
        id: 'closeDetail', name: '关 闭',icon:'fa-times', event: function () {
        	BPMCUtil.hideEditAttributePage();
        }
    });
    BPMCUtil.createFormButton(buttons);
    Public.autoInitializeUI(div);
    bindEvent(div, hasChildren);
}

function bindEvent(div, hasChildren) {
    if (hasChildren > 0) {
        UICtrl.disable($('#detailPageIsFinal'));
        return;
    }
    var isFlowChart = parseInt($('input[name="isFlowChart"]', div).val());
    if (isFlowChart > 0) {
        UICtrl.disable($('#detailPageIsFinal'));
        return;
    }
    //是否末级选项发生改变
    $('#detailPageIsFinal').combox({
        beforeChange: function (data) {
            isFinalChange(data);
            return true;
        }
    });
}

function isFinalChange(data) {
    var value = $(data).attr('key');
    var isFinal = $('#detailPageIsFinal').val();
    if (value == isFinal) {
        return false;
    }
    var msg = ['您确定需要修改[末端流程]标志吗?'];
    UICtrl.confirm(msg.join(''), function () {
        //调用保存
        var flag = doSave({isFinal: value}, function () {
            //重新加载页面
            updateAttributeHandler($('#detailPageId').val());
        });
        if (!flag) {
            setTimeout(function () {
                $('#detailPageIsFinal').combox('setValue', isFinal);
            }, 0);
        }
    }, function () {
        setTimeout(function () {
            $('#detailPageIsFinal').combox('setValue', isFinal);
        }, 0);
    });
}

function doSave( pa, onCallBack) {
    var id = $('#detailPageId').val();
    var url = web_app.name + '/bizBusinessProcess/saveBusinessProcessAndAttribute.ajax';
    var  param = $.extend({}, pa || {});
    $('#submitForm').ajaxSubmit({
        url: url, param: param,
        success: function (data) {
            //修改树节点
            $('#maintree').commonTree('updateNodeName', $('#detailPageId').val(), $('#detailPageName').val());
            if ($.isFunction(onCallBack)) {
                onCallBack.call(window);
            }
            BPMCUtil.initializePageParameters(true);
        },
        fail: function () {
            if ($.isFunction(onCallBack)) {
                onCallBack.call(window);
            }
        }
    });
    return true;
}

function showFlowChart() {
    var id = $('#detailPageId').val();
    UICtrl.addTabItem({
        tabid: 'businessFlowChart' + id,
        text: "流程图维护",
        url: web_app.name + '/bizFlowChart/forwardFlowchart.load?businessProcessId=' + id
    });
}

function deleteFlowChart(div) {
    var businessProcessId = $('#detailPageId').val();
    UICtrl.confirm('您确定要删除该流程图吗,删除后无法恢复?', function () {
        Public.ajax(web_app.name + '/bizFlowChart/deleteFlowChart.ajax', {businessProcessId:businessProcessId});
    });
}

function viewFlowChart(){
	var id = $('#detailPageId').val();
    UICtrl.addTabItem({
        tabid: 'viewFlowChart' + id,
        text: "流程图预览",
        url: web_app.name + '/bizFlowChart/showViewFlowchart.load?businessProcessId=' + id
    });
}