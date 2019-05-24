$(document).ready(function() {
	initializeUI();
	initToolBar();
	loadFlowNodesAndLines();
	FlowChartQueryParam.init();
});

function initToolBar(){
	var _bar=$('#bottomToolBar');
	if(!_bar.length) return;
	_bar.toolBar([
	       { id: 'doNoticeTask', remark: '确定',name:'common.button.ok', className:'btn-danger',disabled:Public.isReadOnly, icon: 'fa-address-card',
	    	   event: function(){
	    		   //完成任务
	    		   Public.ajax(web_app.name + '/workflow/completeTask.ajax',{ taskId: $('#taskProcessingId').val() }, function (data) {
	    			   UICtrl.closeAndReloadTabs("TaskCenter", null,function(){
	    				   Public.closeWebPage();
	    			   });
	    		   });         	
	    	   }
	       }
	]);
}

function initializeUI(){
	var flowChartDiv=$('#flowChartDiv'),functionMap=null;
	flowChartDiv.flowChart({
		haveAxes:false,
		readonly:true,
		haveTool: false, 
		haveGroup: true,
		onSetActivityRelation:function(node){
			var linkKindCodes=node.linkKindCodes||'';
			if(Public.isBlank(linkKindCodes)){
				return '';
			}
			var html=[];
			var _fontSizeStyle=this._getFontSizeStyle();
			var _upHeight=this._getSize('upHeight');
			$.each(linkKindCodes.split(','),function(i,code){
				html.push("<span class='relation' style='width:",_upHeight,"px;height:",_upHeight,"px;font-size:20px;margin-top:3px;'>");
				html.push("<span style='",_fontSizeStyle,"'><i class='fa ",code,"'></i></span>");
				html.push("</span>");
			});
			return html.join('');
		},
		onInit:function(data){
			var chartObj=this.flowChart,_self=this;
			if(!chartObj.$workArea.find('div.FlowChart_item').length){
				return;
			}
			var noPermissions=data['noPermissions'];
			if (Public.isNotBlank(noPermissions)) {
				$.each(noPermissions.split(','),function(i,nodeId){
					var _item=$('#'+nodeId);
					if(_item.length>0){
						_item.addClass('disable-item');
					}
				});
			}
			var focusNodeCode=$('#focusNodeCode').val();
			if (Public.isNotBlank(focusNodeCode)) {
				$.each(focusNodeCode.split(','),function(i,c){
					_self.fixedAndFocusNode(c);
				});
				//没有高亮显示的节点禁用
				disableNotFocusItem();
			}
			//获取定义的功能连接
			if(data['functions']){
				functionMap=data['functions'];
			}
			if(!$.isPlainObject(functionMap)){
				functionMap={};
			}
			//注册功能显示事件
			chartObj.$workArea.find('div.FlowChart_item').each(function(){
				if($(this).hasClass('disable-item')){
					return;
				}
				var nodeId=$(this).attr('id'),nodeData=chartObj.$nodeData[nodeId];
		    	if(!nodeData){
		    		return;
		    	}
		    	if(nodeData.objectKindCode==NodeKind.RULE){
					return;
				}
		    	if(nodeData.objectKindCode==NodeKind.SHADOW){
		    		var quoteNode=_self.getShadowNodeInfo(nodeData);
		    		if(quoteNode){
		    			//引用的节点没权
		    			if($('#'+quoteNode['id']).hasClass('disable-item')){
		    				$('#'+nodeId).addClass('disable-item');
		    				return;
		    			}
		    			//影子节点高亮
		    			if($('#'+quoteNode['id']).hasClass('highlight-item')){
		    				$('#'+nodeId).addClass('highlight-item');
		    			}
		    			//修改功能取值ID
		    			nodeId=quoteNode['id'];
		    		}
		    	}
				var _selfNode=$(this),funs=functionMap[nodeId];
				if(!funs||!funs.length){
					_selfNode.addClass('disable-item').on('click',function(){
						Public.tip('common.error.no.authority');
					});
					return true;
				}
				_selfNode.hover(function(){_selfNode.addClass('hover-item');},function(){_selfNode.removeClass('hover-item');});
				_selfNode.find('span,td').css({cursor:'pointer'});
				_selfNode.on('click',function(){
					var _url=funs[0].url,_paramStr=funs[0].param,code=funs[0].code;
					if (Public.isBlank(_url)) {
						return;
					}
					if(funs[0].isFunction=='0'){
						code='';
					}
					_url=FlowChartQueryParam.getParseUrl(_selfNode,_url,code,_paramStr);
					UICtrl.addTabItem({tabid: 'v' + new Date().getTime(),text:funs[0].name,url: _url});
				});
				if(funs.length > 1){
					var _tmpFun={},html=['<table class="table-fixed">'];
					$.each(funs,function(i,o){
						_tmpFun[o.id]=o;
						html.push('<tr>');
						html.push('<td style="border-bottom: 1px dotted #d0d0d0;">');
						html.push('<a href="javascript:void(0);" class="fun" style="line-height:25px" data-id="',o.id,'">');
						html.push('<i class="fa ',o.icon,'"></i>&nbsp;',o.name);
						html.push('</a>');
						html.push('</td>');
						html.push('</tr>');
					});
					html.push('</table>');
					_selfNode.tooltip({position:'right',width:220,
						content:html.join(''),
						onShow:function(tip){
							$(tip).on('click.tooltipClick',function(e){
								var $clicked = $(e.target || e.srcElement);
								if($clicked.is('i.fa')){
									$clicked=$clicked.parent();
								}
								if($clicked.hasClass('fun')){//删除
									var o=_tmpFun[$clicked.data('id')];
									var _url=o.url,_paramStr=o.param,_code=o.code;
									if (Public.isBlank(_url)) {
										return;
									}
									if(o.isFunction=='0'){
										_code='';
									}
									_url=FlowChartQueryParam.getParseUrl(_selfNode,_url,_code,_paramStr);
									UICtrl.addTabItem({tabid: 'v' + new Date().getTime(),text:o.name,url: _url});
								}
							});
						},
						onHide:function(tip){
							$(tip).off('click.tooltipClick').remove();//删除绑定事件
						}
					});
				}
			});
		}
	});
	UICtrl.layout("#layout",{onSizeChanged:function(){
		if(flowChartDiv.hasClass('FlowChart')){
			setTimeout(function(){
				flowChartDiv.removeClass('ui-hide').flowChart('reInitSize',flowChartDiv.parent().outerWidth(),flowChartDiv.parent().outerHeight());
			},0);
		}
	}});
	$('#layout').find('div.ui-layout-header-toggle').addClass('m-t-sm');
	initLaoutTop();
	//左边悬浮菜单
	/*$.floatDialog({
		openWidth:30,
		openHeight:70,
		title:'流程图菜单',
		openHtml:'<span>流程图</span>',
		content:'<div style="overflow:auto;width:300px;height:340px;"><ul class="processTree"></ul></div>',
		onInit:function(div){
			$('ul.processTree',div).commonTree({
		        loadTreesAction: 'bizBusinessProcess/queryBusinessProcesses.ajax',
		        parentId:'0',
		        nodeWidth:160,
		        changeNodeIcon: function (data) {
		        	var isFinal=data.isFinal;
		        	if(isFinal==1){
		        		data[this.options.iconFieldName] =web_app.name + "/images/icons/application.png";
		        	}
		        },
		        onClick: function (data) {
		        	var isFinal=data.isFinal;
		        	if(isFinal!=1){return;}
		        	reLoadFlowChart(data.id);
		        },
		        dataRender: function (data) {
		            return data;
		        },
		        IsShowMenu: false
		    });
		},
		onClose:function(div){
			$('ul.processTree',div).parent().removeAllNode();
		}
	});*/
}

