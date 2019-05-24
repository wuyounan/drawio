$(document).ready(function() {
	initializeUI();
	loadWorkFlowChartData();
});
function initializeUI(){
	var flowChartDiv=$('#flowChartDiv');
	flowChartDiv.workFlowChart({haveAxes:false});
	UICtrl.layout("#layout",{onSizeChanged:function(){
		if(flowChartDiv.hasClass('FlowChart')){
			setTimeout(function(){
				flowChartDiv.removeClass('ui-hide').workFlowChart('reInitSize',flowChartDiv.parent().outerWidth(),flowChartDiv.parent().outerHeight());
			},0);
		}
	}});
	$('#layout').find('div.ui-layout-header-toggle').addClass('m-t-sm');
}

//加载数据
function loadWorkFlowChartData(){
	var bizId=$('#bizId').val();
	Public.ajax(web_app.name + '/workflow/queryFlowChart.ajax', {bizId:bizId}, function (data) {
		//创建数据解析对象
		var parser=new parseProcChartData(data.procUnits);
		//执行解析
		var chartData=parser.doParse();
		$('#flowChartDiv').workFlowChart('initData',chartData);
	});
}

//后台数据解析类
function parseProcChartData(procUnitDatas){
	this.procUnits=procUnitDatas;
	this.procNodes=[];
	this.chartNodes=[];
	this.chartLines=[];
	this.prevProcUnitId=null;
	this.currentProcUnitId=null;
	this.nextProcUnitId=null;
	this.currentProcUnitNode=null;
	this.currentMaxY=0;
}
$.extend(parseProcChartData.prototype,{
	doParse:function(){//解析入口方法
		var _self=this,procUnitLength=this.procUnits.length;
		//创建流程图中不同节点
		$.each(this.procUnits,function(i,o){
			if(i > 0){
				_self.prevProcUnitId=_self.procUnits[i-1].procUnitId;
			}
			_self.currentProcUnitId=o.procUnitId;
			if(i < procUnitLength-1){
				_self.nextProcUnitId=_self.procUnits[i+1].procUnitId;
			}
			_self.addProcUnit(o);
		});
		//调整节点显示位置
		return this.adjustmentNodeAxis();
	},
	_addNode:function(node){
		this.currentProcUnitNode.nodes.push($.extend(node,{nodeKind:node.nodeKind||NodeKind.RULE}));
	},
	_addLine:function(line){
		this.chartLines.push(line);
	},
	_addNextProcUnitLine:function(_id,_isChoosed){
		this._addLine({id:Public.getUUID(),type:'etb',from:_id,to:this.nextProcUnitId,alt: true,marked:_isChoosed});
	},
	adjustmentNodeAxis:function(){//调整节点显示位置
		//取最大横坐标 中心点坐标
		var _self=this,maxX=0,centerX=0;
		$.each(this.procNodes,function(i,o){
			maxX=Math.max(maxX,o.maxX);
		});
		//中间位置显示
		if(maxX < 4){
			maxX=4;
		}
		centerX=parseFloat((maxX/2).toFixed(1));
		//调整位置
		var _nodeMaxX=0,_nodeMaxY=0,_prevNodeMaxY=0,_subX=0;
		var _length=this.procNodes.length,_yaxis;
		$.each(this.procNodes,function(i,o){
			_prevNodeMaxY+=_nodeMaxY+1;
			_nodeMaxX=o.maxX;
			_nodeMaxY=o.maxY;
			_yaxis=_prevNodeMaxY;
			//最后一个节点调整位置
			if(i<_length-1){
				_yaxis=_yaxis-1;
			}else{
				_yaxis=_yaxis-0.5;
			}
			_self.chartNodes.push({
				id:o.id,
				nodeKind:o.nodeKind,
				name:o.name,
				code:o.code,
				xaxis:centerX,
				yaxis:_yaxis,
				handlers:o.handlers||{}
			});
			if(o.nodes&&o.nodes.length>0){
				//子节点调整量
				_subX=parseFloat(((maxX-_nodeMaxX)/2).toFixed(1));
				$.each(o.nodes,function(x,n){
					_self.chartNodes.push($.extend(n,{xaxis:n.xaxis+_subX,yaxis:n.yaxis+_prevNodeMaxY-1}));
				});
			}else{
				//没有子节点直接连接下一环节
				if( i < _self.procNodes.length-1){
					_self._addLine({id:Public.getUUID(),type: 'tb',from:o.id,to:_self.procNodes[i+1].id,alt: true});
				}
			}
		});
		//线条排序 存在marked==true 后画
		this.chartLines.sort(function(aLine,bLine){
			var a=aLine.marked?10:0,b=bLine.marked?10:0;
            return a-b;
        });
		return {nodes:this.chartNodes,lines:this.chartLines};
	},
	addProcUnit:function(o){//流程中的环节
		var _name=o.procUnitName||'';
		var _nodeKind=NodeKind.PROCUNIT;
		if(o.procUnitId=='StartEvent'||o.procUnitId=='EndEvent'){
			_nodeKind=NodeKind.EVENT;
			_name=o.procUnitId=='StartEvent'?'S':'E';
		}
		var _node={
			id:o.procUnitId,
			nodeKind:_nodeKind,
			name:_name,
			code:o.procUnitId,
			maxX:0,
			maxY:0,
			handlers:o.handlers||{},
			rules:o.rules||[],
			nodes:[]
		};
		this.currentProcUnitNode=_node;
		//解析其中规则树
		this.addRules(_node);
		this.procNodes.push(_node);
	},
	addRules:function(procUnitNode){//规则树
		var rules=procUnitNode.rules,procUnitId=procUnitNode.id;
		if(!rules||rules.length==0){
			return;
		}
		this.x=0;
		this.y=0;
		this.currentMaxY=1;
		var _self=this,ruleId='';
		var _isChoosed=false;
		var _length=rules.length;
		if(_length > 1){
			_self.y=_self.y+1;//新加一行
			$.each(rules,function(r,h){
				ruleId=Public.getUUID();
				if(r > 0){//新加一列
					_self.x=_self.x+1;
				}
				_isChoosed=h.isChoosed;
				_self.y=1;//新的规则都是从1开始
				_self._addLine({id:Public.getUUID(),type: 'tb',from:procUnitId,to:ruleId,alt: true,marked:_isChoosed});
				_self._addNode({
					id:ruleId,
					nodeKind:NodeKind.RULE,
					name:h.name,
					xaxis:_self.x+_self._getChildrenDiff(h.children),
					yaxis:_self.y,
					marked:_isChoosed
				});
				_self.addRuleChildren(ruleId,h.children);//调用处理规则节点
				_self.addHandlers(ruleId,h.handlers,_self.y,_isChoosed);//可能存在处理人
			});
		}else{//只有一个节点不用显示
			_self.addRuleChildren(procUnitId,rules[0].children);//调用处理规则节点
			_self.addHandlers(procUnitId,rules[0].handlers,this.y,rules[0].isChoosed);//可能存在处理人
		}
		procUnitNode.maxX=this.x;
		procUnitNode.maxY=this.currentMaxY;
	},
	addRuleChildren:function(nodeId,children){
		if(!children||children.length==0){
			return;
		}
		var _length=children.length;
		var _self=this,_id='';
		var _isChoosed=false;
		if(_length > 1){
			_self.y=_self.y+1;//新加一行
			$.each(children,function(i,o){
				_id=Public.getUUID();
				if(i > 0){//新加一列
					_self.x=_self.x+1;
				}
				_isChoosed=o.isChoosed;
				_self._addLine({id:Public.getUUID(),type: 'tb',from:nodeId,to:_id,alt: true,marked:_isChoosed});
				_self._addNode({
					id:_id,
					nodeKind:NodeKind.RULE,
					name:o.name,
					xaxis:_self.x+_self._getChildrenDiff(o.children),
					yaxis:_self.y,
					marked:_isChoosed
				});
				_self.addRuleChildren(_id,o.children);//递归调用处理子规则
				_self.addHandlers(_id,o.handlers,_self.y,_isChoosed);//可能存在处理人
			});
		}else{
			//只有一个节点不用显示
			_self.addRuleChildren(nodeId,children[0].children);//调用处理规则节点
			_self.addHandlers(nodeId,children[0].handlers,this.y,children[0].isChoosed);//可能存在处理人
		}
	},
	_getChildrenDiff:function(children){
		if(!children||children.length==0){
			return 0;
		}
		return parseFloat(((children.length-1)/2).toFixed(1));
	},
	addHandlers:function(nodeId,handlers,_y,_isChoosed){//处理人信息
		if(!handlers||handlers.length==0){
			return;
		}
		var _self=this,_id='',_length=handlers.length,_dep=1;
		$.each(handlers,function(i,o){
			_id=Public.getUUID();
			_dep=o.groupHandlers.length;
			_self._addLine({id:Public.getUUID(),type: 'tb',from:nodeId,to:_id,alt: true,marked:_isChoosed});
			_self._addNode({
				id:_id,
				nodeKind:NodeKind.HANDLER,
				name:o.groupId,
				groupHandlers:o.groupHandlers,
				groupHandlerSize:_length,
				marked:_isChoosed,
				xaxis:_self.x,
				yaxis:_y+1//统一规则下handlers中数据按groupId排序都是串行
			});
			_y=_y+1.2;
			//存在多个处理人的计算逻辑为 每多一个坐标位置+0.6
			if(_dep > 1){
				_y=_y+(_dep-1)*0.6;
			}
			if(i==_length-1){//最后一个节点链接下一个环节
				_self._addNextProcUnitLine(_id,_isChoosed);
			}
		});
		this.currentMaxY=Math.max(this.currentMaxY,_y);
	}
});

