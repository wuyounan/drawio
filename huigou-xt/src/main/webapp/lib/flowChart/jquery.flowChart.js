//节点类型
var NodeKind={
	ACTIVITY:'activity',
	RULE:'rule',
	INTERFACE:'interface',
	SHADOW:'shadow'
};
var NodeKindInfo={
	'activity':{name:'活动',sequence:1,color:'background_color_red'},
	'rule':{name:'规则',sequence:2,color:'background_color_gray'},
	'interface':{name:'接口',sequence:3,color:'background_color_blue'},
	'shadow':{name:'影子',sequence:4,color:'background_color_green'}
};
//规则类型
var RuleKind={
	OR:'V',
	XOR:'X',
	AND:'A'
};
var RuleKindName={
	OR:'或',
	XOR:'异或',
	AND:'与'
};

//连线类别
var LineTypes={
	sl:'直线',
	olr:'水平折线',
	otb:'垂直折线',
	lr:'左右折线',
	tb:'上下折线'
};

(function($) {
	
	$.fn.flowChart = function (op){
		var obj=this.data('ui_flow_chart');
		if(!obj){
			new JQueryFlowChart(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				var _ms=['addNode','moveNode','updateNode','checkNode','reInitSize','initData'];
				_ms.push('showAddNodeDialog','exportData','getShadowNodeInfo','getShadowNodeDataSource','getNodeById','fixedNode');
				_ms.push('getIsModified','setIsModified','setDirection','getDirection','fixedAndFocusNode','positionNode');
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

	function JQueryFlowChart(el,op){
		this.element=$(el);
		this.set(op);
		this.init();
		this.bindEvent();
		this.addContextMenu();
		this.direction='Y';//扩展放方向默认 纵向
		this.element.data('ui_flow_chart',this);
		this.proportion=1;
		this._isModified=false;
	}
	
	$.extend(JQueryFlowChart.prototype,{
		set:function(op){
			this.options=$.extend({
				haveHead: true,
				headBtns: headBtns,
				haveTool: true, 
				haveGroup: true,
				useOperStack: true,
				haveAxes:true,//是否需要坐标轴
				getAddNodeHtml:false,
				onShowAddNode:false,
				onNodeChange:function(){//节点改变事件
					
				},
				onApplyUpdateNode:function(id){//修改节点申请
					
				},
				onSetActivityRelation:null,//设置关联对象显示
				onNodeSelect:false,//节点点击事件
				onInit:null
			},this.options, op||{});
			//是否只读显示
			this.readonly=this.options.readonly;
			var headBtns=[];
			if(!this.readonly){
				headBtns.push({code:'checkbox',name:'全选'});
				headBtns.push({code:'new',name:'新增节点'});
				headBtns.push({code:'save',name:'保存'});
				headBtns.push({code:'delete',name:'删除选中节点'});
			}
			headBtns.push({code:'magnifier_minus',name:'缩小'});
			headBtns.push({code:'magnifier_add',name:'放大'});
			headBtns.push({code:'magnifier_one',name:'正常大小'});
			headBtns.push({code:'magnifier_whole',name:'全屏显示'});
			if(!this.readonly){
				headBtns.push({code:'move_up',name:'上移'});
				headBtns.push({code:'move_down',name:'下移'});
				headBtns.push({code:'move_left',name:'左移'});
				headBtns.push({code:'move_right',name:'右移'});
				headBtns.push({code:'undo',name:'撤销'});
				headBtns.push({code:'redo',name:'重做'});
			}
			this.options.headBtns=headBtns;
			//单元格长宽
			this.cellWidth=220;
			this.cellHeight=110;
			//活动节点长宽
			this.nodeWidth=175;
			this.nodeHeight=70;
			//规则长宽
			this.ruleWidth=32;
			this.ruleHeight=32;
			//接口长宽
			this.interfaceWidth=120;
			this.interfaceHeight=40;
			//影子长宽
			this.shadowWidth=60;
			this.shadowHeight=40;
			//规则name显示宽度
			this.labelWidth=100;
			//活动上半部分显示宽度
			this.upHeight=26;
			//活动下半部分显示宽度
			this.downHeight=44;
			//默认字体
			this.fontSize=14;
			this.iconSize=12;
			//规则显示字体
			this.ruleFontSize=26;
		},
		getIsModified:function(){
			return this._isModified;
		},
		setIsModified:function(isModified){
			this._isModified=isModified;
		},
		getDirection:function(){
			return this.direction;
		},
		setDirection:function(direction){
			this.direction=direction;
		},
		init:function(){
			this.flowChart=$.createFlowChart(this.element, this.options);
			//设置属性
			this._setFlowChartPrototype();
			this.setOperStack();
			//注册按钮事件
			var _self=this;
			var reg = new RegExp("(^)on\\s*", "i");
			$.each(this.options,function(key,o){
				if (reg.test(key)){
					_self.flowChart[key]=o;
				}
			});
			//通用按钮事件
			_self.flowChart['onBtnClick']=function(btnId,btn){
				if(!btnId) return;
				//放大or缩小
				if(btnId.startsWith('magnifier_')){
					_self._setProportion(btnId.replaceAll('magnifier_',''));
					$('i.ico_checkbox',$(btn).parent().parent()).removeClass('checkedbox');
					return;
				}
				//移动
				if(btnId.startsWith('move_')){ 
					_self._moveSelectedNode(btnId.replaceAll('move_',''));
					return;
				}
				//删除选中
				if(btnId=='delete'){
					_self._deleteSelectedNode();
					return;
				}
				//全选or全取消
				if(btnId=='checkbox'){
					_self._selectAllNodes($(btn));
					return;
				}
				if(btnId=='undo'){
					_self.undo();
					return;
				}
				if(btnId=='redo'){
					_self.redo();
					return;
				}
			};
		},
		//修改流程图对象属性
		_setFlowChartPrototype:function(){
			var _self=this;
			$.extend(this.flowChart, {
				getLineStrokeWidth:function(){
					var strokeWidth=parseInt(14*_self.proportion,10);
					return strokeWidth/10;
				},
				getNodeHtml:function(id,json){
					var objectKindCode=json.objectKindCode,rk=json.ruleKind;
					var html=[]; 
					if(objectKindCode==NodeKind.ACTIVITY){
						var _codeViewStyle=_self._getNameViewStyle('upHeight');
						var _nameViewStyle=_self._getNameViewStyle('downHeight');
						var _fontSizeStyle=_self._getFontSizeStyle();
						var _upHeight=_self._getSize('upHeight'),_downHeight=_self._getSize('nodeHeight')-_upHeight;
						html.push("<div class='FlowChart_item' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;' title='",json.name,"'>");
						html.push("<span class='left_span checkbox'></span>");
						html.push("<table border=0 cellspacing=0 cellpadding=0  style='width:",(json.width),"px;height:",(json.height),"px;'>");
						html.push("<colgroup><col width='25%'><col width='75%'></colgroup>"); 
						html.push("<tr>");
						html.push("<td class='code_color_td item_color_red'>");
						html.push("<span class='text_view code_view' style='text-align:center;",_codeViewStyle,"'>");
						html.push(json.code||'');
						html.push("</span>");
						html.push("</td>");
						html.push("<td class='item_color_gray node_relation' style='height:",_upHeight,"px;text-align:left;'>");
						//设置图标
						html.push(_self.setActivityRelation(json));
						html.push("</td>");
						html.push("</tr>");
						html.push("<tr>");
						html.push("<td class='item_color_yellow' style='height:",_downHeight,"px;'>");
						html.push("<span class='text_view owner_view'  style='",_nameViewStyle,"'>");
						html.push(json.ownerName);
						html.push("</span>");
						html.push("</td>");
						html.push("<td style='height:",_downHeight,"px;'>");
						html.push("<span class='node_name text_view' style='",_nameViewStyle,"'>");
						html.push(json.name);
						html.push("</span>");
						html.push("</td></tr>");
						html.push("</table>");
						html.push("</div>");
					}else if(objectKindCode==NodeKind.RULE){
						var _ruleHeight=_self._getSize('ruleHeight'),_labelWidth=_self._getSize('labelWidth');
						var _textViewStyle=_self._getNameViewStyle('ruleHeight','ruleFontSize');
						var _nameViewStyle=_self._getNameViewStyle('ruleHeight','fontSize');
						html.push("<div class='FlowChart_item item_round' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;'>");
						html.push("<span class='left_span checkbox'></span>");
						html.push("<table cellspacing='0' style='width:",(json.width),"px;height:",(json.height),"px;'><tr>");
						html.push("<td style='height:",_ruleHeight,"px;'>");
						html.push("<span class='text_view rule_kind_view'  style='",_textViewStyle,"'>");
						html.push(RuleKind[rk]);
						html.push("</span>");
						html.push("</td>");
						html.push("</tr></table>");
						html.push("<div class='span' style='top:1px;left:-",(_labelWidth+2),"px;width:",_labelWidth,"px;'>");
						html.push("<span class='text_view node_name'  style='text-align:right;",_nameViewStyle,"'>");
						html.push("[",json.code,"]");
						html.push(json.name);
						html.push("</span>");
						html.push("</div></div>");
					}else if(objectKindCode==NodeKind.INTERFACE){
						var _nameViewStyle=_self._getNameViewStyle('interfaceHeight');
						html.push("<div class='FlowChart_item' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;' title='",json.name,"'>");
						html.push("<span class='left_span checkbox'></span>");
						html.push("<table border=0 cellspacing=0 cellpadding=0  style='width:",(json.width),"px;height:",(json.height),"px;'>");
						html.push("<colgroup><col width='25%'><col width='75%'></colgroup>"); 
						html.push("<tr>");
						html.push("<td class='item_color_blue' style='height:",json.height,"px;'>"); 
						html.push("<span class='text_view code_view'  style='",_nameViewStyle,"'>");
						html.push(json.code);
						html.push("</span>");
						html.push("</td>");
						html.push("<td class='item_color_gray' style='height:",json.height,"px;'>");
						html.push("<span class='node_name text_view' style='",_nameViewStyle,"'>");
						html.push(json.name);
						html.push("</span>");
						html.push("</td></tr>");
						html.push("</table>");
						html.push("</div>");
					}else if(objectKindCode==NodeKind.SHADOW){
						var _nameViewStyle=_self._getNameViewStyle('shadowHeight');
						var _quoteNode=_self.getShadowNodeInfo(json),_name='';
						if(_quoteNode){
							_name=_quoteNode.code;
						}
						html.push("<div class='FlowChart_item' id='",id,"' style='top:",json.top,"px;left:",json.left,"px;'>");
						html.push("<span class='left_span checkbox'></span>");
						html.push("<table border=0 cellspacing=0 cellpadding=0  style='width:",(json.width),"px;height:",(json.height),"px;'>");
						html.push("<colgroup><col width='35%'><col width='65%'></colgroup>");
						html.push("<tr>");
						html.push("<td class='item_color_bright_green' style='height:",json.height,"px;'>");  
						html.push("<span class='text_view code_view'  style='",_nameViewStyle,"'>");
						html.push(json.code);
						html.push("</span>");
						html.push("</td>");
						html.push("<td class='item_color_green' style='height:",json.height,"px;'>");
						html.push("<span class='node_name text_view' style='",_nameViewStyle,"'>");
						html.push(_name);
						html.push("</span>");
						html.push("</td></tr>");
						html.push("</table>");
						html.push("</div>");
					}
					return html.join('');
				},
				getLineTopDiff:function(diff){
					return parseInt(diff*_self.proportion,10);
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
		//设置使用堆栈记录操作并提供“撤销/重做”的功能
		setOperStack:function(){
			if(!this.options.useOperStack) return;
			this.$undoStack=[];
			this.$redoStack=[];
			this.$isUndo=0;
		},
		//undo/redo中的操作缓存栈,最多只可放40步操作;超过40步时,将自动删掉最旧的一个缓存
		pushUndoOper:function(fn){
			if(!this.options.useOperStack) return;
			this.$undoStack.push(fn);
			if(this.$undoStack.length>40){
				this.$undoStack.shift();
			}
			if(this.$isUndo==0){
				this.$redoStack.splice(0,this.$redoStack.length);
			}
			this.$isUndo=0;
		},
		pushRedoOper:function(fn){
			if(!this.options.useOperStack) return;
			this.$redoStack.push(fn);
			this.$isUndo=false;
			if(this.$redoStack.length>40){
				this.$redoStack.shift();
			}
		},
		undo:function(){
			if(this.$undoStack.length==0)	return;
			var tmp=this.$undoStack.pop();
			this.$isUndo=1;
			if($.isFunction(tmp)){
				tmp.call(this);
			}
		},
		redo:function(){
			if(this.$redoStack.length==0)	return;
			var tmp=this.$redoStack.pop();
			this.$isUndo=2;
			if($.isFunction(tmp)){
				tmp.call(this);
			}
		},
		//设置活动关联图标
		setActivityRelation:function(node){
			var objectKindCode=node.objectKindCode;
			if(objectKindCode!=NodeKind.ACTIVITY){
				return '';
			}
			if($.isFunction(this.options.onSetActivityRelation)){
				return this.options.onSetActivityRelation.call(this,node);
			}
			var linkKindCodes=node.linkKindCodes||'';
			if(Public.isBlank(linkKindCodes)){
				return '';
			}
			var html=[];
			var _fontSizeStyle=this._getFontSizeStyle();
			var _upHeight=this._getSize('upHeight');
			$.each(linkKindCodes.split(','),function(i,code){
				html.push("<span class='relation' style='width:",_upHeight,"px;height:",_upHeight,"px;'>");
				html.push("<span class='rel-",code,"' style='",_fontSizeStyle,"'></span>");
				html.push("</span>");
			});
			return html.join('');
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
			//重构试图
			var datas=this.flowChart.exportData();
			var nodes=[],lines=[],areas=[];
			$.each(datas.nodes,function(id,node){
				node['id']=id;
				nodes.push(node);
			});
			$.each(datas.lines,function(id,line){
				line['id']=id;
				lines.push(line);
			});
			$.each(datas.areas,function(id,area){
				area['id']=id;
				areas.push(area);
			});
			this.initData({nodes:nodes,lines:lines,areas:areas});
			if(type=='whole'){
				this._scrollWorkArea();
			}
		},
		_getMaxAxis:function(){
			var maxX=0,maxY=0;
			//获取最大X,Y坐标
			$.each(this.flowChart.$nodeData,function(k,node){
				var xaxis=parseInt(node.xaxis),yaxis=parseInt(node.yaxis);
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
			_proportion=parseInt(_proportion*10,10)/10;
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
			return parseInt(this[name]*this.proportion,10);
		},
		_getSizeByValue:function(value){
			var _value=Public.toNumber(value);
			return parseInt(_value*this.proportion,10);
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
			var xaxis=parseInt(op.xaxis,10),yaxis=parseInt(op.yaxis,10);
			if(op.objectKindCode==NodeKind.ACTIVITY){
				var diffx=(this._getSize('cellWidth')-this._getSize('nodeWidth'))/2;
				var diffy=(this._getSize('cellHeight')-this._getSize('nodeHeight'))/2;
				var X=xaxis*this._getSize('cellWidth')+diffx;
				var Y=yaxis*this._getSize('cellHeight')+diffy;
				return {left:X,top:Y,width:this._getSize('nodeWidth'),height:this._getSize('nodeHeight')};
			}else if(op.objectKindCode==NodeKind.RULE){
				var diffx=(this._getSize('cellWidth')-this._getSize('ruleWidth'))/2;
				var diffy=(this._getSize('cellHeight')-this._getSize('ruleHeight'))/2;
				var X=xaxis*this._getSize('cellWidth')+diffx;
				var Y=yaxis*this._getSize('cellHeight')+diffy;
				return {left:X,top:Y,width:this._getSize('ruleWidth'),height:this._getSize('ruleHeight')};
			}else if(op.objectKindCode==NodeKind.INTERFACE){
				var diffx=(this._getSize('cellWidth')-this._getSize('interfaceWidth'))/2;
				var diffy=(this._getSize('cellHeight')-this._getSize('interfaceHeight'))/2;
				var X=xaxis*this._getSize('cellWidth')+diffx;
				var Y=yaxis*this._getSize('cellHeight')+diffy;
				return {left:X,top:Y,width:this._getSize('interfaceWidth'),height:this._getSize('interfaceHeight')};
			}else if(op.objectKindCode==NodeKind.SHADOW){
				var diffx=(this._getSize('cellWidth')-this._getSize('shadowWidth'))/2;
				var diffy=(this._getSize('cellHeight')-this._getSize('shadowHeight'))/2;
				var X=xaxis*this._getSize('cellWidth')+diffx;
				var Y=yaxis*this._getSize('cellHeight')+diffy;
				return {left:X,top:Y,width:this._getSize('shadowWidth'),height:this._getSize('shadowHeight')};
			}
		},
		positionAreaData:function(area){
			if(!area['oldLeft']){
				area['oldLeft']=area.left;
				area['oldTop']=area.top;
				area['oldWidth']=area.width;
				area['oldHeight']=area.height;
			}
            var left=this._getSizeByValue(area.oldLeft),
                top=this._getSizeByValue(area.oldTop),
                width=this._getSizeByValue(area.oldWidth),
                height=this._getSizeByValue(area.oldHeight);
			return $.extend({},area,{
				left:left,
				top:top,
				width:width,
				height:height
			});
		},
		//中心点坐标
		getNodeCentralPoint:function(node){
			var xaxis=node.xaxis,yaxis=node.yaxis;
			var X=parseInt(xaxis)*this._getSize('cellWidth')+this._getSize('cellWidth')/2;
			var Y=parseInt(yaxis)*this._getSize('cellHeight')+this._getSize('cellHeight')/2;
			return {x:X,y:Y};
		},
		//判断是否是被引用的节点
		_isCitedNode:function(id){
			var flag=false;
			$.each(this.flowChart.$nodeData,function(k,node){
				if(node.objectKindCode==NodeKind.SHADOW){
					if(node.quoteId==id){
						flag=true;
						return false;
					}
				}
			});
			return flag;
		},
		//被引用节点颜色改变
		_changeCitedNodeColor:function(id){
			if(!this.flowChart.$nodeData[id]) return;
			var dom=this.flowChart.$nodeDom[id];
			$('td.code_color_td',dom).removeClass('item_color_red').addClass('item_color_green');
		},
		_cleanCitedNodeColor:function(id){
			if(id=='') return;
			if(!this.flowChart.$nodeData[id]) return;
			if(this._isCitedNode(id)) return;
			var dom=this.flowChart.$nodeDom[id];
			$('td.code_color_td',dom).removeClass('item_color_green').addClass('item_color_red');
		},
		//获取影子节点引用的节点
		getShadowNodeInfo:function(node){
			if(node.objectKindCode==NodeKind.SHADOW){
				var quoteId=node.quoteId;
				if (!Public.isBlank(quoteId)) {
					var quoteNode=this.getNodeById(quoteId);
					if(quoteNode){
						return quoteNode;
					}
				}
			}
			return null;
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
			var countX=parseInt(w/_cellWidth)+1;
			var countY=parseInt(h/_cellHeight)+1;
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
			var _left=parseInt(xaxis)*_cellWidth,_top=parseInt(yaxis)*_cellHeight;
			this.$xFlex.css({top:_top+22,height:_cellHeight}).show();
			this.$yFlex.css({left:_left+22,width:_cellWidth}).show();
		},
		_hideCoordinateAxesFlex:function(){
			this.$xFlex.hide();
			this.$yFlex.hide();
		},
		_getUUID:function(){
			var s = [];
			var hexDigits = "0123456789abcdef";
			for (var i = 0; i < 36; i++) {
			   s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
			}
			s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
			s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
			s[8] = s[13] = s[18] = s[23] = "-";
			var uuid = s.join("");
			uuid=uuid.toUpperCase().replace(/-/g,'');    
			return uuid;
		},
		exportData:function(){
			var datas= this.flowChart.exportData();
			//节点
			var nodes=datas['nodes'];
			if(nodes&&!$.isEmptyObject(nodes)){
				var _nodes=[];
				$.each(nodes,function(id,node){
					var _node=$.extend({},node,{viewId:id,id:''});
					_nodes.push(_node);
				});
				datas['nodes']=_nodes;
			}else{
				datas['nodes']=[];
			}
			//连线
			var lines=datas['lines'];
			if(lines&&!$.isEmptyObject(lines)){
				var _lines=[];
				$.each(lines,function(id,line){
					var _line=$.extend({},line,{
						lineType: line.type,
						fromNode: line.from,
						toNode: line.to,
					});
					_lines.push(_line);
				});
				datas['lines']=_lines;
			}else{
				datas['lines']=[];
			}
			//区域
			var areas=datas['areas'];
			if(areas&&!$.isEmptyObject(areas)){
				var _areas=[];
				$.each(areas,function(id,area){
					var _area=$.extend({},area,{id:''});
					_areas.push(_area);
				});
				datas['areas']=_areas;
			}else{
				datas['areas']=[];
			}
			return datas;
		},
		checkNode:function(node){
			//校验是否允许创建节点
			var flag=true,id=node.id;
			//位置校验
			var xaxis=parseInt(node.xaxis),yaxis=parseInt(node.yaxis);
			var nodeIdByAxis=this.getNodeIdByAxis(xaxis,yaxis);
			if(nodeIdByAxis==id){
				nodeIdByAxis=null;
			}
			if(nodeIdByAxis){
				Public.errorTip('该坐标已存在节点,请确认!');
				flag=false;
				return false;
			}
			//编号重复校验
			$.each(this.flowChart.$nodeData,function(k,n){
				if(n.id==id){//排除自己
					return true;
				}
	    		if(n['objectKindCode']==node.objectKindCode&&n['code']==node.code){
	    			flag=false;
	    			Public.errorTip('输入节点编号['+node.code+']重复,请确认!');
	    			return false;
	    		}
	    	});
			return flag;
		},
		_addNode:function(node){
			var position=this.position(node);
			node=$.extend(node,{
				left:position.left,
				top:position.top,
				width:position.width,
				height:position.height
			});
			this.flowChart.addNode(node.id,node);
			//如果添加的是影子需要修改 被引用节点颜色
			if(node.objectKindCode==NodeKind.SHADOW){
				this._changeCitedNodeColor(node.quoteId);
			}
		},
		addNode:function(node,previousNode){
			//校验是否允许创建节点
			var flag=this.checkNode(node);
			if(!flag){return false;}
			//校验关联关系
			flag=this._checkAssociation(node,previousNode,true);
			if(!flag){return false;}
			this._addNode(node);
			//调用节点改变事件
			if($.isFunction(this.options.onNodeChange)){
				this.options.onNodeChange.call(this);
			}
			this.setIsModified(true);
			this.reInitworkAreaSize();
			return true;
		},
		//校验两个节点是否允许关联 isPrompt是否需要提示信息
		_checkAssociation:function(node,previousNode,isPrompt){
			if(!previousNode){
				return true;
			}
			var nodeKind=node.objectKindCode;
			var previousNodeKind=previousNode.objectKindCode;
			
			if(previousNodeKind==NodeKind.SHADOW){
				if(isPrompt){
					Public.errorTip('影子['+previousNode.code+']不能添加下一步!');
				}
				return false;
			}
			if(previousNodeKind==NodeKind.INTERFACE){
				if(previousNode.interfaceKind=='output'){
					if(isPrompt){
						Public.errorTip('输出接口['+previousNode.code+'.'+previousNode.name+']不能添加下一步!');
					}
					return false;
				}
				//输入节点只能有一个下一步节点
				if(this._hasNextStep(previousNode.id)){
					if(isPrompt){
						Public.errorTip('输人接口['+previousNode.code+'.'+previousNode.name+']已存在下一步!');
					}
					return false;
				}
			}
			if(previousNodeKind==NodeKind.ACTIVITY){
				//活动只能有一个下一步节点
				if(this._hasNextStep(previousNode.id)){
					if(isPrompt){
						Public.errorTip('活动['+previousNode.code+'.'+previousNode.name+']已存在下一步!');
					}
					return false;
				}
			}
			
			if(nodeKind==NodeKind.SHADOW){
				if(this._hasPreviousStep(node.id)){
					if(isPrompt){
						Public.errorTip('影子['+node.code+']已存在上一步!');
					}
					return false;
				}
			}
			if(nodeKind==NodeKind.INTERFACE){
				if(previousNode.interfaceKind=='input'){
					if(isPrompt){
						Public.errorTip('输入接口['+node.code+'.'+node.name+']不能添加上一步!');
					}
					return false;
				}
				//输出节点只能有一个下上一步节点
				if(this._hasPreviousStep(node.id)){
					if(isPrompt){
						Public.errorTip('输出接口['+node.code+'.'+node.name+']已存在上一步!');
					}
					return false;
				}
			}
			if(nodeKind==NodeKind.ACTIVITY){
				//输入接口连接活动不算接入接出活动
				if(previousNode.interfaceKind=='input'){
					return true;
				}
				//活动只能有一个下一步节点
				if(this._hasPreviousStep(node.id)){
					if(isPrompt){
						Public.errorTip('活动['+node.code+'.'+node.name+']已存在上一步!');
					}
					return false;
				}
			}
			
			return true;
		},
		_checkAssociationById:function(nodeId,previousNodeId,isPrompt){
			var node=this.getNodeById(nodeId);
			var previousNode=this.getNodeById(previousNodeId);
			if(!node||!previousNode){
				Public.errorTip('未找到对应节点信息!');
				return false;
			}
			return this._checkAssociation(node,previousNode,isPrompt);
		},
		delNode:function(nodeId){
			var node=this.getNodeById(nodeId);
			if(!node) return;
    		this.flowChart.delNode(nodeId);
    		//被删除的节点是影子时需要修改样式
    		if(node.objectKindCode==NodeKind.SHADOW){
    			this._cleanCitedNodeColor(node.quoteId);
    		}
    		if($.isFunction(this.options.onNodeChange)){
				this.options.onNodeChange.call(this);
			}
    		this.setIsModified(true);
		},
		_addLine:function(line){
			var name=line.name||'';
			line.name=name;
			this.flowChart.addLine(line.id,line);
		},
		addLine:function(line){
			this._addLine(line);
			this.setIsModified(true);
		},
		delLine:function(lineId){
			this.flowChart.delLine(lineId);
			this.setIsModified(true);
		},
		moveNode:function(node){
			var position=this.position(node);
			this.flowChart.$nodeData[node.id]=$.extend(node,{
				left:position.left,
				top:position.top,
				width:position.width,
				height:position.height
			});
			this.flowChart.moveNode(node.id,position.left,position.top);
			this.setIsModified(true);
	    },
	    updateNode:function(json){
	    	var _self=this;
	    	var _redoOper=function(n){
	     		return function(){
	     			_self._updateNode(n);
	     			_self.pushUndoOper(_undoOper);
	     		};
	 		}(json);
	 		var _undoOper=function(nId){
	 			var n=_self.getNodeById(nId);
	 			//linkKindCodes 字段不撤销
	 			n['linkKindCodes']=json['linkKindCodes'];
	 			return function(){
	 				_self._updateNode(n);
		 			_self.pushRedoOper(_redoOper);
	 			};
	         }(json.id);
	         _redoOper();
	    },
	    _updateNode:function(json){
	    	var id=json.id,x=json.xaxis,y=json.yaxis;
	    	var n=this.getNodeById(id);
	    	if(!n){
	    		return;
	    	}
	    	var oldx=parseInt(n.xaxis),oldy=parseInt(n.yaxis);
	    	var oldQuoteId=n.quoteId||'';
	    	var nodeDemo=this.flowChart.$nodeDom[id];
	    	var node=$.extend({},n,json);
	    	var objectKindCode=node.objectKindCode;
	    	if(objectKindCode==NodeKind.ACTIVITY){
				$('span.code_view',nodeDemo).text(node.code);
				$('span.owner_view',nodeDemo).text(node.ownerName);
				$('span.node_name',nodeDemo).text(node.name);
				//修改图标
				$('td.node_relation',nodeDemo).html(this.setActivityRelation(node));
			}else if(objectKindCode==NodeKind.RULE){
				var rk=node.ruleKind;
				var ruleKind=RuleKind[rk];
				$('span.rule_kind_view',nodeDemo).text(ruleKind);
				$('span.node_name',nodeDemo).text('['+node.code+']'+node.name);
			}else if(objectKindCode==NodeKind.INTERFACE){
				$('span.code_view',nodeDemo).text(node.code);
				$('span.node_name',nodeDemo).text(node.name);
			}else if(objectKindCode==NodeKind.SHADOW){
				var newQuoteId=node.quoteId;
				if(oldQuoteId!=newQuoteId){
					var _quoteNode=this.getShadowNodeInfo(node),_name='';
					if(_quoteNode){
						_name=_quoteNode.code;
					}
					$('span.node_name',nodeDemo).text(_name);
					this._changeCitedNodeColor(newQuoteId);
				}
				$('span.code_view',nodeDemo).text(node.code);
			}
	    	//位置发生改变
	    	if(x!=oldx||y!=oldy){
	    		this.moveNode(node);
	    	}else{
	    		this.flowChart.$nodeData[node.id]=node;
	    		this._cleanCitedNodeColor(oldQuoteId);
	    	}
	    	if($.isFunction(this.options.onNodeChange)){
				this.options.onNodeChange.call(this);
			}
	    },
	    reInitSize:function (width,height){
	    	this.flowChart.reInitSize(width,height);
	    },
	    initData:function(data){
	    	var _self=this;
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
				if(node.objectKindCode==NodeKind.SHADOW){
					shadowNodeQuoteIds.push(node.quoteId);
				}
			});
			$.each(data.lines,function(i,o){
				var _from= o.fromNode||o.from,_to=o.toNode||o.to,_type=o.lineType||o.type;
				var m=_self._getLineMValue(jsondata.nodes[_from],jsondata.nodes[_to],_type);
				var line={id: o.id,type: _type,from: _from,to: _to,name: o.name,M:m};
				jsondata.lines[o.id]=line;
			});
			$.each(data.areas,function(i,o){
				jsondata.areas[o.id]=_self.positionAreaData(o);
			});
			this.loadData(jsondata);
			if(shadowNodeQuoteIds.length>0){
				$.each(shadowNodeQuoteIds,function(i,id){
					_self._changeCitedNodeColor(id);
				});
			}
			if($.isFunction(_self.options.onNodeChange)){
				_self.options.onNodeChange.call(_self);
			}
			//调整显示大小
			this.reInitworkAreaSize();
			//初始化数据后执行
			if($.isFunction(this.options.onInit)){
				this.options.onInit.call(this,data);
			}
	    },
	    _getLineMValue:function(n1,n2,mType){
	    	if(mType=='sl'){
	    		return null;
	    	}
	    	return this.flowChart.getMValue(n1,n2,mType);
	    },
	    getNodeById:function(id){
	    	var nodes=this.flowChart.getNodeData();
	    	var node=nodes[id];
	    	return node;
	    },
	    getLineById:function(id){
	    	var lines=this.flowChart.$lineData;
	    	var line=lines[id];
	    	return line;
	    },
	    getNodeIdByAxis:function(x,y){
	    	var nodeId=null;
	    	$.each(this.flowChart.$nodeData,function(k,node){
	    		if(parseInt(node.xaxis)==parseInt(x)&&parseInt(node.yaxis)==parseInt(y)){
	    			nodeId=k;
	    			return false;
	    		}
	    	});
	    	return nodeId;
	    },
	    getLineByFromAndTo:function(from,to){
	    	var lineNode=null;
	    	$.each(this.flowChart.$lineData,function(k,line){
	    		if(line.from==from&&line.to==to){
	    			lineNode=$.extend({},line,{id:k});
	    			return false;
	    		}
	    	});
	    	return lineNode;
	    },
	    _getRelationNodes:function(id){
	    	var relationNodes=[],node=null,_self=this;
	    	$.each(this.flowChart.$lineData,function(k,line){
	    		if(line.from==id){
	    			relationNodes.push(_self.getNodeById(line.to));
	    		}
	    		if(line.to==id){
	    			relationNodes.push(_self.getNodeById(line.from));
	    		}
	    	});
	    	return relationNodes;
	    },
	    //判断是否有下一步
	    _hasNextStep:function(previousId){
	    	var flag=false
	    	$.each(this.flowChart.$lineData,function(k,line){
	    		if(line.from==previousId){
	    			flag=true;
	    			return false;
	    		}
	    	});
	    	return flag;
	    },
	    //获取下一步节点ID
	    _getNextStepId:function(previousId){
	    	var nextStepId=false
	    	$.each(this.flowChart.$lineData,function(k,line){
	    		if(line.from==previousId){
	    			nextStepId=line.to;
	    			return false;
	    		}
	    	});
	    	return nextStepId;
	    },
	    //判断是否有上一步
	    _hasPreviousStep:function(id){
	    	var flag=false
	    	$.each(this.flowChart.$lineData,function(k,line){
	    		if(line.to==id){
	    			flag=true;
	    			return false;
	    		}
	    	});
	    	return flag;
	    },
	    //判断是否是下一步
	    _isNextStep:function(previousId,id){
	    	var line=this.getLineByFromAndTo(previousId,id);
	    	if(line){
	    		return true;
	    	}
	    	return false;
	    },
	    loadData:function(data){
	    	this.flowChart.clearData();
	    	this.flowChart.loadData(data);
	    },
	    bindEvent:function(){
	    	//流程图只读不注册事件
	    	if(this.readonly){
	    		return;
	    	}
	    	var chartObj=this.flowChart,_self=this;
	    	//鼠标拖地选择框
	    	this.element.find('div.FlowChart_work_inner').on('mousedown',function(e){
	    		if(e.button==2){
					return false;
				}
	    		var $clicked = $(e.target || e.srcElement);
	    		//点击复选图标
	    		if($clicked.is('span.checkbox')){
	    			$clicked.toggleClass('checkbox-checked');
	    			$clicked.parent().toggleClass('checked-item');
	    			if($.isFunction(_self.options.onNodeSelect)){
	    				_self.options.onNodeSelect.call(this,$clicked.parent());
	    			}
	    			return $.stopPropagation(e);
	    		}
	    		/*if($clicked.is('div.FlowChart_item')){
	    			if($.isFunction(_self.options.onNodeClick)){
	    				return _self.options.onNodeClick.call(this,$clicked);
	    			}
	    		}*/
	    		if(chartObj.$nowType =='cursor'){
		    		var ev=mousePosition(e),workArea=chartObj.$workArea[0];
		    	    var active_box=$('#active_box');
		    		if(!active_box.length){
		    			active_box=$('<div id="active_box" class="mouse_box"></div>').appendTo(this);
		    		}
		    		active_box.css({left:ev.x+workArea.parentNode.scrollLeft,top:ev.y+workArea.parentNode.scrollTop,width:0,height:0,filter:'alpha(opacity=30)',opacity:0.3});
	    		}
	    		//清空全部选中节点
	    		_self._cleanSelectNodes();
	    	});
	    	//鼠标移动
	    	$(document).on('mousemove',function(e){
	    		var active_box=$('#active_box');
	    		if(active_box.length>0){
	    			var ev=mousePosition(e),offset=active_box.offset();
	    			active_box.css({width:ev.x-offset.left,height:ev.y-offset.top});
	    		}
	    	}).on('mouseup',function(){
	    		var active_box=$('#active_box');
	    		if(active_box.length>0){
	    			//判断包含多少元素对象
	    			var offset=active_box.offset(),workArea=chartObj.$workArea[0];
	    			var t=getElCoordinate(workArea);
	    			var top=offset.top,left=offset.left,width=active_box.width(),height=active_box.height();
	    			left=left-t.left+workArea.parentNode.scrollLeft-1;
	    			top=top-t.top+workArea.parentNode.scrollTop-1; 
	    			$.each(chartObj.getNodeData(),function(id,o){
	    				var position=_self.getNodeCentralPoint(o);
	    				if(position.x>left&&position.x<(left+width)&&position.y>top&&position.y<(top+height)) {
	    					var _item=$('#'+id);
	    					if(_item.hasClass('checked-item')){
	    						_item.removeClass('checked-item');
	    						_item.find('span.checkbox').removeClass('checkbox-checked');
	    					}else{
	    						_item.addClass('checked-item');
	    						_item.find('span.checkbox').addClass('checkbox-checked');
	    					}
	    			    } 
	    			});
	    			active_box.remove();
	    		}
	    	});
	    },
	    getFlowChartNode:function(id){
	    	var nodes=this.flowChart.getNodeData();
	    	return nodes[id];
	    },
	    insertRowOrColumn:function(id,direction){
	    	var _self=this;
	    	var _redoOper=function(_id,_direction){
	    		return function(){
	    			_self._insertRowOrColumn(_id,_direction);
		    		_self.pushUndoOper(_undoOper);
	    		};
			}(id,direction);
			var _undoOper=function(_id,_direction){
				return function(){
					_self._deleteRowOrColumn(_id,_direction);
					_self.pushRedoOper(_redoOper);
				};
            }(id,direction);
            _redoOper();
	    },
	    //插入行或列
	    _insertRowOrColumn:function(id,direction){
	    	var _self=this,type=direction.toLowerCase()+'axis';
	    	var nodes=this.flowChart.getNodeData();
	    	var node=nodes[id];
	    	if(!node){
	    		return;
	    	}
	    	var value=parseInt(node[type]),axis=0;
	    	$.each(nodes,function(id,o){
	    		axis=parseInt(o[type]);
	    		if(axis>value){
	    			o[type]=axis+1;
	    		}
	    		_self.moveNode(o);
	    	});
	    	if($.isFunction(this.options.onNodeChange)){
				this.options.onNodeChange.call(this);
			}
	    },
	    //删除插入的行或列
	    _deleteRowOrColumn:function(id,direction){
	    	var _self=this,type=direction.toLowerCase()+'axis';
	    	var nodes=this.flowChart.getNodeData();
	    	var node=nodes[id];
	    	if(!node){
	    		return;
	    	}
	    	var value=parseInt(node[type]),axis=0;
	    	$.each(nodes,function(id,o){
	    		axis=parseInt(o[type]);
	    		if(axis>value){
	    			o[type]=axis-1;
	    		}
	    		_self.moveNode(o);
	    	});
	    	if($.isFunction(this.options.onNodeChange)){
				this.options.onNodeChange.call(this);
			}
	    },
	    _checkAxis:function(previousId,x,y,direction){
	    	var flag=true,_self=this;
	    	//输入坐标有节点
	    	if(this.getNodeIdByAxis(x,y)){
	    		return false;
	    	}
	    	if(direction!='Z'){
		    	var relationNodes=this._getRelationNodes(previousId);
		    	if(!relationNodes.length) return true;
		    	if(direction=='Y'){//输入坐标为纵向扩展
		    		//找下级坐标判断是否有链接线
			    	$.each(relationNodes,function(i,node){
			    		var xaxis=parseInt(node.xaxis),yaxis=parseInt(node.yaxis);
			    		if(xaxis==x&&yaxis>y){
			    			flag=false;
			    			return false;
			    		}
			    	});
		    	}
		    	if(direction=='X'){//输入坐标为横向扩展
		    		//找下级坐标判断是否有链接线
		    		$.each(relationNodes,function(i,node){
		    			var xaxis=parseInt(node.xaxis),yaxis=parseInt(node.yaxis);
			    		if(yaxis==y&&xaxis>x){
			    			flag=false;
			    			return false;
			    		}
			    	});
		    	}
	    	}
	    	return flag;
	    },
	    //新增时计算坐标
	    _computeAxis:function(id){
	    	var node=this.getNodeById(id);
	    	if(!node) return {x:1,y:1};
	    	var xaxis=parseInt(node.xaxis),yaxis=parseInt(node.yaxis);
	    	var x=xaxis,y=yaxis;
	    	if(this.direction=='Y'){
	    		y++;
	    		while (true){
	    			var tmpNodeId=this.getNodeIdByAxis(x,y);
	    			if(tmpNodeId){
	    				if(!this._isNextStep(id,tmpNodeId)){
		    				break;
		    			}
	    			}else{
	    				if(this._checkAxis(id,x,y,xaxis==x?'Y':'Z')){
	    	    			return {x:x,y:y};
	    	    		}
	    			}
	    			x++;
	    		}
	    		if(this._checkAxis(id,xaxis+1,yaxis,'X')){
	    			return {x:xaxis+1,y:yaxis};
	    		}
	    	}
	    	if(this.direction=='X'){
	    		x++;
	    		while (true){
	    			var tmpNodeId=this.getNodeIdByAxis(x,y);
	    			if(tmpNodeId){
	    				if(!this._isNextStep(id,tmpNodeId)){
		    				break;
		    			}
	    			}else{
	    				if(this._checkAxis(id,x,y,yaxis==y?'X':'Z')){
	    	    			return {x:x,y:y};
	    	    		}
	    			}
	    			y++;
	    		}
	    		if(this._checkAxis(id,xaxis,yaxis+1,'Y')){
	    			return {x:xaxis,y:yaxis+1};
	    		}
	    	}
	    	return {x:'',y:''};
	    },
	    addContextMenu:function(){
	    	//流程图只读不注册事件
	    	if(this.readonly){
	    		return;
	    	}
	    	var chartObj=this.flowChart,_self=this;
	    	var _hideTimer=null,_showTimer=null,_canHide=true;
	    	var _clearTimer=function(){
				if (_showTimer) {
					clearTimeout(_showTimer);
					this.showTimer = null;
				}
				if (_hideTimer) {
					clearTimeout(_hideTimer);
					this.hideTimer = null;
				}
			};
	    	var _hide=function(obj){
	    		if(!_canHide) return;
	    		_clearTimer();
	    		_hideTimer = setTimeout(function() {
	    			chartObj.$workArea.closeMenu();
	    			$('span.checkbox-show',chartObj.$workArea).removeClass('checkbox-show');
	    			//_self._hideCoordinateAxesFlex();
				},300);
	    	};
	    	var _show=function(obj){
	    		_clearTimer();
	    		_showTimer=setTimeout(function(){
	    			chartObj.$workArea.triggerHandler('showMenu.flowChart',{obj:obj});
	    			$('span.checkbox-show',chartObj.$workArea).removeClass('checkbox-show');
	    			$('span.checkbox',obj).addClass('checkbox-show');
	    			//_self._showCoordinateAxesFlex($(obj).attr('id'));
				},200);
	    	};
	    	var _move=function(id,type){
	    		var node=_self.getNodeById(id);
				_self._moveNodes({id:node},type);
	    	};
	    	chartObj.$workArea.delegate("div.FlowChart_item","mouseenter",{inthis:this},function(e){
	    		_show(this);
			});
	    	chartObj.$workArea.delegate("div.FlowChart_item","mouseleave",{inthis:this},function(e){
	    		_hide(this);
			});
	    	chartObj.$workArea.contextMenu({
				width:100,
				eventType:'showMenu.flowChart',
				checkEvent:function(){
					//当流图图处于cursor状态时才能显示
					return chartObj.$nowType =='cursor';
				},
				overflow:function(menu,e){//获取的菜单显示位置函数
					var _item=$(e.data.obj),_offset=_item.offset();
					var t=getElCoordinate($(_self.element)[0]);
					var top=_offset.top-20,left=_offset.left+_item.width()+3;
					if((left+80)>($(_self.element).width()+t.left+$(_self.element).scrollLeft())){
						left=_offset.left-102;
					}
					if((top+70)>($(_self.element).height()+t.top+$(_self.element).scrollTop())){
						top=top-60;
					}
					this.flowChartNodeId=_item.attr('id');
					return {top:top,left:left};
				},
				items:[
					{name:"新  增",icon:'fa-plus-square',classes:'temp',handler:function(){
						_self.showAddNodeDialog(this.flowChartNodeId);
					}},
					{name:"操 作",icon:'fa-podcast',width:100,
						items:[
							{name:"编 辑",icon:'fa-edit',handler:function(){
								if($.isFunction(_self.options.onApplyUpdateNode)){
									_self.options.onApplyUpdateNode.call(window,this.flowChartNodeId);
								}
							}},
							{name:"关  联",icon:'fa-link',handler:function(){
								_self.showNodeRelation(this.flowChartNodeId);
							}},
							{name:"删   除",icon:'fa-trash',handler:function(){
								var nodes={}; 
								nodes[this.flowChartNodeId]=_self.getNodeById(this.flowChartNodeId);
								_self._deleteNodes(nodes);
							}}
						]
					},
					{name:"插 入",icon:'fa-indent',width:100,
						items:[
							{name:"插入行",icon:'fa-list',handler:function(){
								_self.insertRowOrColumn(this.flowChartNodeId,'Y');
							}},
							{name:"插入列",icon:'fa-sort-amount-asc',handler:function(){
								_self.insertRowOrColumn(this.flowChartNodeId,'X');
							}}
						]
					},
					{name:"移 动",icon:'fa-arrows-alt',width:100,
						items:[
							{name:"上 移",icon:'fa-caret-square-o-up',handler:function(){
								_move(this.flowChartNodeId,'up');
							}},
							{name:"下 移",icon:'fa-caret-square-o-down',handler:function(){
								_move(this.flowChartNodeId,'down');
							}},
							{name:"左 移",icon:'fa-caret-square-o-left',handler:function(){
								_move(this.flowChartNodeId,'left');
							}},
							{name:"右 移",icon:'fa-caret-square-o-right',handler:function(){
								_move(this.flowChartNodeId,'right');
							}}
						]
					}
				],
				onOpenMenu:function(m,e){
					if(!m.hasClass('mouseleaveClass')){
						m.addClass('mouseleaveClass').bind('mouseleave',function(){
							_canHide=true;
							_hide();
						}).bind('mouseenter',function(){
							_canHide=false;
							_clearTimer();
						});
					}
					//新增按钮是失效
					var node=_self.getNodeById(this.flowChartNodeId);
					if(!node){
						m.find('li.temp').addClass('disabled');
						return;
					}
					m.find('li.temp').removeClass('disabled');
					if(node.objectKindCode==NodeKind.SHADOW){
						m.find('li.temp').addClass('disabled');
					}
					if(node.objectKindCode==NodeKind.INTERFACE){
						if(node.interfaceKind=='output'){
							m.find('li.temp').addClass('disabled');
						}
					}
				},
				onSelect:function(){
					this._hideMenu();
				}
			});
	    },
	    //获取节点默认编号 传入节点类型
	    _getNodeDefaultCode:function(nodeKind){
	    	var code=0;
	    	$.each(this.flowChart.$nodeData,function(k,n){
	    		if(n['objectKindCode']==nodeKind){
	    			var _code=parseInt(n['code'],10);
	    			if(_code>code){
	    				code=_code;
	    			}
	    		}
	    	});
	    	return code+1;
	    },
	    //获取影子节点数据源
	    getShadowNodeDataSource:function(previousId){
	    	var datas=[],dataSource={},_self=this;
	    	$.each(this.flowChart.$nodeData,function(k,n){
	    		if(n['objectKindCode']==NodeKind.ACTIVITY){
	    			if(previousId){
	    				//排除存在关系的节点
		    			if(!_self._isNextStep(k,previousId)){
		    				datas.push(n);
		    			}
	    			}else{
	    				datas.push(n);
	    			}
	    		}
	    	});
	    	if(!datas.length) return{};
	    	//排序
	    	datas.sort(function(n1,n2){
	    		return parseInt(n1.code) - parseInt(n2.code); 
	    	}); 
	    	$.each(datas,function(i,n){
	    		dataSource[n.id]=n.code+'.'+n.name;
	    	});
	    	return dataSource;
	    },
	    //新增页面节点类型改变
	    _onKindCodeChange:function(kindCode,div){
      		var _code=this._getNodeDefaultCode(kindCode);
    		$('#code',div).val(_code);
    		//根据选中的类型控制页面显示
    		switch(kindCode){
				case NodeKind.RULE:
					$('div.ruleKindDiv',div).show();
					$('div.interfaceKindDiv',div).hide();
					$('div.quoteNameDiv',div).hide();
					$('div.nameViewDiv',div).show();
					$('div.ownerViewDiv',div).hide();
					break;
				case NodeKind.INTERFACE:
					$('div.ruleKindDiv',div).hide();
					$('div.interfaceKindDiv',div).show();
					$('div.quoteNameDiv',div).hide();
					$('div.nameViewDiv',div).show();
					$('div.ownerViewDiv',div).hide();
					break;
				case NodeKind.SHADOW:
					$('div.ruleKindDiv',div).hide();
					$('div.interfaceKindDiv',div).hide();
					$('div.quoteNameDiv',div).show();
					$('div.nameViewDiv',div).hide();
					$('div.ownerViewDiv',div).hide();
					break;
				default :
					$('div.ruleKindDiv',div).hide();
					$('div.interfaceKindDiv',div).hide();
					$('div.quoteNameDiv',div).hide();
					$('div.nameViewDiv',div).show();
					$('div.ownerViewDiv',div).show();
					break;
			}
    		Public.autoElementWrapperHeight(div);
	    },
	    showAddNodeDialog:function(previousId){
	    	var html="",_self=this;
	    	if($.isFunction(this.options.getAddNodeHtml)){
	    		html=this.options.getAddNodeHtml.call(this);
			}else{
				return;
			}
		    var options = {title:'新增节点',content: html,width: 400,opacity: 0.1,top:50,
		        onClick: function ($el,div) {
		        	var elId=$el.attr('id');
		            if(elId=='cancelDialog'){
		            	this.close();
		            }else if(elId=='addYaxis'){
		            	_self.insertRowOrColumn(previousId,'Y');
		            	_computeAxis(previousId,div);
		            }else if(elId=='addXaxis'){
		            	_self.insertRowOrColumn(previousId,'X');
		            	_computeAxis(previousId,div);
		            }else if(elId=='addNode'){
		            	var flag=_self._createNode(div,previousId);
		            	if(flag){
		            		this.close();
		            	}
		            }else if($el.is('input:radio')){
		            	if($el.attr('name')=='objectKindCode'){
		            		var kindCode=$el.val();
		            		_self._onKindCodeChange(kindCode,div);
		            	}
		            }
		        },
		        onClose:function(){
		        	$.closePicker();
		        }
		    };
		    var div=Public.dialog(options);
		    //初始化参数
		    $('input[name="name"]',div).val('');
		    $('input[name="ownerId"]',div).val('');
		    $('input[name="ownerName"]',div).val('');
		    $('input[name="objectKindCode"]:eq(0)',div).attr('checked',true);
		    $('input[name="ruleKind"]:eq(0)',div).attr('checked',true);
		    $('input[name="yesorno"]:eq(1)',div).attr('checked',true);
		    $('input[name="interfaceKind"]:eq(1)',div).attr('checked',true);
		    //注册影子取值
		    $('input[name="quoteId"]',div).combox({data:_self.getShadowNodeDataSource(previousId)});
		    //默认取活动编号
		    var _code=this._getNodeDefaultCode(NodeKind.ACTIVITY);
		    $('input[name="code"]',div).val(_code).spinner({min:1,max:999}).mask('999',{number:true});
		    $('input[name="xaxis"]',div).val(1).spinner({min:1,max:100}).mask('99', {number: true});
		    $('input[name="yaxis"]',div).val(1).spinner({min:1,max:100}).mask('99', {number: true});
		    //计算坐标
		    function _computeAxis(previousId,div){
		    	var axis=_self._computeAxis(previousId);
			    $('input[name="xaxis"]',div).val(axis.x);
			    $('input[name="yaxis"]',div).val(axis.y);
			    if(axis.x==''||axis.y==''){
			    	$('div.prompt',div).html('未找到可添加节点坐标，可插入行或列，或手动指定坐标!');
			    }
		    };
		    _computeAxis(previousId,div);
	    	if($.isFunction(this.options.onShowAddNode)){
	    		html=this.options.onShowAddNode.call(this,div);
			}
	    	Public.autoElementWrapperHeight(div);
	    },
	    //新增创建节点
	    _createNode:function(div,previousId){
	    	var _self=this;
	    	var nodeData=$('form',div).formToJSON({check:false,encode:false});
	    	var objectKindCode=nodeData.objectKindCode;
	    	if (Public.isBlank(objectKindCode)) {
	    		Public.errorTip('请选择节点类型!');
        		return false;
	    	}
	    	if (Public.isBlank(nodeData.code)) {
	    		Public.errorTip('请填写编号!');
	    		$('input[name="code"]',div).focus();
        		return false;
	    	}
	    	if(objectKindCode==NodeKind.ACTIVITY){
	    		if (Public.isBlank(nodeData.name)) {
        			Public.errorTip('请填写名称!');
        			$('input[name="name"]',div).focus();
            		return false;
        		}
	    	}else if(objectKindCode==NodeKind.RULE){
	    		if (Public.isBlank(nodeData.ruleKind)) {
            		Public.errorTip('请选择规则类型!');
            		return false;
            	}
	    	}else if(objectKindCode==NodeKind.INTERFACE){
	    		if (Public.isBlank(nodeData.interfaceKind)) {
            		Public.errorTip('请选择接口类型!');
            		return false;
            	}
	    		if (Public.isBlank(nodeData.name)) {
        			Public.errorTip('请填写名称!');
        			$('input[name="name"]',div).focus();
            		return false;
        		}
	    	}else if(objectKindCode==NodeKind.SHADOW){
	    		if (Public.isBlank(nodeData.quoteId)) {
        			Public.errorTip('请选中引用节点!');
            		return false;
        		}
	    	}
	    	//判断是否为插入
        	var isInsert=0,previousNode=null;
        	if(previousId){
        		previousNode=_self.getNodeById(previousId);
        		if(previousNode){//存在父节点才有插入操作
        			isInsert=nodeData.yesorno;
        		}
        	}
        	//输入接口不存在父节点
        	if(previousNode&&objectKindCode==NodeKind.INTERFACE){
        		var interfaceKind=nodeData.interfaceKind;
        		if(interfaceKind=='input'){
        			previousNode=null;
        			isInsert=0;
        		}
        	}
        	nodeData['isInsert']=isInsert;
        	if(isInsert==0){//普通添加
        		var xaxis=nodeData.xaxis,yaxis=nodeData.yaxis;
        		if (Public.isBlank(xaxis)) {
            		Public.errorTip('请填写横坐标!');
            		return false;
            	}
        		if (Public.isBlank(yaxis)) {
            		Public.errorTip('请填写纵坐标!');
            		return false;
            	}
            	var id=_self._getUUID();
            	var node=$.extend({},nodeData,{id:id});
            	
            	var _redoOper=function(n,pn){
            		return function(){
	            		var flag=_self.addNode(n,pn);
	                	if(flag){//添加成功
	                		if(pn){//存在父节点添加连线
	                			_self.addLine({id:_self._getUUID(),type: 'sl',from: pn.id,to:n.id});
	                		}
	                		_self.pushUndoOper(_undoOper);
	                	}
	                	return flag;
            		};
            	}(node,previousNode);
            	var _undoOper=function(nId){
            		return function(){
            			_self.delNode(nId);
                		_self.pushRedoOper(_redoOper);
            		};
            	}(id);
            	return _redoOper();
        	}else{//插入添加
        		var id=_self._getUUID();
        		//取下一步ID
        		var nextStepId=_self._getNextStepId(previousId);
        		if(nextStepId){
        			 if(objectKindCode==NodeKind.SHADOW){
        				 Public.errorTip('影子节点不能执行插入操作!');
	       				 return false;
        			 }
        			 if(objectKindCode==NodeKind.INTERFACE){
        				 Public.errorTip('接口节点不能执行插入操作!');
	       				 return false;
        			 }
        		}
        		//计算插入节点坐标
        		var xaxis=parseInt(previousNode.xaxis),yaxis=parseInt(previousNode.yaxis);
        		if(_self.direction=='Y'){
        			yaxis++;
        		}else{
        			xaxis++;
        		}
        		var node=$.extend({},nodeData,{id:id,xaxis:xaxis,yaxis:yaxis});
        		//判断插入的位置是否存在节点
        		var otherNodeId=_self.getNodeIdByAxis(xaxis,yaxis,_self.direction);
        		if(otherNodeId){
        			if(_self._isNextStep(otherNodeId,previousId)){
	       				 Public.errorTip('无法插入节点,请检测关联关系!');
	       				 return false;
	       			}
        			if(!nextStepId){
        				//没有下一步节点
        				var _redoOper=function(n,pn){
	           				 return function(){
	   		        			 //插入行或列
	   		    				 _self._insertRowOrColumn(pn.id,_self.direction);
	   		        			 var flag=_self.addNode(n,pn);	
	   		        			 if(flag){//添加成功
	   		        				_self.addLine({id:_self._getUUID(),type: 'sl',from: pn.id,to: n.id});
	   		         	            //注册撤销方法
	   		         	            _self.pushUndoOper(_undoOper(n.id,pn.id,_self.direction));
	   		        			 }else{//添加失败恢复关联关系
	   		        				_self._deleteRowOrColumn(pn.id,_self.direction);
	   		        			 }
	   		     	             return flag;
	           				 };
	           		    }(node,previousNode);
	           		    var _undoOper=function(mId,pId,direction){
	                		return function(){
	                			 _self.delNode(mId);
	           					 _self._deleteRowOrColumn(pId,direction);
	                         	 _self.pushRedoOper(_redoOper);
	                		};
	                	};
	           		    return _redoOper();
        			}else{
        				//存在下一步节点
        				var _redoOper=function(n,pn,oId){
	           				 return function(){
	   		        			 //插入行或列
	   		    				 _self._insertRowOrColumn(pn.id,_self.direction);
	   		        			 var oldLine=_self.getLineByFromAndTo(pn.id,oId);
	   		        			 _self.delLine(oldLine.id);   
	   		        			 var flag=_self.addNode(n,pn);	
	   		        			 if(flag){//添加成功
	   		        				_self.addLine({id:_self._getUUID(),type:'sl',from: pn.id,to: n.id});
	   		         	            _self.addLine({id:_self._getUUID(),type:'sl',from: id,to: oId});
	   		         	            //注册撤销方法
	   		         	            _self.pushUndoOper(_undoOper(n.id,oldLine,pn.id,_self.direction));
	   		        			 }else{//添加失败恢复关联关系
	   		        				_self.addLine(oldLine);
	   		        				_self._deleteRowOrColumn(pn.id,_self.direction);
	   		        			 }
	   		     	             return flag;
	           				 };
	           		    }(node,previousNode,nextStepId);
	           		    var _undoOper=function(mId,oLine,pId,direction){
	           				 return function(){
	           					 _self.delNode(mId);
	           					 _self.addLine(oLine);
	           					 _self._deleteRowOrColumn(pId,direction);
	                         	 _self.pushRedoOper(_redoOper);
	           				 };
	                    };
	           		    return _redoOper();
        			}
        		}else{
        			//插入位置没有节点
        			if(!nextStepId){
        				var _redoOper=function(n,pn){
            				return function(){
    	        				var flag=_self.addNode(n,pn);
    	        				if(flag){
    	        					_self.addLine({id:_self._getUUID(),type: 'sl',from: pn.id,to: n.id});
    	                    		_self.pushUndoOper(_undoOper);
    	                    	}
    	                    	return flag;
            				};
                    	}(node,previousNode);
                    	var _undoOper=function(nId){
                    		return function(){
                    			_self.delNode(nId);
                        		_self.pushRedoOper(_redoOper);
                    		};
                    	}(id);
    	            	return _redoOper();
        			}else{
        				var _redoOper=function(n,pn,oId){
	           				 return function(){
	   		        			 var oldLine=_self.getLineByFromAndTo(pn.id,oId);
	   		        			 _self.delLine(oldLine.id);   
	   		        			 var flag=_self.addNode(n,pn);	
	   		        			 if(flag){//添加成功
	   		        				_self.addLine({id:_self._getUUID(),type:'sl',from: pn.id,to: n.id});
	   		         	            _self.addLine({id:_self._getUUID(),type:'sl',from: id,to: oId});
	   		         	            //注册撤销方法
	   		         	            _self.pushUndoOper(_undoOper(n.id,oldLine));
	   		        			 }else{//添加失败恢复关联关系
	   		        				_self.addLine(oldLine);
	   		        			 }
	   		     	             return flag;
	           				 };
	           		    }(node,previousNode,nextStepId);
	           		    var _undoOper=function(mId,oLine){
	           				 return function(){
	           					 _self.delNode(mId);
	           					 _self.addLine(oLine);
	                         	 _self.pushRedoOper(_redoOper);
	           				 };
	                    };
	           		    return _redoOper();
        			}
        		}
        	}
        	return false;
	    },
	    //显示节点关联关系
	    showNodeRelation:function(nodeId){
	    	var node=null,_self=this;
	    	var previousHtml=[],nextHtml=[];
	    	previousHtml.push('<table style="display:table;width:100%;" border=0 cellspacing=0 cellpadding=0 class="previousTable hg-form">', '<thead>', '<tr>');
	    	previousHtml.push('<th width="4%">&nbsp;</th>','<th width="25%">上一步</th>', '<th width="48%">描述</th>', '<th width="23%">类型</th>', '</tr>', '</thead>');
	    	nextHtml.push('<table style="display:table;width:100%;" border=0 cellspacing=0 cellpadding=0 class="nextTable operating hg-form">', '<thead>', '<tr>');
	    	nextHtml.push('<th width="4%">&nbsp;</th>','<th width="25%">下一步</th>', '<th width="48%">描述</th>', '<th width="23%">类型</th>', '</tr>', '</thead>');
	    	$.each(this.flowChart.$lineData,function(key,line){
	    		if(line.to==nodeId){
	    			node=_self.getNodeById(line.from);
	    			previousHtml.push('<tr class="list" lineId="', key, '">');
	    			previousHtml.push('<td>','<a href="javascript:void(0);" class="aLink ui-icon-trash" title="删除"><i class="fa fa-trash"></i></a>', '</td>');
	    			previousHtml.push('<td>','[',NodeKindInfo[node.objectKindCode]['name'],node.code,']', node.name, '</td>');
	    			previousHtml.push('<td><input type="text" class="text" maxlength="10" value="', line.name, '"/></td>');
	    			previousHtml.push('<td><input type="text" class="line_type" name="type_',key,'" value="', line.type, '"/></td>');
	    			previousHtml.push('</tr>');
	    		}
	    		if(line.from==nodeId){
	    			node=_self.getNodeById(line.to); 
	    			nextHtml.push('<tr class="list" lineId="', key, '">');
	    			nextHtml.push('<td>','<a href="javascript:void(0);" class="aLink ui-icon-trash" title="删除"><i class="fa fa-trash"></i></a>', '</td>');
	    			nextHtml.push('<td>','[',NodeKindInfo[node.objectKindCode]['name'],node.code,']', node.name, '</td>');
	    			nextHtml.push('<td><input type="text" class="text" maxlength="10" value="', line.name, '"/></td>');
	    			nextHtml.push('<td><input type="text" class="line_type" name="type_',key,'" value="', line.type, '"/></td>');
	    			nextHtml.push('</tr>');
	    		}
	    	});
	    	previousHtml.push('</table>');
	    	nextHtml.push('</table>');
	    	var html=['<div style="height:300px;overflow-x:hidden;overflow-y:auto;">']
	    	html.push('<div><b>上一步</b></div><div class="navline"></div>');
	    	html.push.apply(html,previousHtml);
	    	html.push('<div><b>下一步</b></div><div class="navline"></div>');
	    	html.push.apply(html,nextHtml);
	    	html.push('</div>');
	    	html.push('<div style="text-align:right;width:500px">');
	    	html.push('<button type="button" id="addPrevious" class="btn btn-info" >新增上一步</button>&nbsp;&nbsp;');
	    	html.push('<button type="button" id="addNext" class="btn btn-info" >新增下一步</button>&nbsp;&nbsp;');
	    	html.push('<button type="button" id="saveLine" class="btn btn-success" >确 定</button>&nbsp;&nbsp;');
	    	html.push('<button type="button" id="cancelDialog" class="btn btn-info">取 消</button>&nbsp;&nbsp;');
	    	html.push('<div class="showNodes" style="display:none;position:absolute;overflow-x:hidden;overflow-y:auto;background-color:#ffffff;border:1px solid #e1e1e1;"></div>');
	    	html.push('</div>');	
		    var options = {title: '节点关联关系',content: html.join(''),width: 500,opacity: 0.1,height:350,
		        onClick: function ($el,div) {
		        	var elId=$el.attr('id');
		            if(elId=='cancelDialog'){
		            	this.close();
		            	return;
		            }else if(elId=='addPrevious'){
		            	_showNodeByRelation('previous');
		            	return;
		            }else if(elId=='addNext'){
		            	_showNodeByRelation('next');
		            	return;
		            }else if(elId=='saveLine'){
		            	_saveLine();
		            	this.close();
		            	return;
		            }
		            if($el.hasClass('ui-icon-trash')){
		            	var parent=$el.parents('tr.list');
		            	var lineId=parent.attr('lineId');
		            	parent.remove();
		            	_doDelLine(lineId);
		            	return;
		            }
		            if($el.parent().hasClass('flow_node_choose')){
		            	_doAddLine($el.parent());
		            	return;
		            }
		            if($el.hasClass('flow_node_choose')){
		            	_doAddLine($el);
		            	return;
		            }
		        }
		    };
		    var div=Public.dialog(options);
		    $('input.line_type',div).each(function(i,o){
		    	$(o).combox({data:LineTypes});
		    });
		    var showNodesDiv=$('div.showNodes',div).css({top:30,left:5,width:'50%',height:300}).hide();
		    showNodesDiv.on('mouseleave',function(){$(this).hide();});
		    //查询可关联的节点
		    function _showNodeByRelation(type){
		    	var allNodes=$.extend({},_self.flowChart.$nodeData);
		    	//删除自己
		    	delete allNodes[nodeId]; 
		    	//删除已关联
		    	$.each(_self.flowChart.$lineData,function(key,line){
		    		if(line.to==nodeId){
		    			delete allNodes[line.from];
		    		}
		    		if(line.from==nodeId){
		    			delete allNodes[line.to];
		    		}
		    	});
		    	var allNodesArray=[];
		    	$.each(allNodes,function(id,node){
		    		allNodesArray.push($.extend({},node,{id:id}));
		    	});
		    	//节点排序
		    	allNodesArray=allNodesArray.sort(function(n1,n2){
		    		var kindSequence1=NodeKindInfo[n1.objectKindCode]['sequence'],code1=n1.code;
		    		var kindSequence2=NodeKindInfo[n2.objectKindCode]['sequence'],code2=n2.code;
		    		var sequence1=kindSequence1*1000+parseInt(code1,10);
		    		var sequence2=kindSequence2*1000+parseInt(code2,10);
		    		return sequence1 - sequence2;
		    	});
		    	var nodesHtml=[],from,to;
		    	$.each(allNodesArray,function(i,node){ 
		    		var id=node.id;
		    		if(type=='previous'){//上一步
		    			from=id,to=nodeId;
		    		}else{
		    			from=nodeId,to=id;
		    		}
		    		//校验是否允许关联
		    		if(!_self._checkAssociationById(to,from,false)){
	    				return;
	    			}
		    		nodesHtml.push('<li nodeId="',id,'" type="',type,'" class="flow_node_choose">');
		    		nodesHtml.push('[<span class="color_gray">',NodeKindInfo[node.objectKindCode]['name'],'</span>');
		    		nodesHtml.push('<b class="color_red">',node.code,'</b>]'); 
		    		nodesHtml.push(node.name);
		    		nodesHtml.push('</li>');
		    	});
		    	var _html=['<ul class="flow_node_list">'];
		    	if(nodesHtml.length>0){
		    		_html.push.apply(_html,nodesHtml);
		    	}else{
		    		_html.push('<li>','<b class="color_gray">没有可关联的节点!</b>','</li>');
		    	}
		    	_html.push('</ul>');
		    	showNodesDiv.html(_html.join('')).show();
		    };
		    //执行删除链接
		    function _doDelLine(lineId){
		    	var lineNode=_self.getLineById(lineId);
		    	lineNode['id']=lineId;
		    	var _redoOper=function(line){
		    		return function(){
		    			_self.delLine(line.id);
			    		_self.pushUndoOper(_undoOper);
		    		};
				}(lineNode);
				var _undoOper=function(line){
					return function(){
						_self.addLine(line);
						_self.pushRedoOper(_redoOper);
					};
	            }(lineNode);
	            _redoOper();
		    };
		    //执行添加链接
		    function _doAddLine($el){
		    	var _id=$el.attr('nodeId'),type=$el.attr('type');
		    	var from=nodeId,to=_id;//默认下一步
		    	if(type=='previous'){//添加上一步
		    		from=_id,to=nodeId;
		    	}
		    	var _lineId=_self._getUUID();
		    	var lineNode={id:_lineId,type: 'sl',from: from,to: to};
		    	var addHtml=['<tr class="list" lineId="',_lineId, '">'];
		    	addHtml.push('<td>','<span class="ui-icon ui-icon-trash">&nbsp;</span>', '</td>');
		    	addHtml.push('<td>', $el.text(), '</td>');
		    	addHtml.push('<td><input type="text" class="text" maxlength="10" value=""/></td>');
		    	addHtml.push('<td><input type="text" class="line_type" name="type_',_lineId,'" value="sl"/></td>');
		    	addHtml.push('</tr>');
		    	var _add=$(addHtml.join(''));
		    	$('table.'+type+'Table',div).append(_add);
		    	$('input.line_type',_add).each(function(i,o){
			    	$(o).combox({data:LineTypes});
			    });
		    	showNodesDiv.hide();
		    	var _redoOper=function(line){
		    		return function(){
		    			_self.addLine(line);
			    		_self.pushUndoOper(_undoOper);
		    		};
				}(lineNode);
				var _undoOper=function(line){
					return function(){
						_self.delLine(line.id);
						_self.pushRedoOper(_redoOper);
					};
	            }(lineNode);
	            _redoOper();
		    };
		    //保存数据改变
		    function _saveLine(){
		    	var _lines=[],_oldLines=[];
		    	$('tr.list',div).each(function(){
		    		var lineId=$(this).attr('lineId');
		    		var lineName=$(this).find('input.text').val();
		    		var lineType=$(this).find('input.line_type').val();
		    		var lineNode=_self.getLineById(lineId);
		    		_lines.push({id:lineId,name:lineName,type:lineType});
		    		_oldLines.push($.extend({},lineNode,{id:lineId}));
		    	});
		    	var _redoOper=function(ls){
		    		return function(){
		    			$.each(ls,function(i,l){
		    				_self.flowChart.setName(l.id,l.name,'line');
		    	    		_self.flowChart.setLineType(l.id,l.type);
		    			});
			    		_self.pushUndoOper(_undoOper);
			    		_self.setIsModified(true);
		    		};
				}(_lines);
				var _undoOper=function(ls){
					return function(){
						$.each(ls,function(i,l){
		    				_self.flowChart.setName(l.id,l.name,'line');
		    	    		_self.flowChart.setLineType(l.id,l.type);
		    			});
						_self.pushRedoOper(_redoOper);
					};
	            }(_oldLines);
	            _redoOper();
		    };
	    },
	    _selectAllNodes:function(btn){
	    	if(btn.hasClass('checkedbox')){
				btn.removeClass('checkedbox');
				//清空已选
    			this._cleanSelectNodes();
			}else{
				btn.addClass('checkedbox');
				$('div.FlowChart_item',this.element).each(function(){
					$(this).addClass('checked-item');
					$('span.checkbox',this).addClass('checkbox-checked'); 
				});
			}
	    },
	    _cleanSelectNodes:function(){
	    	//清空全部选中节点
    		$('div.checked-item',this.element).removeClass('checked-item');
			$('span.checkbox-checked',this.element).removeClass('checkbox-checked');
	    },
	    _getSelectedNodes:function(){
	    	var selectedNodes={},_self=this;
	    	$('div.checked-item',this.element).each(function(){
	    		var id=$(this).attr('id');
	    		selectedNodes[id]=_self.getNodeById(id);
	    	});
	    	return selectedNodes;
	    },
	    _modifyAxis:function(node,type){
	    	var xaxis=parseInt(node.xaxis),yaxis=parseInt(node.yaxis);
	    	switch(type){
				case "up":		yaxis--; break;
				case "right":	xaxis++; break;
				case "down":	yaxis++; break;
				case "left":	xaxis--; break;
			}
	    	return {xaxis:xaxis,yaxis:yaxis};
	    },
	    _moveNodes:function(nodes,type){
	    	var allNodes=[],_self=this;
	    	//排除本次需要移动的节点
	    	$.each(this.flowChart.$nodeData,function(k,n){
	    		if(nodes[k]){
	    			return;
	    		}
	    		allNodes.push(n);
	    	});
	    	var flag=true,_mNodes=[],_oldNodes=[];
	    	//校验是否允许移动节点
	    	$.each(nodes,function(k,n){
	    		var _h=['节点[',n.code,']',n.name];
	    		var axis=_self._modifyAxis(n,type);
	    		if(axis.xaxis<0||axis.yaxis<0){
	    			flag=false;
	    		}
	    		if(flag){
	    			//校验坐标
		    		$.each(allNodes,function(i,_an){
			    		if(parseInt(_an.xaxis)==axis.xaxis&&parseInt(_an.yaxis)==axis.yaxis){
			    			_h.push('移动后与节点[',_an.code,']',_an.name,'位置重合');
			    			flag=false;
			    			return false;
			    		}
			    	});
	    		}
	    		if(!flag){
	    			_h.push(',无法移动!')
	    			Public.errorTip(_h.join(''));
	    			return false;
	    		}
	    		_oldNodes.push(n);
	    		_mNodes.push($.extend({},n,axis));
	    	});
	    	if(!flag){
	    		return false;
	    	}
	    	var _redoOper=function(ns){
	    		return function(){
	    			$.each(ns,function(i,n){
			    		_self.moveNode(n);
			    	});
		    		_self.pushUndoOper(_undoOper);
	    		};
			}(_mNodes);
			var _undoOper=function(ns){
				return function(){
					$.each(ns,function(i,n){
			    		_self.moveNode(n);
			    	});
					_self.pushRedoOper(_redoOper);
				};
            }(_oldNodes);
            _redoOper();
	    	this.reInitworkAreaSize();
	    },
	    _moveSelectedNode:function(type){
	    	//获取选中节点
	    	var selectedNodes=this._getSelectedNodes();
	    	if($.isEmptyObject(selectedNodes)){
	    		return false;
	    	}
	    	this._moveNodes(selectedNodes,type);
	    },
	    //删除节点
	    _deleteNodes:function(nodes){
	    	var _self=this;_nodes=[],_lines=[];
	    	$.each(nodes,function(id,node){
	    		$.each(_self.flowChart.$lineData,function(lineId,line){
	    			if(line.from==id||line.to==id){
	    				_lines.push($.extend({},line,{id:lineId}));
	    			}
	    		});
	    		_nodes.push($.extend({},node,{id:id}));
	    	});
	    	var _redoOper=function(ns){
	    		return function(){
		    		$.each(ns,function(i,node){
			    		_self.delNode(node['id']);
			    	});
		    		_self.pushUndoOper(_undoOper);
		    		if($.isFunction(_self.options.onNodeChange)){
		    			_self.options.onNodeChange.call(_self);
					}
		    		_self.setIsModified(true);
	    		};
			}(_nodes);
			var _undoOper=function(ns,ls){
				return function(){
					$.each(ns,function(i,n){
						_self._addNode(n);
					});
					$.each(ls,function(i,l){
						_self._addLine(l);
					});
					_self.pushRedoOper(_redoOper);
					if($.isFunction(_self.options.onNodeChange)){
		    			_self.options.onNodeChange.call(_self);
					}
		    		_self.setIsModified(true);
				};
            }(_nodes,_lines);
            _redoOper();
	    },
	    _deleteSelectedNode:function(){
	    	//获取选中节点
	    	var selectedNodes=this._getSelectedNodes();
	    	if($.isEmptyObject(selectedNodes)){
	    		return false;
	    	}
	    	this._deleteNodes(selectedNodes);
	    },
	    //定位并选中节点
	    fixedNode:function(nodeId){
	    	var nodeDom=this.flowChart.$nodeDom[nodeId];
	    	if(!nodeDom){
	    		return;
	    	}
	    	//清空全部选中节点
    		this._cleanSelectNodes();
	    	//标注选中节点
	    	var _item=$('#'+nodeId);
			if(!_item.hasClass('checked-item')){
				_item.addClass('checked-item');
				_item.find('span.checkbox').addClass('checkbox-checked');
			}
			//定位节点
	    	var position=$(nodeDom).position();
	    	var _workArea= this.flowChart.$workArea.parent();
			$(_workArea).scrollLeft(position.left-this._getSize('cellWidth')*2);
			$(_workArea).scrollTop(position.top-this._getSize('cellHeight')*2);
	    },
	    fixedAndFocusNode:function(nodeCode){
	    	var nodeId='';
	    	$.each(this.flowChart.$nodeData,function(k,n){
	    		if(n.nodeCode==nodeCode){
	    			nodeId=k;
	    			return false;
	    		}
	    	});
	    	if(nodeId==''){
	    		return;
	    	}
	    	var nodeDom=this.flowChart.$nodeDom[nodeId];
	    	if(!nodeDom){
	    		return;
	    	}
	    	//标注选中节点
	    	var _item=$('#'+nodeId);
			if(!_item.hasClass('highlight-item')){
				_item.addClass('highlight-item');
			}
			//定位节点
	    	var position=$(nodeDom).position();
	    	var _workArea= this.flowChart.$workArea.parent();
			$(_workArea).scrollLeft(position.left-this._getSize('cellWidth')*2);
			$(_workArea).scrollTop(position.top-this._getSize('cellHeight')*2);
	    },
	    positionNode:function(nodeId){
	    	var nodeDom=this.flowChart.$nodeDom[nodeId];
	    	if(!nodeDom){
	    		return;
	    	}
			//定位节点
	    	var position=$(nodeDom).position();
	    	var _workArea= this.flowChart.$workArea.parent();
			$(_workArea).scrollLeft(position.left-this._getSize('cellWidth')*2);
			$(_workArea).scrollTop(position.top-this._getSize('cellHeight')*2);
	    }
	});
	
	$.stopPropagation=function(e) {
	    e.preventDefault();
	    e.stopPropagation();
	    return false;
	};
})(jQuery);