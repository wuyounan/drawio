var BPMCUtil=BPMCUtil||{};

//页面关闭前验证数据是否被修改
function checkDefaultValueModified(){
	return BPMCUtil.checkDefaultValueModified();
}

BPMCUtil.checkDefaultValueModified=function(){
	return false;
}
//是否已绑定window resize 事件
BPMCUtil._isInitResize=false;
//数据是否改变
BPMCUtil._isModified=false;
//是否需要刷新列表
BPMCUtil._refreshFlag=false;
//资源选时是否添加推荐表格
BPMCUtil.recommendedWhenSelecting=false;
//资源荐表格查询参数获取方法
BPMCUtil.getRecommendedWhenSelectingParam=function(){
	return {};
};
//关联资源显示表格
BPMCUtil.relationGridManager=null;

//初始化页面参数
BPMCUtil.initializePageParameters=function(refreshFlag){
	BPMCUtil._refreshFlag=refreshFlag;
	BPMCUtil._isModified=false;
	//设置form组件的默认值
	Public.updateDefaultValue($('#editAttributePageDiv'));
};

BPMCUtil.initializeWindowResize=function(refreshFlag){
	BPMCUtil.setAttributePageHeight();
	if(BPMCUtil._isInitResize){
		return;
	}
	BPMCUtil._isInitResize=true;
	$.WinReszier.register(function(){
		BPMCUtil.setAttributePageHeight();
	});
};

BPMCUtil.setAttributePageHeight=function(){
	var div=$('#editAttributePageDiv');
	if(!div.length){
		return;
	}
	div.height(div.parent().height());
};

//是否是编辑属性模式
BPMCUtil.isEditAttributePage=function(){
	var div=$('#editAttributePageDiv');
	if(!div.length){
		return false;
	}
	return div.is(':visible');
};
//进入编辑属性模式
BPMCUtil.createEditAttributePage=function(parentDom,url,param,callback) {
	var _toDo=function(){
		var div=$('#editAttributePageDiv');
		if(div.length > 0){
			div.removeAllNode();
		}
		div=$('<div class="dom-overflow-auto" id="editAttributePageDiv"></div>').appendTo(parentDom);
		div.height($(parentDom).height());
		div.css({position:'absolute',width:'99.8%',zIndex:10,top: 1, left:1,background:'#ffffff'});
		Public.load(url, param, function(data){
			div.html(data);
			if ($.isFunction(callback)) {
	            callback.call(window, div);
	        }
			//初始化属性编辑div
			//BPMCUtil.initEditAttributePage(div);
			//初始化页面参数
			BPMCUtil.initializePageParameters(false);
		});
		BPMCUtil.initializeWindowResize();
	};
	var flag=BPMCUtil.checkDefaultValueModified();
	if(flag===true){
		UICtrl.confirm('您修改的数据尚未保存，确定离开此页面吗？',function(){
			_toDo();
		});
	}else{
		_toDo();
	}
};
//创建按钮
BPMCUtil.createFormButton=function(op){
	if($.isArray(op)){
		var buttons=[];
		$.each(op,function(i,o){
			buttons.push(o);
		});
		op={buttons:buttons};
	}
	op['holdBar']=false;
	var div=$('#editAttributePageDiv');
	var bar=$('#editAttributePageFormBar');
	//var hold=$('#editAttributePageHoldBar');
	if(bar.length > 0){
		bar.removeAllNode();
		//hold.removeAllNode();
	}
	bar=$('<div class="ui-form-bar ui-form-bar-ground" id="editAttributePageFormBar"></div>').appendTo(div.parent());
	//hold=$('<div class="ui-hold-bar" id="editAttributePageHoldBar"></div>').appendTo(div);
	bar.css({position:'absolute'});
	bar.formButton(op);
};

//关闭属性编辑框
BPMCUtil.hideEditAttributePage=function(){
	var _toDo=function(){
		if(window['reloadGridByLastSelectedId']){
			window['reloadGridByLastSelectedId'].call(window);
		}
		var div=$('#editAttributePageDiv');
		if(div.length > 0){
			div.removeAllNode();
		}
		var bar=$('#editAttributePageFormBar');
		if(bar.length > 0){
			bar.removeAllNode();
		}
		$.closePicker();
	};
	var flag=BPMCUtil.checkDefaultValueModified();
	if(flag===true){
		UICtrl.confirm('您修改的数据尚未保存，确定离开此页面吗？',function(){
			_toDo();
		});
	}else{
		_toDo();
	}
};

//删除对象
BPMCUtil.deleteObject=function(options){
	var message=options.message||'您确认删除选中数据及其子数据吗,删除后无法恢复?';
	options = $.extend({nochooseMessage: '请选择需要删除的数据!'}, options||{});
	var ids = DataUtil.getSelectedIds(options);
	if (!ids) return;
	var params = options.param || {};
	params.ids = $.toJSON(ids);
	var _do = function () {
		Public.ajax(web_app.name + '/' + options.action, params, function (data) {
			if ($.isFunction(options.onSuccess)){
				options.onSuccess(data);
       	 	} 
		});
	 };
	 UICtrl.confirm(message, function () {
		 _do();
     });
};