function onWrapperResize(_size){
	var flowChartDiv=$('#flowChartDiv');
	flowChartDiv.flowChart('reInitSize',flowChartDiv.parent().outerWidth(),flowChartDiv.parent().outerHeight());
}

function initLaoutTop(){
	var _remark=$('#businessProcessRemark').text();
	_remark=$.trim(_remark);
	$('#layout').layout('setTopHeight',_remark.length>0?50:1);
}

function geBusinessProcessId(){
	return $('#businessProcessId').val();
}

function loadFlowNodesAndLines(){
	var businessProcessId=geBusinessProcessId();
	Public.ajax(web_app.name + '/bizFlowChart/queryFlowNodesAndLinesAndFunction.ajax', {businessProcessId:businessProcessId}, function (data) {
		$('#flowChartDiv').flowChart('initData',data);
	});
}

function reLoadFlowChart(pid){
	var businessProcessId=geBusinessProcessId();
	if(pid==businessProcessId){
		return;
	}
	Public.locationHref('bizFlowChart/showViewFlowchart.load',{businessProcessId:pid});
}

//没有高亮显示的节点禁用
function disableNotFocusItem(){
	$('div.FlowChart_item',$('#flowChartDiv')).each(function(){
		if($(this).hasClass('highlight-item')){//高亮节点
			return true;
		}
		if($(this).hasClass('item_round')){//规则节点
			return true;
		}
		$(this).addClass('disable-item');
	});
}


var FlowChartQueryParam={};
FlowChartQueryParam.data={};

FlowChartQueryParam.init=function(){
	var url=$('#queryParamUrl').val();
	if (Public.isBlank(url)) {
		return;
	}
	var businessProcessId=geBusinessProcessId();
	Public.ajax(web_app.name + '/'+url, {}, function (data) {
		if (Public.isNotBlank(data['processRemark'])) {
			$('#businessProcessRemark').html(data['processRemark']);
		}
		var focusNodeCode=data['focusNodeCode'];
		if (Public.isNotBlank(focusNodeCode)) {
			$('#focusNodeCode').val(focusNodeCode);
			$.each(focusNodeCode.split(','),function(i,c){
				$('#flowChartDiv').flowChart('fixedAndFocusNode',c);
			});
			//没有高亮显示的节点禁用
			disableNotFocusItem();
		}
		initLaoutTop();
		FlowChartQueryParam.setQueryData(data);
	});
};

FlowChartQueryParam.setQueryData=function(data){
	FlowChartQueryParam.data=data;
};

FlowChartQueryParam.getParam=function(keys){
	if (Public.isBlank(keys)) {
		return {};
	}
	if (typeof keys == 'string') {
		keys=Public.getJSONDataSource(keys);
	}
	if($.isEmptyObject(keys)){
		return {};
	}
	var _param={};
	$.each(keys,function(k,o){
		var _v=FlowChartQueryParam.data[o];
		if (Public.isNotBlank(_v)) {
			_param[k]=_v;
		}
	});
	return _param;
};

FlowChartQueryParam.getParseUrl=function(_node,action,code,keys){
	var url = action;
	if(action.startsWith('http')){
		url=action;
	}else{
		url=web_app.name+ "/" +action;
	}
	//只有高亮的才拼接参数
	var param=_node.hasClass('highlight-item')?FlowChartQueryParam.getParam(keys):{};
	if (Public.isNotBlank(code)) {
		param['functionCode']=code;
	}
	if(param&&!$.isEmptyObject(param)){
		url+=(/\?/.test(url) ? '&' : '?')+ $.param(param);
	}
	return url;
};