var groupGridManager = null,detailGridManager = null;

function initializeEditPageUI(div){
	UICtrl.layout($('div.layout',div),{topHeight:$('#submitForm',div).height()+5,leftWidth:5,allowLeftCollapse:false,allowLeftResize:false});
	initializeGroupGrid(div);
	initializeDetailGrid(div);
	Public.autoInitializeUI(div);
}

function doClose(){
	BPMCUtil.hideEditAttributePage();
}

function doSaveMain(form) {
    $(form).ajaxSubmit({
        url: web_app.name + '/groupFunction/updateFunctions.ajax',
        success: function (data) {
            reloadGrid();
        }
    });
}

function doView(form) { 
	var code = $('#code',form).val();
	UICtrl.addTabItem({
	    tabid: 'viewFgroup' + code,
	    text: $('#name',form).val(),
	    url: web_app.name + '/groupFunction/' + code+'/show.do'
	});
}
function initializeGroupGrid(div) {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function(){
        	 UICtrl.showAjaxDialog({
        		 title: '新增功能分组',
        	     width: 400,
        	     url: web_app.name + '/groupFunction/showInsertFunctionsGroup.load',
        	     ok: function(d){
        	    	 var _self = this;
        	    	 $('#submitForm',d).ajaxSubmit({
        	    	     url: web_app.name + '/groupFunction/insertFunctionsGroup.ajax',
        	    	     param:{bpmFunctionsId: $("#functionsId").val()},
        	    	     success: function (data) {
        	    	    	 groupGridManager.loadData();
        	    	         _self.close();
        	    	     }
        	    	 });
        	     }
        	 });
        },
        updateHandler: function () {
        	updateFunctionsGroup();
        },
        deleteHandler: function(){
        	DataUtil.del({
                action: 'groupFunction/deleteFunctionsGroup.ajax',
                gridManager: groupGridManager, idFieldName: 'id',
                onSuccess: function () {
                	groupGridManager.loadData();
                }
            });
        },
        saveSortIDHandler: function(){
        	var action = "groupFunction/updateFunctionsGroupSequence.ajax";
            DataUtil.updateSequence({
                action: action, gridManager: groupGridManager, idFieldName: 'id', onSuccess: function () {
                	groupGridManager.loadData();
                }
            });
        },
        showAll:{id:'showAll',text:'显示全部',img:'fa-link',click:function(){
        	$('#chooseFunctionsGroupId').val('');
    		$('div.layout',div).layout('setCenterTitle','功能列表');
    		UICtrl.gridSearch(detailGridManager,{functionsGroupId:''});
		}}
    });
    var _grid=$('div.groupgrid',div);
    groupGridManager = UICtrl.grid(_grid, {
        columns: [
            { display: "操作", name: "code", width: 60, minWidth: 60, type: "string", align: "left",
            	render: function (item) {
                    return '<a class="gridLink" href="javascript:void(0);" data-id="'+item.id+'" data-name="'+item.nameZh+'">编辑功能</a>';
                }
            },
            { display: "中文描述", name: "nameZh", width: 150, minWidth: 60, type: "string", align: "left" },	
            { display: "英文描述", name: "nameEn", width: 150, minWidth: 60, type: "string", align: "left" },	
            { display: "颜色", name: "colorTextView", width: 60, minWidth: 60, type: "string", align: "left" },
            { display: "common.field.sequence", name: "sequence", width: 80, minWidth: 60, type: "number", align: "right",
            	render: function (item) {
                    return UICtrl.sequenceRender(item);
                }
    		}
        ],
        dataAction: 'server',
        url: web_app.name + '/groupFunction/queryFunctionsGroup.ajax',
        parms: {bpmFunctionsId: $("#functionsId").val()},
        width: '100%',
        height: '100%',
        heightDiff: -200,
        sortName: 'sequence',
        sortOrder: 'asc',
        checkbox: true,
        usePager: false,
        toolbar: toolbarOptions,
        onDblClickRow: function (data, rowindex, rowobj) {
        	updateFunctionsGroup(data.id);
        }
    });
    _grid.on('click',function(e){
    	var $clicked = $(e.target || e.srcElement);
    	if($clicked.is('a.gridLink')){
    		var id=$clicked.data('id'),text=$clicked.data('name'),html=[];
    		$('#chooseFunctionsGroupId').val(id);
    		html.push('<span class="tomato-color">[',text,']</span>','功能列表');
    		$('div.layout',div).layout('setCenterTitle',html.join(''));
    		UICtrl.gridSearch(detailGridManager,{functionsGroupId:id});
    	}
    });
}