var NodeKind={
	PROCUNIT:'procUnit',//流程节点
	EVENT:'event',//开始or结束
	RULE:'rule',//规则
	HANDLER:'handler'//处理人
};
(function($) {
	
	$.fn.workFlowChart = function (op){
		var obj=this.data('ui_work_flow_chart');
		if(!obj){
			new JQueryWorkFlowChart(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				var _ms=['reInitSize','initData'];
				$.each(_ms,function(i,m){
					if(op==m){
						args = Array.prototype.slice.call(args, 1);
						value=obj[m].apply(obj,args);
						return false;
					}
				});
				return value;
			}
		}
		return this;
    };

	function JQueryWorkFlowChart(el,op){
		this.element=$(el);
		this.set(op);
		this.init();
		this.element.data('ui_work_flow_chart',this);
		this.proportion=1;
		this.groupHandlers={};
	}
	
	$.extend(JQueryWorkFlowChart.prototype,{
		set:function(op){
			var headBtns=[];
			headBtns.push({code:'magnifier_minus',name:'缩小'});
			headBtns.push({code:'magnifier_add',name:'放大'});
			headBtns.push({code:'magnifier_one',name:'正常大小'});
			headBtns.push({code:'magnifier_whole',name:'全屏显示'});
			headBtns.push({code:'menu',name:'流程轨迹'});
			this.options=$.extend({
				haveHead: true,
				headBtns: headBtns,
				haveTool: false,
				haveGroup: true,
				useOperStack: true,
				haveAxes:true,//是否需要坐标轴
				getAddNodeHtml:false,
				onShowAddNode:false
			},this.options, op||{});
			//单元格长宽
			this.cellWidth=180;
			this.cellHeight=110;
			
			this.procUnitWidth=140;
			this.procUnitHeight=50;
			
			this.eventWidth=32;
			this.eventHeight=32;
			
			this.ruleWidth=120;
			this.ruleHeight=50
			
			this.handlerWidth=120;
			this.handlerTitle=38;
			this.handlerHeight=23;
			
			//默认字体
			this.fontSize=14;
			//规则显示字体
			this.eventFontSize=26;
		},
		init:function(){
			this.flowChart=$.createFlowChart(this.element, this.options);
			//设置属性
			this._setFlowChartPrototype();
			var _self=this;
			//通用按钮事件
			_self.flowChart['onBtnClick']=function(btnId,btn){
				if(!btnId) return;
				//放大or缩小
				if(btnId.startsWith('magnifier_')){
					_self._setProportion(btnId.replaceAll('magnifier_',''));
					return;
				}
				if(btnId=='menu'){
					var bizId=$('#bizId').val();
					 //流程轨迹
				    UICtrl.showFrameDialog({
				        title: 'common.job.approvalhistory',
				        width: getDefaultDialogWidth(),
				        height: getDefaultDialogHeight(),
				        url: web_app.name + '/workflow/showApprovalHistory.do',
				        param: { bizId: bizId },
				        resize: true,
				        ok: false,
				        cancelVal: 'common.button.close',
				        cancel: true
				    });
				}
			};
		},
		bindEvent:function(){
			var _slef=this;
			$('tr.showTip',this.element).each(function(){
				var _this=$(this),_handlerId=_this.data('handlerId');
				var _handlerNode=_slef.groupHandlers[_handlerId],html=[];
				if(_handlerNode){
					html.push('<table class="table-bordered table-fixed">');
					html.push('<tr>','<td class="title" style="width:30%;height:25px;">',$.i18nProp('common.field.handlername'),':</td><td style="width:70%;">');
					html.push(_handlerNode.executorPersonMemberName);
					html.push('</td>','</tr>');
					html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.field.handleddate'),':</td><td >',_handlerNode.endTime,'</td>','</tr>');
					html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.field.handleresult'),':</td><td >');
					if(_handlerNode.statusId){
						switch (_handlerNode.statusId) {
							case TaskStatus.SLEEPING:
							case TaskStatus.RETURNED:
							case TaskStatus.WAITED:
							case TaskStatus.SUSPENDED:
						    case TaskStatus.CANCELED:
						    case TaskStatus.ABORTED:
						    case TaskStatus.DELETED:
						    	html.push(TaskStatus.getDisplay(_handlerNode.statusId));
					        break;
						    case TaskStatus.EXECUTING:
						    	html.push('');
						    break;
						    default:
						    	html.push(HandleResult.getDisplayName(_handlerNode.result));
					    }
					}
					html.push('</td>','</tr>');
					html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.field.opinion'),':</td><td >',_handlerNode.opinion,'</td>','</tr>');
					html.push('</table>');
					//注册提示框
					_this.tooltip({width:260,position:'right',content:function(){return html.join('');}});
				}
			});
		},
		addGroupHandler:function(handler){
			var _id=Public.getUUID();
			this.groupHandlers[_id]=handler;
			return _id;
		},
		_setFlowChartPrototype:function(){//修改流程图对象属性
			var _self=this;
			$.extend(this.flowChart, {
				getNodeHtml:function(id,json){
					var nodeKind=json.nodeKind;
					var html=[]; 
					var mark=json.marked? ' item_mark':'';
					if(nodeKind==NodeKind.PROCUNIT){
						var _textViewStyle=_self._getNameViewStyle('procUnitHeight');
						var _procUnitHeight=_self._getSize('procUnitHeight');
						html.push("<div class='FlowChart_item item_radius",mark,"' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;'>");
						html.push("<table border=0 cellspacing=1 cellpadding=0 class='table_radius background_color_blue' style='width:",(json.width),"px;height:",(json.height),"px;'>");
						html.push("<tr>");
						html.push("<td style='height:",_procUnitHeight,"px;'>");
						html.push("<span class='node_name text_view' style='",_textViewStyle,"' title='",json.name,"'>");
						html.push(json.name);
						html.push("</span>");
						html.push("</td>");
						html.push("</tr>");
						html.push("</table>");
						html.push("</div>");
					}else if(nodeKind==NodeKind.EVENT){
						var _eventHeight=_self._getSize('eventHeight');
						var _textViewStyle=_self._getNameViewStyle('eventHeight','eventFontSize');
						html.push("<div class='FlowChart_item item_round background_color_red",mark,"' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;'>");
						html.push("<table cellspacing='0' style='width:",(json.width),"px;height:",(json.height),"px;'><tr>");
						html.push("<td style='height:",_eventHeight,"px;'>");
						html.push("<span class='text_view rule_kind_view'  style='",_textViewStyle,"'>");
						html.push(json.name);
						html.push("</span>");
						html.push("</td>");
						html.push("</tr></table>");
						html.push("</div>");
					}else if(nodeKind==NodeKind.RULE){
						var _textViewStyle=_self._getNameViewStyle('ruleHeight');
						var _ruleHeight=_self._getSize('ruleHeight');
						html.push("<div class='FlowChart_item item_radius",mark,"' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;'>");
						html.push("<table border=0 cellspacing=1 cellpadding=0 class='table_radius background_color_green' style='width:",(json.width),"px;height:",(json.height),"px;'>");
						html.push("<tr>");
						html.push("<td style='height:",_ruleHeight,"px;'>");
						html.push("<span class='node_name text_view' style='",_textViewStyle,"' title='",json.name,"'>");
						html.push(json.name);
						html.push("</span>");
						html.push("</td>");
						html.push("</tr>");
						html.push("</table>");
						html.push("</div>");
					}else if(nodeKind==NodeKind.HANDLER){
						var _handlerTitleStyle=_self._getNameViewStyle('handlerTitle');
						var _handlerStyle=_self._getNameViewStyle('handlerHeight');
						var _titleHeight=_self._getSize('handlerTitle');
						var _handlerHeight=_self._getSize('handlerHeight');
						html.push("<div class='FlowChart_item item_radius",mark,"' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;'>");
						html.push("<table border=0 cellspacing=1 cellpadding=0  class='table_radius' style='width:",(json.width),"px;height:",(json.height),"px;'>");
						var handlerClassName='',handlerId='';
						$.each(json.groupHandlers,function(i,o){
							handlerClassName='';
							handlerId='';
							if(o.statusId){
								handlerId=_self.addGroupHandler(o);
								switch (o.statusId) {
									case TaskStatus.READY:
									case TaskStatus.EXECUTING:
										handlerClassName = "showTip background-info";
								        break;
									case TaskStatus.COMPLETED:
										handlerClassName = "showTip background-success";
								    	break;
									case TaskStatus.SLEEPING:
									case TaskStatus.RETURNED:
									case TaskStatus.WAITED:
									case TaskStatus.SUSPENDED:
										handlerClassName = "showTip background-warning";
								        break;
								    case TaskStatus.CANCELED:
								    case TaskStatus.ABORTED:
								    case TaskStatus.DELETED:
								    	handlerClassName = "showTip background-danger";
								        break;
								    default:
								    	handlerClassName = "background-default";
							    }
							}
							html.push("<tr class='",handlerClassName,"' data-handler-id='",handlerId,"'>");
							html.push("<td style='height:",_titleHeight,"px;'>");
							html.push("<span class='node_name text_view' style='",_handlerTitleStyle,"'>");
							html.push(o.description||'');
							html.push("</span>");
							html.push("</td>");
							html.push("</tr>");
							html.push("<tr class='",handlerClassName," border_bottom' data-handler-id='",handlerId,"'>");
							html.push("<td style='height:",_handlerHeight,"px;'>");
							html.push("<span class='node_name text_view' style='",_handlerStyle,"'>");
							html.push(o.handlerName||'');
							html.push("</span>");
							html.push("</td>");
							html.push("</tr>");
						});
						html.push("</table>");
						html.push("</div>");
					}
					return html.join('');
				},
				getLineStrokeWidth:function(){
					var strokeWidth=parseFloat(14*_self.proportion,10);
					return strokeWidth/10;
				},
				getLineTopDiff:function(diff){
					return parseFloat(diff*_self.proportion,10);
				},
				getLineTextStyle:function(){
					var _fontSize=_self._getSize('fontSize');
					if (($.browser.chrome||$.browser.safari)&&_fontSize<12) {
						return 'display:none';
					}
					return 'font-size:'+(_self.proportion*100)+'%;';
				},
				sizeAdjustment:function(w,h){
					var _maxAxis=_self._getMaxAxis();
					var maxX=_maxAxis.x,maxY=_maxAxis.y;
					var _cellWidth=_self._getSize('cellWidth');
					var _cellHeight=_self._getSize('cellHeight');
					//最大显示坐标
					var maxWidth=maxX*_cellWidth+_cellWidth;
					var maxHeight=maxY*_cellHeight+_cellHeight;
					return {width:Math.max(w,maxWidth),height:Math.max(h,maxHeight)};
				},
				onReInitSize:function(w,h){
					_self.addCoordinateAxes();
				}
			});
		},
		_setProportion:function(type){
			var _proportion=this.proportion;
			if (typeof (type) == "string"){
				if(type=='minus'){//缩小
					_proportion=_proportion-0.1;
					if(_proportion<0.2){
						_proportion=0.2;
					}
				}else if(type=='add'){//放大
					_proportion=_proportion+0.1;
					if(_proportion>2){
						_proportion=2;
					}
				}else if(type=='one'){//正常大小
					_proportion=1;
				}else if(type=='whole'){//计算全屏显示尺寸
					_proportion=this._computeWholeProportion();
				}
			}else if (typeof (type) == "number"){
				_proportion= type;
			}
			_proportion=parseFloat(_proportion.toFixed(1),10);
			//比例一样不做处理
			if(_proportion==this.proportion){
				return;
			}
			this.proportion=_proportion;
			this.flowChart.$proportion=_proportion;
			this.initData();
			if(type=='whole'){
				this._scrollWorkArea();
			}
		},
		_getMaxAxis:function(){
			var maxX=0,maxY=0;
			//获取最大X,Y坐标
			$.each(this.flowChart.$nodeData,function(k,node){
				var xaxis=parseFloat(node.xaxis),yaxis=parseFloat(node.yaxis);
				if(xaxis>maxX){maxX=xaxis;}
				if(yaxis>maxY){maxY=yaxis;}
	    	});
			return {x:maxX,y:maxY};
		},
		//计算全屏显示比例
		_computeWholeProportion:function(){
			var _maxAxis=this._getMaxAxis();
			var maxX=_maxAxis.x,maxY=_maxAxis.y;
			//最大显示坐标
			var maxWidth=maxX*this.cellWidth+this.cellWidth;
			var maxHeight=maxY*this.cellHeight+this.cellHeight;
			//获取可视区域大小
			var workArea=this.flowChart.$bgDiv.children("div.FlowChart_work");
			var width=$(workArea).width(),height=$(workArea).height();
			//计算比例
			var _proportion=Math.min(width/maxWidth,height/maxHeight);
			//取1位小数，舍弃其他
			_proportion=parseFloat(_proportion*10,10)/10;
			if(_proportion>1){
				return 1;
			}
			return _proportion;
		},
		_scrollWorkArea:function(){
			var _workArea= this.flowChart.$workArea.parent();
			$(_workArea).scrollLeft(0);
			$(_workArea).scrollTop(0);
		},
		_getSize:function(name){
			return parseFloat(this[name]*this.proportion,10);
		}, 
		_getNameViewStyle:function(heightType,fontSizeType){
			if(!fontSizeType){
				fontSizeType='fontSize';
			}
			var _fontSize=this._getSize(fontSizeType);
			var html=[],fontSizeStyle=this._getFontSizeStyle(fontSizeType);
			html.push(fontSizeStyle);
			if (($.browser.chrome||$.browser.safari)&&_fontSize<12) {
				var width=100*(1/this.proportion);
				html.push('width:',width.toFixed(1),'%;');
			}
			html.push('height:',this._getSize(heightType),'px;');
			return html.join('');
		},
		_getFontSizeStyle:function(type){
			if(!type){
				type='fontSize';
			}
			var _fontSize=this._getSize(type);
			var html=[];
			//解决chrome浏览器是不支持12px以下小字体的问题
			if ($.browser.chrome||$.browser.safari) {
				if(_fontSize<12){
					//字体缩放
					html.push('-webkit-transform:scale(',this.proportion,');');
					//解决缩放后margin的位移问题
					html.push('-webkit-transform-origin: 0;');
					//让opera的不缩放
					html.push('-o-transform: scale(1);');
				}else{
					html.push('font-size:',(this.proportion*100),'%;');
				}
			}else{
				html.push('font-size:',(this.proportion*100),'%;');
			}
			return html.join('');
		},
		position:function(op){
			var xaxis=parseFloat(op.xaxis,10),yaxis=parseFloat(op.yaxis,10);
			var _nodeKind=op.nodeKind;
			var diffx=(this._getSize('cellWidth')-this._getSize(_nodeKind+'Width'))/2;
			var diffy=(this._getSize('cellHeight')-this._getSize(_nodeKind+'Height'))/2;
			var X=xaxis*this._getSize('cellWidth')+diffx;
			var Y=yaxis*this._getSize('cellHeight')+diffy;
			return {left:X,top:Y,width:this._getSize(_nodeKind+'Width'),height:this._getSize(_nodeKind+'Height')};
		},
		reInitworkAreaSize:function(){
			this.flowChart.reInitworkAreaSize();
		},
		//添加坐标系统
		addCoordinateAxes:function(){
			var workArea=this.flowChart.$workArea;
			if(!this.options.haveAxes){
				workArea.css({marginTop:0,marginLeft:0});
				return;
			}
			var w=$(workArea).width(),h=$(workArea).height();
			//坐标轴 x,y
			this.$X=$('div.FlowChart_X_axes',this.flowChart.$bgDiv);
			this.$Y=$('div.FlowChart_Y_axes',this.flowChart.$bgDiv);
			//坐标定位层x,y
			this.$xFlex=$('div.FlowChart_axes_flex_x',this.flowChart.$bgDiv);
			this.$yFlex=$('div.FlowChart_axes_flex_y',this.flowChart.$bgDiv);
			if(this.$X.length==0){
				this.$xFlex=$('<div class="FlowChart_axes_flex_x"></div>').insertAfter(workArea);
				this.$yFlex=$('<div class="FlowChart_axes_flex_y"></div>').insertAfter(workArea);
				$('<div class="FlowChart_empty_axes"></div>').insertAfter(workArea);
				this.$X=$('<div class="FlowChart_X_axes"></div>').insertAfter(workArea);
				this.$Y=$('<div class="FlowChart_Y_axes"></div>').insertAfter(workArea);
				$(workArea).parent().on('scroll',{inthis:this},function(e){
					var This=e.data.inthis;
					This.$X.css({top:$(this).scrollTop()});
					This.$Y.css({left:$(this).scrollLeft()})
				});
				workArea.css({marginTop:20,marginLeft:20});
			}
			this.$xFlex.width(w+20);
			this.$yFlex.height(h+20);
			this.$X.empty().width(w); 
			this.$Y.empty().height(h);
			//设置坐标轴显示刻度
			var _cellWidth=this._getSize('cellWidth'),_cellHeight=this._getSize('cellHeight');
			var countX=parseFloat(w/_cellWidth)+1;
			var countY=parseFloat(h/_cellHeight)+1;
			var htmlX=['<ul style="width:',w+_cellWidth,'px;">'],htmlY=['<ul style="height:',h+_cellHeight,'px;">'];
			for(var i=0;i<countX;i++){
				htmlX.push('<li style="width:',_cellWidth,'px">',i,'</li>');
			}
			for(var i=0;i<countY;i++){
				htmlY.push('<li style="height:',_cellHeight,'px;line-height:',_cellHeight,'px;">',i,'</li>');
			}
			htmlX.push('</ul>');
			htmlY.push('</ul>');
			this.$X.html(htmlX.join(''));
			this.$Y.html(htmlY.join(''));
		},
		//显示坐标定位
		_showCoordinateAxesFlex:function(nodeId){
			var node=this.getNodeById(nodeId);
			if(!node) return;
			var xaxis=node.xaxis,yaxis=node.yaxis;
			var _cellWidth=this._getSize('cellWidth'),_cellHeight=this._getSize('cellHeight');
			var _left=parseFloat(xaxis)*_cellWidth,_top=parseFloat(yaxis)*_cellHeight;
			this.$xFlex.css({top:_top+22,height:_cellHeight}).show();
			this.$yFlex.css({left:_left+22,width:_cellWidth}).show();
		},
		_hideCoordinateAxesFlex:function(){
			this.$xFlex.hide();
			this.$yFlex.hide();
		},
	    reInitSize:function (width,height){
	    	this.flowChart.reInitSize(width,height);
	    },
	    _getLineMValue:function(n1,n2,mType){
	    	if(mType=='sl'){
	    		return null;
	    	}
	    	if(mType=="lr"){
				return (n1.left+n1.width/2+n2.left+n2.width/2)/2;
			}else if(mType=="tb"){
				return (n1.top+n1.height/2+n2.top+n2.height/2)/2;
			}else if(mType=="etb"){
				return (n2.top-this._getSize('cellHeight')+n2.height/2+n2.top+n2.height/2)/2;
			}
	    	return null;
	    },
	    initData:function(data){
	    	var _self=this;
	    	if(data){
	    		this._datas=data;
	    	}else{
	    		data=this._datas;
	    	}
	    	var jsondata = {nodes:{},lines:{},areas:{}};
	    	var shadowNodeQuoteIds=[];
			$.each(data.nodes,function(i,o){
				var xaxis=o.xaxis,yaxis=o.yaxis;
				var id=o.id,position=_self.position(o);
				var node=$.extend(o,{
					left:position.left,
					top:position.top,
					width:position.width,
					height:position.height
				});
				jsondata.nodes[id]=node;
			});
			$.each(data.lines,function(i,o){
				var _from= o.fromNode||o.from,_to=o.toNode||o.to,_type=o.lineType||o.type;
				var m=_self._getLineMValue(jsondata.nodes[_from],jsondata.nodes[_to],_type);
				_type=_type=='etb'?'tb':_type;
				var line={id: o.id,type: _type,from: _from,to: _to,name: o.name||'',M:m,marked:o.marked};
				jsondata.lines[o.id]=line;
			});
			this.loadData(jsondata);
			this.bindEvent();
			//调整显示大小
			this.reInitworkAreaSize();
	    },
	    loadData:function(data){
	    	this.flowChart.clearData();
	    	this.flowChart.loadData(data);
	    	//手机定位到开始节点
	    	if($.isMobile()){
	    		this.positionNode('StartEvent');
	    	}
	    },
	    positionNode:function(nodeId){
	    	var nodeDom=this.flowChart.$nodeDom[nodeId];
	    	if(!nodeDom){
	    		return;
	    	}
			//定位节点
	    	var position=$(nodeDom).position();
	    	var _workArea= this.flowChart.$workArea.parent();
			$(_workArea).scrollLeft(position.left-this._getSize('cellHeight'));
			$(_workArea).scrollTop(position.top-this._getSize('cellHeight'));
	    }
	});
})(jQuery);