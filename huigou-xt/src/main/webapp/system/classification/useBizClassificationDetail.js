var bizTableGridManager={};

function initializeUseUI() {
    UICtrl.layout("#layoutDetail", {heightDiff:-5,leftWidth:3,
        onSizeChanged:function(){
        	$.each(bizTableGridManager,function(k,gridManager){
        		if(gridManager){
        			gridManager._onResize.call(gridManager);
        		}
        	});
        }
    });
    loadBizClassificationsTree();
    bindBizClassificationEvent();
}

function onOrgIdChange(orgId){
	$('#mainOrgId').val(orgId);
	var current=$('#bizClassificationDetails').find('li.active');
	if(current.length>0){
		showClassificationDetails(current); 
	}
	$.each(bizTableGridManager,function(k,gridManager){
		if(gridManager){
			gridManager.options.parms['orgId'] = orgId;
		    gridManager.loadData();
		}
	});
}

//加载业务分类树
function loadBizClassificationsTree() {
  $('#maintree').commonTree({
      loadTreesAction: 'bizClassification/queryBizClassificationsByPermission.ajax',
      onClick: function (data) {
    	  $('#bizClassificationId').val(data.id);
    	  queryBizClassificationDetail();
      },
      dataRender: function (data) {
          return data;
      },
      IsShowMenu: false
  });
}


//点击树节点时加载表格
function queryBizClassificationDetail() {
    var bizClassificationId=$('#bizClassificationId').val();
    var url=web_app.name + '/bizClassification/queryDetailsByClassificationId.ajax';
    Public.ajax(url, {bizClassificationId:bizClassificationId}, function(data){
    	//清空旧数据
    	$('#bizClassificationDetails').removeAllNode();
    	bizTableGridManager={}
    	if(!data||!data.length){
    		var div=$('<div id="bizClassificationDetails">选择的分类没有业务配置!</div>').appendTo($('#layoutDetailCenter'));
    		div.css({color:'#aaaaaa',fontWeight:'700',fontSize:'30px',padding:'50px',textAlign:'center'});
    		return;
    	}
    	var div=$('<div id="bizClassificationDetails" style="border:0px;margin: 0;"><div>').appendTo($('#layoutDetailCenter'));
    	var links=['<div class="ui-tab-links"><ul style="left: 20px;">'],contents=['<div class="ui-tab-content" style="padding: 2px;">'];
    	$.each(data,function(i,o){
    		links.push('<li id="',o.id,'" bizCode="',o.bizCode,'" bizType="',o.bizType,'"');
    		links.push(' bizPropertyId="',o.bizPropertyId,'" tableName="',o.tableName,'" dialogWidth="',o.dialogWidth,'">');
    		links.push(o.bizName,'</li>');
    		contents.push('<div class="layout" id="div_',o.id,'"></div>');
    	});
    	links.push('</ul></div>');
    	contents.push('</div>');
    	links.push.apply(links,contents);
    	div.html(links.join(''));
    	div.tab();
    	initTabLayoutHeight();
    	$(div).find('li:first').trigger('click');
    });
}

function initTabLayoutHeight(){
	var middleHeight=$('#layoutDetailCenter').height();
	$('#bizClassificationDetails').find('div.layout').height(middleHeight-35); 
}

//tab切换事件
function bindBizClassificationEvent(){
	$('#layoutDetailCenter').on('click',function(e){
		var $clicked = $(e.target || e.srcElement);
        if ($clicked.is('li')) {
        	showClassificationDetails($clicked);
        } else if ($clicked.parent().is('li')) {
        	showClassificationDetails($clicked.parent());
        }else if($clicked.hasClass('save-detail')){
        	saveFlexField($clicked.attr('id'));
        }
	});
}
//明细表展示
function showClassificationDetails(li){
	var param={
        id:li.attr('id'),
        bizPropertyId:li.attr('bizPropertyId'),
        bizCode:li.attr('bizCode'),
        bizType:li.attr('bizType'),
        tableName:li.attr('tableName'),
        dialogWidth:li.attr('dialogWidth'),
        title:li.text()
    };
	if(param.bizType=='bizTable'){//业务表
		intBizTable(param);
	}else{//业务字段
		intFlexField(param);
	}
}

function intFlexField(param){
	var orgId=$('#mainOrgId').val(),parentDiv=$('#div_'+param.id).css('overflow','auto');
	var div=$('#flexField_'+param.id);
	if(!div.length){
		div=$('<div id="flexField_'+param.id+'" bizCode="'+param.bizCode+'"></div>').appendTo(parentDiv);
		var html=['<div style="min-height:40px;text-align:center;margin-top:5px;">'];
		html.push('<button type="button" class="btn btn-primary save-detail" id="button_',param.id,'" hidefocus="">');
		html.push('<i class="fa fa-save"></i>&nbsp;保存修改</div>');
		html.push('</button>');
		html.push('</div>');
		$(html.join('')).appendTo(parentDiv);
	}
	//加载扩展字段
	div.flexField({bizCode: param.bizCode,bizId:orgId});
}

