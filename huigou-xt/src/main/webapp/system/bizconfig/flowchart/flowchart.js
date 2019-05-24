var gridManager=null,widthProportion=0.7;
$(document).ready(function() {
	initializeGrid();
	initializeUI();
	loadFlowNodesAndLines();
});

function initializeUI(){
	var flowChartDiv=$('#flowChartDiv');
	flowChartDiv.flowChart({
		onBtnSaveClick:function(){
			saveAll();
		},
		onBtnNewClick:function(){
			flowChartDiv.flowChart('showAddNodeDialog');
		},
		onNodeChange:function(){
			var datas=flowChartDiv.flowChart('exportData');
			setGridDatas(datas['nodes']);
		},
		onItemDel:function(id,type){
			if(type=='node'){
				if (BPMCUtil.isEditAttributePage()) {
					var viewId=$('input[name="viewId"]',$('#editAttributePageDiv')).val();
					if(viewId==id){
						BPMCUtil.hideEditAttributePage();
					}
				}
			}
			return true;
		},
		onApplyUpdateNode:function(id){
			updateAttributeHandler(id);
		},
		onNodeSelect:function(item){
			//选择节点后手动关闭编辑框
			if (BPMCUtil.isEditAttributePage()) {
				var id=item.attr('id');
				var viewId=$('input[name="viewId"]',$('#editAttributePageDiv')).val();
				if(viewId!=id){
					BPMCUtil.hideEditAttributePage();
				}
			}
		},
		getAddNodeHtml:function(){
			return $('#inputDiv').html();
		},
		onShowAddNode:function(div){
		},
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
				html.push("<span style='color:#595965;",_fontSizeStyle,"'><i class='fa ",code,"'></i></span>");
				html.push("</span>");
			});
			return html.join('');
		}
	});
	UICtrl.layout("#layout",{rightWidth:4,onSizeChanged:function(){
		if(flowChartDiv.hasClass('FlowChart')){
			setTimeout(function(){
				flowChartDiv.removeClass('ui-hide').flowChart('reInitSize',flowChartDiv.parent().outerWidth(),flowChartDiv.parent().outerHeight());
			},0);
		}
	}});
	$('#layout').find('div.ui-layout-header-toggle').addClass('m-t-sm');
	//左边悬浮菜单
	$.floatDialog({
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
	});
}

function onWrapperResize(_size){
	var flowChartDiv=$('#flowChartDiv');
	flowChartDiv.flowChart('reInitSize',flowChartDiv.parent().outerWidth(),flowChartDiv.parent().outerHeight());
}

function addDirectionToggle(flowChartDiv){
	var toolBar=$('div.FlowChart_head',flowChartDiv);
	var chartDirection=geChartDirection();
	$('#flowChartDiv').flowChart('setDirection',chartDirection);
	var html=['<div class="float_right_span">'];
	html.push('<label style="width:170px;">绘图方向:&nbsp;');	
	html.push('<input type="radio" name="chartDirection" value="Y" ',chartDirection=='Y'?'checked="checked"':'','>纵向&nbsp;&nbsp;');
	html.push('<input type="radio" name="chartDirection" value="X" ',chartDirection=='X'?'checked="checked"':'','>横向&nbsp;&nbsp;');
	html.push('</label>');
	html.push('</div>');
	var div=$(html.join('')).appendTo(toolBar);
	div.find('input').on('click', function () {
		var radio=$(this);
	    setTimeout(function () {
	    	$('#flowChartDiv').flowChart('setDirection',radio.val());
	    }, 0);
	});
}

function isShowGrid(flag){
	
}

function geBusinessProcessId(){
	return $('#businessProcessId').val();
}

function geChartDirection(){
	var _direction=$('#chartDirection').val();
	if (Public.isBlank(_direction)) {
		return $('#flowChartDiv').flowChart('getDirection');
	}
	return _direction;
}

function loadFlowNodesAndLines(){
	var businessProcessId=geBusinessProcessId();
	Public.ajax(web_app.name + '/bizFlowChart/queryFlowNodesAndLines.ajax', {businessProcessId:businessProcessId}, function (data) {
		$('#flowChartDiv').flowChart('initData',data);
	});
}