function updateFunctionsGroup(id){
	if (!id) {
        var id = DataUtil.getUpdateRowId(groupGridManager);
        if (!id) {
            return;
        }
    }
	UICtrl.showAjaxDialog({
		 title: '修改功能分组',
	     width: 400,
	     url: web_app.name + '/groupFunction/showLoadFunctionsGroup.load',
	     param:{id:id},
	     ok: function(d){
	    	 var _self = this;
	    	 $('#submitForm',d).ajaxSubmit({
	    	     url: web_app.name + '/groupFunction/updateFunctionsGroup.ajax',
	    	     success: function (data) {
	    	    	 groupGridManager.loadData();
	    	         _self.close();
	    	     }
	    	 });
	     }
	 });
}


function initializeDetailGrid(div) {
    var toolbarOptions = UICtrl.getDefaultToolbarOptions({
        addHandler: function(){
        	var functionsGroupId=$('#chooseFunctionsGroupId').val();
        	if (Public.isBlank(functionsGroupId)) {
        		Public.tip('请选择对应的分组!');
                return false;
            }
        	UICtrl.showAjaxDialog({
	       		 title: '新增功能',
	       	     width: 400,
	       	     url: web_app.name + '/groupFunction/showInsertFunctionsDetails.load',
	       	     init:function(d){
	       	    	intFunctionsDetails(d);
	       	     },
	       	     ok: function(d){
	       	    	 var _self = this;
	       	    	 $('#submitForm',d).ajaxSubmit({
	       	    	     url: web_app.name + '/groupFunction/insertFunctionsDetails.ajax',
	       	    	     param:{bpmFunctionsId: $("#functionsId").val(),functionsGroupId:functionsGroupId},
	       	    	     success: function (data) {
	       	    	    	detailGridManager.loadData();
	       	    	         _self.close();
	       	    	     }
	       	    	 });
	       	     }
       	 	});
        },
        updateHandler: function () {
        	updateFunctionsDetails();
        },
        deleteHandler: function(){
        	DataUtil.del({
                action: 'groupFunction/deleteFunctionsDetails.ajax',
                gridManager: detailGridManager, idFieldName: 'id',
                onSuccess: function () {
                	detailGridManager.loadData();
                }
            });
        },
        saveSortIDHandler: function(){
        	var action = "groupFunction/updateFunctionsDetailsSequence.ajax";
            DataUtil.updateSequence({
                action: action, gridManager: detailGridManager, idFieldName: 'id', onSuccess: function () {
                	detailGridManager.loadData();
                }
            });
        },
        moveFuntion:{id:'moveFuntion',text:'移动',img:'fa-arrows',click:function(){
        	var ids=DataUtil.getSelectedIds({gridManager:detailGridManager});
			if(!ids) return false;
        	var datas = DataUtil.getGridData({ gridManager: groupGridManager, isAllData: true });
        	var html=['<ul class="list-group">'];
        	$.each(datas,function(i,o){
        		html.push('<li class="list-group-item">',o.nameZh,'&nbsp;&nbsp;<button type="button" class="btn btn-info btn-sm" data-id="',o.id,'">确定</button></li>');
        	});
        	html.push('</ul>');
        	UICtrl.showDialog({
	       		 title: '移动功能',
	       	     width: 400,
	       	     content:html.join(''),
	       	     init:function(d){
	       	    	 var _self = this;
	       	    	 $(d).on('click',function(e){
	       	    		 var $clicked = $(e.target || e.srcElement);
	       	    		 if($clicked.is('button')){
	       	    			 var id=$clicked.data('id');
	       	    			 Public.ajax(web_app.name + '/groupFunction/updateFunctionsDetailsGroup.ajax', {groupId:id,ids:$.toJSON(ids)}, function (data) {
	       	    				  detailGridManager.loadData();
		       	    	          _self.close();
	       	    	         });
	       	    		 }
	       	    	 });
	       	     },
	       	     ok:false
     	 	});
		}},
		changeColor:{id:'changeColor',text:'修改颜色',img:'fa-link',click:function(){
			var ids=DataUtil.getSelectedIds({gridManager:detailGridManager});
			if(!ids) return false;
			UICtrl.showAjaxDialog({
	       		 title: '修改颜色',
	       	     width: 400,
	       	     url: web_app.name + '/groupFunction/showChooseFunctionsDetailColor.load',
	       	     ok: function(d){
	       	    	 var _self = this;
	       	    	 $('#submitForm',d).ajaxSubmit({
	       	    	     url: web_app.name + '/groupFunction/updateFunctionsDetailsColor.ajax',
	       	    	     param:{ids:$.toJSON(ids)},
	       	    	     success: function (data) {
	       	    	    	detailGridManager.loadData();
	       	    	         _self.close();
	       	    	     }
	       	    	 });
	       	     }
      	 	});
		}}
    });
    detailGridManager = UICtrl.grid($('div.detailgrid',div), {
        columns: [
            { display: "编号", name: "code", width: 100, minWidth: 60, type: "string", align: "left" },
			{ display: "中文描述", name: "nameZh", width: 200, minWidth: 60, type: "string", align: "left" },	
			{ display: "英文描述", name: "nameEn", width: 200, minWidth: 60, type: "string", align: "left" },	
			{ display: "颜色", name: "colorTextView", width: 60, minWidth: 60, type: "string", align: "left" },
			{ display: "common.field.sequence", name: "sequence", width: 80, minWidth: 60, type: "number", align: "right",
				render: function (item) {
			        return UICtrl.sequenceRender(item);
			    }
			},
			{ display: "连接", name: "url", width: 200, minWidth: 60, type: "string", align: "left" }
        ],
        dataAction: 'server',
        url: web_app.name + '/groupFunction/queryFunctionsDetails.ajax',
        parms: {bpmFunctionsId: $("#functionsId").val()},
        width: '100%',
        height: '100%',
        heightDiff: -200,
        checkbox: true,
        sortName: 'sequence',
        sortOrder: 'asc',
        usePager: false,
        toolbar: toolbarOptions,
        onDblClickRow: function (data, rowindex, rowobj) {
        	updateFunctionsDetails(data.id);
        }
    });
}

function intFunctionsDetails(div){
	var _code=$('#code',div);
	_code.treebox({
		name:"opFunction",
		onChange:function(value,data){
			_code.val(data.code);
			$('#nameZh',div).val(data.name);
		}
	});
}

function updateFunctionsDetails(id){
	if (!id) {
        var id = DataUtil.getUpdateRowId(detailGridManager);
        if (!id) {
            return;
        }
    }
	UICtrl.showAjaxDialog({
		 title: '修改功能',
	     width: 400,
	     url: web_app.name + '/groupFunction/showLoadFunctionsDetails.load',
	     param:{id:id},
	     init:function(d){
    	     intFunctionsDetails(d);
    	 },
	     ok: function(d){
	    	 var _self = this;
	    	 $('#submitForm',d).ajaxSubmit({
	    	     url: web_app.name + '/groupFunction/updateFunctionsDetails.ajax',
	    	     success: function (data) {
	    	    	 detailGridManager.loadData();
	    	         _self.close();
	    	     }
	    	 });
	     }
	 });
}