function saveFlexField(buttonId){
	var orgId=$('#mainOrgId').val();
	if (Public.isBlank(orgId)) {
		Public.tip('请选择对应组织机构!');
		return false;
	}
	var id=buttonId.replace('button_','');
	var div=$('#flexField_'+id);
	var bizCode=div.attr('bizCode');
	var param=div.flexField('getFieldValues');
	if(!param){
		return false;
	}
	param['bizCode']=bizCode;
	param['orgId']=orgId;
	var url=web_app.name + '/bizClassificationFlexField/saveFlexFiledDatas.ajax';
	Public.ajax(url, param, function(data){});
}

function intBizTable(param){
	//存在则不加载配置
	if(bizTableGridManager[param.id]){
		return;
	}
	//查询业务表表头
	var url=web_app.name + '/bizClassification/queryClassificationVisibleFields.ajax';
	Public.ajax(url, {bizPropertyId:param.bizPropertyId}, function(data){
		var columns=[];
		$.each(data,function(i,o){
			var width=parseInt(o['controlWidth']);
			width=isNaN(width)?100:width;
			width=width<=0?100:width;
			var fieldType=o['fieldType'],type='string',align='left';
			if(fieldType=='2'){
				type='number';
				align='right';
			}else if(fieldType=='3'){
				type='date';
			}else if(fieldType=='4'){
				type='datetime';
			}
			var filed={
				display:o['description'],
				name: o['fieldName'],
				width:width,
				minWidth: 60,
				type: type,
				align:align
			};
			columns.push(filed);
		});
		var gridManager=getDetailGridManager(param,columns);
		bizTableGridManager[param.id]=gridManager;
	});
}

function getDetailGridManager(param,columns){
	var div=$('#grid_'+param.id),parentDiv=$('#div_'+param.id);
	if(!div.length){
		div=$('<div id="grid_'+param.id+'"></div>').appendTo(parentDiv);
	}else{
		return;
	}
	var orgId=$('#mainOrgId').val();
	var toolbarOptions = UICtrl.getDefaultToolbarOptions({
		addHandler:function(){
			var orgId=$('#mainOrgId').val();
			if (Public.isBlank(orgId)) {
				Public.tip('请选择对应组织机构!');
				return false;
			}
			showTableDetailDialog({
				bizclassificationdetailId:param.id,
				bizCode:param.bizCode,
				bizId:'',
				title:'添加['+param.title+']',
				width:param.dialogWidth
			});
		},
		updateHandler:function(){
			var row = gridManager.getSelectedRow();
			if (!row) {Public.tip('请选择数据！'); return; }
			showTableDetailDialog({
				bizclassificationdetailId:param.id,
				bizCode:param.bizCode,
				bizId:row.id,
				title:'编辑['+param.title+']',
				width:param.dialogWidth
			});
		},
		deleteHandler:function(){
			DataUtil.delSelectedRows({action:'bizClassificationFlexField/deleteDatas.ajax',
				gridManager:gridManager,param:{tableName:param.tableName,bizCode:param.bizCode},
				onSuccess:function(){
					gridManager.loadData();
				}
			});
		}
	});
	var gridManager=UICtrl.grid(div, {
		columns:columns,
		dataAction : 'server',
		url:web_app.name +'/bizClassificationFlexField/sliceQueryBizTables.ajax',
		parms:{orgId:orgId,bizCode:param.bizCode,tableName:param.tableName},
		width :'100%',
		height:'100%',
		heightDiff: -15,
		usePager: true,
		checkbox: true,
		sortName:'version',
		sortOrder:'asc',
		toolbar: toolbarOptions,
		onDblClickRow : function(row, rowindex, rowobj) {
			showTableDetailDialog({
				bizclassificationdetailId:param.id,
				bizCode:param.bizCode,
				bizId:row.id,
				title:'编辑['+param.title+']',
				width:param.dialogWidth
			});
		},
		onLoadData :function(){
			return !($('#mainOrgId').val()=='');
		}
	});
	return gridManager;
}

function showTableDetailDialog(op){
	var width=parseInt(op.width);
	width=isNaN(width)?400:width;
	width=width<=0?400:width;
	UICtrl.showDialog({title:op.title,width:width+20,
		content:'<div class="showFlexField" style="width:'+width+'"></div>',
		ok:function(doc){
			var orgId=$('#mainOrgId').val();
			if (Public.isBlank(orgId)) {
				Public.tip('请选择对应组织机构!');
				return false;
			}
			var _self=this;
			var div=$('div.showFlexField',doc);
			var param=div.flexField('getFieldValues');
			if(!param){
				return false;
			}
			var bizCode=op.bizCode;
			var bizId=op.bizId;
			param['orgId']=orgId;
			param['bizCode']=bizCode;
			param['bizclassificationdetailId']=op.bizclassificationdetailId;
			var url=web_app.name + '/bizClassificationFlexField/insertData.ajax';
			if (!Public.isBlank(bizId)) {
				 url=web_app.name + '/bizClassificationFlexField/updateData.ajax';
				 param['detailId']=bizId;
			}
			Public.ajax(url, param, function(data){
				_self.close();
				bizTableGridManager[op.bizclassificationdetailId].loadData();
			});
			return false;
		},
		init:function(doc){
			$('div.showFlexField',doc).flexField({bizCode: op.bizCode,bizId:op.bizId,needTitle:false});
		}
	});
}