function initializeGrid() {
    gridManager = UICtrl.grid('#nodeListGrid', {
        columns: [
            {display: "序号", name: "code", width:'15%', minWidth: 60, type: "string", align: "center",
            	render: function (item) {
            		var objectKindCode=item.objectKindCode;
            		var color=NodeKindInfo[objectKindCode]['color'];
            		var html=['<div class="',color,'">'];
            		html.push(item.code);
            		html.push('</div>')
                    return html.join('')
                }
            },
            {display: "名称", name: "name", width:'50%', minWidth: 160, type: "string", align: "left",
            	render: function (item) {
            		var objectKindCode=item.objectKindCode;
            		if(objectKindCode==NodeKind.SHADOW){
            			var _quoteNode=$('#flowChartDiv').flowChart('getShadowNodeInfo',item);
    					if(_quoteNode){
    						return '引用['+_quoteNode.code+']';
    					}else{
    						return '';
    					}
            		}else if(objectKindCode==NodeKind.RULE){
            			var name=item.name,ruleKind=item.ruleKind;
            			if (Public.isBlank(name)) {
            				name='规则';
            			}
            			var ruleKindName=RuleKindName[ruleKind];
            			return name+'['+ruleKindName+']';
            		}else{
            			 return item.name;
            		}
                }
            },
            {display: "横坐标", name: "xaxis", width: '15%', minWidth: 60, type: "string", align: "center"},
            {display: "纵坐标", name: "yaxis", width: '15%', minWidth: 60, type: "string", align: "center"}
        ],
        width: '100%',
        height: '100%',
        heightDiff: -10,
        usePager:false,
        onDblClickRow: function (data, rowindex, rowobj) {
            updateAttributeHandler(data.viewId);
        }
    });

    $('#queryKindCodeSpan').on('click',function(e){
    	setTimeout(function(){
    		var datas=$('#flowChartDiv').flowChart('exportData');
    		setGridDatas(datas['nodes']);
    	},10);
    });
}

function setGridDatas(nodes) {
	var queryKindCode='all';
	$('#queryKindCodeSpan').find('input:radio').each(function(){
		if($(this).is(':checked')){
			queryKindCode=$(this).val();
			return false;
		}
	});
	var _nodes=[];
	$.each(nodes,function(i,o){
		if(queryKindCode=='all'){
			_nodes.push(o);
		}else{
			if(queryKindCode==o.objectKindCode){
				_nodes.push(o);
			}
		}
	});
	_nodes=_nodes.sort(function(n1,n2){
		var kindSequence1=NodeKindInfo[n1.objectKindCode]['sequence'],code1=n1.code;
		var kindSequence2=NodeKindInfo[n2.objectKindCode]['sequence'],code2=n2.code;
		var sequence1=kindSequence1*1000+parseInt(code1,10);
		var sequence2=kindSequence2*1000+parseInt(code2,10);
		return sequence1 - sequence2;
	});
	gridManager.options.data = {Rows: _nodes};
    gridManager.loadData();
}

function saveAll(){
	var businessProcessId=geBusinessProcessId();
	var chartDirection=$('#flowChartDiv').flowChart('getDirection');
	var datas=$('#flowChartDiv').flowChart('exportData');
	Public.ajax(web_app.name + '/bizFlowChart/saveFlowNodesAndLines.ajax', 
		{
			businessProcessId:businessProcessId,
			chartDirection:chartDirection,
			nodes:Public.encodeJSONURI(datas['nodes']),
			lines:Public.encodeJSONURI(datas['lines']),
			areas:Public.encodeJSONURI(datas['areas'])
		},function (data) {
			$('#flowChartDiv').flowChart('setIsModified',false);
		}
	);
}

function updateAttributeHandler(id) {
	if (Public.isBlank(id)) {
		return;
	}
	var node=$('#flowChartDiv').flowChart('getNodeById',id);
	if(!node) return;
	var businessProcessId=geBusinessProcessId();
    var url = web_app.name + '/bizFlowChart/showLoadFlowNode.load';
    BPMCUtil.createEditAttributePage($('#nodeInfoDiv'), url, {viewId: id,businessProcessId:businessProcessId,objectKindCode:node.objectKindCode}, function (div) {
    	isShowGrid(true);//打开编辑框
        initializeEditPageUI(div,node);
        $('#flowChartDiv').flowChart('fixedNode',id);
    });
}

//页面关闭前验证数据是否被修改
function checkDefaultValueModified(){
	var flag=BPMCUtil.checkDefaultValueModified();
	if(flag===true) return true;
	flag=$('#flowChartDiv').flowChart('getIsModified');
	return flag;
}


function reLoadFlowChart(pid){
	var businessProcessId=geBusinessProcessId();
	if(pid==businessProcessId){
		return;
	}
	var flag=checkDefaultValueModified();
	if(flag===true){
		UICtrl.confirm('您修改的数据尚未保存，确定离开此页面吗？',function(){
			Public.locationHref('bizFlowChart/forwardFlowchart.load',{businessProcessId:pid});
		});
	}else{
		Public.locationHref('bizFlowChart/forwardFlowchart.load',{businessProcessId:pid});
	}
}
