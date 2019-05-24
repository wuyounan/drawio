(function ($) {
	
	$.selectOrgCommon=function(dialog,params,div){
		var obj =new selectOrgCommon(params,div);
		dialog._selectOrg=obj;
		obj.close=function(){
			dialog.close();
		};
		return obj;
	};
	
	function selectOrgCommon(params,div){
		this.div=div;
		this.initParams(params);
		this.initVariable();
	}
	
	$.extend(selectOrgCommon.prototype,{
		extend:function(object){
			for (property in object) { 
				this[property] = object[property]; 
			} 
		},
		initParams:function(params){
			this.inputParams=$.extend({
				filter: "", //过滤条件
			    parentId: "", //根过滤条件
			    manageCodes: "", //管理权限
			    displayableOrgKinds: "ogn,dpt,pos,psm", //显示的组织机构类别
			    selectableOrgKinds: "psm", //可选择的组织机构类别
			    showDisabledOrg: 0, //显示禁用组织
			    showVirtualOrg: 0, //显示虚拟组织
			    showProjectOrg: 0, //显示项目组织
			    showPosition: 0, //显示岗位
			    chooseChildCheck: 1,//级联选择
			    customDefinedRoot: 0, //自定义根
			    rootIds: "", //自定义根
			    multiSelect: 1,
			    showCommonGroup: 0,
			    showSendMessageColumn: 0,
			    cascade: 1,
			    selected: []
			},params);
		},
		initVariable:function(){
			this.treeManager=null;
			this.userGroupTreeManager=null;
			this.gridManager=null;
			this.selectedData = [];
			this.orgQueryGridManager = null;
			this.isInitUserGroup = false;
			this._selectableType={isOrg:true,isGroup:false,isQuery:false};
		},
		initView:function(){
			if (this.inputParams.showVirtualOrg == 1 ) {
	            $(".showVirtualOrg",this.div).attr("checked", 'true');
	        }
	        if (this.inputParams.showPosition == 1) {
	            $(".showPosition",this.div).attr("checked", 'true');
	        }
	        if (this.inputParams.chooseChildCheck == 1) {
	            $(".chooseChildCheck",this.div).attr("checked", 'true');
	            this.setChooseChildCheck();
	        }
			this.initializeLayout();
			this.bindEvents();
			this.loadOrgTreeView();
			this.initializeGrid();
		},
		initializeLayout:function(){
			var parentWidth=getDefaultDialogWidth(),heightDiff=5,topHeight=50;
			if(parentWidth < 768){
				this.div.addClass('dom-overflow-auto');
				heightDiff=-67;
				topHeight=70;
			}
			var _layoutDom=$('div.orgSelectLayout',this.div);
			UICtrl.layout(_layoutDom, {heightDiff:heightDiff,topHeight:topHeight,leftWidth:6,allowLeftResize:false,allowLeftCollapse:false});
	    	var layout=_layoutDom.data('ui_bootstrap_layout'),html=[];
	    	html.push('<div class="ui-layout-header-toggle">');
	    	html.push('<button type="button" class="btn btn-success header-btn divAdd" title="添加"><i class="fa fa-angle-double-right"></i></button>&nbsp;&nbsp;');
	    	html.push('<button type="button" class="btn btn-danger header-btn divDelete" title="删除"><i class="fa fa-trash"></i></button>&nbsp;&nbsp;');
	    	html.push('</div>');
	    	layout.left.header.prepend(html.join(''));
	    	//查询条件显示样式
	    	var conditionCss={left:'60px',width:'180px',top:'2px'};
	    	if(parentWidth < 768){
	    		//修改顺序
	    		layout.left.insertAfter(layout.center).width(layout.left.width());
	    		layout.center.content.addClass('ui-layout-height-auto').height(10);
	    		layout.center.height('auto');
	    		conditionCss={left:'5px',width:'160px',top:'2px'};
	    	}
	    	//查询条件显示位置调整
    		$('div.queryConditionDiv',this.div).css(conditionCss).removeClass('ui-hide');
		},
		bindEvents:function(){
			var g=this;
			$('.divAdd',g.div).click(function () {
	            g.addData();
	        });
			$('.divDelete',g.div).click(function () {
	            g.deleteData();
	        });
	        $('.chooseChildCheck',g.div).click(function () {
	            g.setChooseChildCheck();
	        });
	        $(".showVirtualOrg",g.div).click(function () {
	            if (!g.treeManager) {
	                return;
	            }
	            if ($(this).is(':checked')) {
	                g.inputParams.showVirtualOrg = 1;
	            } else {
	                g.inputParams.showVirtualOrg = 0;
	            }
	            g.loadOrgTreeView();
	        });
	        $(".showPosition",g.div).click(function () {
	            if (!g.treeManager) {
	                return;
	            }
	            if ($(this).is(':checked')) {
	                g.inputParams.showPosition = 1;
	            } else {
	                g.inputParams.showPosition = 0;
	            }
	            g.loadOrgTreeView();
	        });
	        $('.ui-grid-query-button',g.div).click(function () {
	            var value = $('.ui-grid-query-input',g.div).val();
	            if (value != '') {
	            	g._switchSelectable({isQuery:true});
	                if (g.orgQueryGridManager) {
	                    UICtrl.gridSearch(g.orgQueryGridManager, {
	                    	paramValue: encodeURI(value),
	                    	showProjectOrg: g.inputParams.showProjectOrg == 1 ? 1 : 0,
	                    	showVirtualOrg: g.inputParams.showVirtualOrg == 1 ? 1 : 0,
	                    	displayableOrgKinds: g.inputParams.showPosition ==1 ?"pos,psm":"psm"
	                    });
	                }else{
	                	g.initializeOrgQueryGrid({paramValue: encodeURI(value)});
	                }
	            }
	        });
	        $('.ui-grid-query-clear',g.div).click(function () {
	        	$('.ui-grid-query-input',g.div).val('');
	        	g._switchSelectable({isQuery:false});
	        });
	        $('.ui-grid-query-input',g.div).keyup(function (e) {
	            var value = $(this).val();
	            if (value == '') {
	            	g._switchSelectable({isQuery:false});
	            } else {
	                var k = e.charCode || e.keyCode || e.which;
	                if (k == 13) {
	                    $('.ui-grid-query-button',g.div).trigger('click');
	                }
	            }
	        });
	        //切换选择来源
	        $('div.chooseTabs',g.div).on('click', function (e) {
	            var $clicked = $(e.target || e.srcElement),li=null;
	            if($clicked.is('i.fa')){$clicked=$clicked.parent();}
	            if ($clicked.is('button')) {
	                if ($clicked.hasClass('active')) {
	                    return false;
	                }
	                $(this).find('button.active').removeClass('active');
	                var divExpr = $clicked.attr('divExpr');
	                $clicked.addClass('active');
	                if (divExpr == 'chooseByGroup' ) {
	                	if(!g.isInitUserGroup){
	                		g.initializeUserGroup();
	                	}
	                	g._switchSelectable({isGroup:true,isOrg:false});
	                }else if(divExpr=='chooseByOrgDiv'){
	                	g._switchSelectable({isOrg:true,isGroup:false});
	                }
	            }
	        });
		}, //切换可选列表显示
	    _switchSelectable:function(op){
	    	var g=this;
	    	g._selectableType=$.extend(g._selectableType,op);
	    	if(g._selectableType.isQuery){
	    		$('div.orgTreeDiv',g.div).hide();
	    		$('div.chooseByGroup',g.div).hide();
	            $('div.orgSelectLayoutLeft',g.div).addClass('dom-overflow');
	        	$('div.orgQueryGridDiv',g.div).show();
	    	}else{
	    		$('div.orgSelectLayoutLeft',g.div).removeClass('dom-overflow');
	    		$('div.orgQueryGridDiv',g.div).hide();
	    		if(g._selectableType.isOrg){
	    			$('div.orgTreeDiv',g.div).show();
	            	$('div.chooseByGroup',g.div).hide();
	    		}else{
	    			$('div.orgTreeDiv',g.div).hide();
	            	$('div.chooseByGroup',g.div).show();
	    		}
	    	}
	    },
	    loadOrgTreeView:function() {
	    	var g=this,inputParams=g.inputParams;
	    	var _orgTree=$(".orgTree",g.div);
	        if (g.treeManager) {
	        	_orgTree.removeAllNode();
	        	_orgTree=$("<ul class='orgTree'></ul>").appendTo($(".orgTreeDiv",g.div));
	        }
	        g.treeManager = UICtrl.tree(_orgTree, {
	            url: web_app.name + "/org/queryOrgs.ajax",
	            param: {
	                parentId: inputParams.parentId,
	                filter: inputParams.filter,
	                manageCodes: inputParams.manageCodes,
	                displayableOrgKinds: inputParams.displayableOrgKinds,
	                showDisabledOrg: inputParams.showDisabledOrg,
	                showVirtualOrg: inputParams.showVirtualOrg,
	                showProjectOrg: inputParams.showProjectOrg,
	                showPosition: inputParams.showPosition,
	                customDefinedRoot: inputParams.customDefinedRoot,
	                rootIds: inputParams.rootIds
	                //sortname: "fullSequence"
	            },
	            checkbox: true,
	            idFieldName: "id",
	            parentIDFieldName: "parentId",
	            textFieldName: "name",
	            iconFieldName: "nodeIcon",
	            btnClickToToggleOnly: true,
	            autoCheckboxEven: false,
	            nodeWidth:200,
	            isLeaf: function (data) {
	                data.children = [];
	                data.nodeIcon = OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
	                return data.hasChildren == 0;
	            },
	            onBeforeExpand: function(node){
	            	if (node.data.hasChildren) {
	                    if (!node.data.children || node.data.children.length == 0) {
	                    	Public.ajax(web_app.name +"/org/queryOrgs.ajax", {
	                        	parentId: node.data.id,
	                            filter: inputParams.filter,
	                            manageCodes: inputParams.manageCodes,
	                            displayableOrgKinds: inputParams.displayableOrgKinds,
	                            showDisabledOrg: inputParams.showDisabledOrg,
	                            showVirtualOrg: inputParams.showVirtualOrg,
	                            showPosition: inputParams.showPosition
	                        }, function (data) {
	                            g.treeManager.append(node.target, data.Rows);
	                        });
	                    }
	                }
	            },
	            dataRender: function (data) {
	            	return data['Rows'];
	            },
	            onDblclick: function (node) {
	                g.addDataOneNode(node.data);
	            }
	        });
	        g.setChooseChildCheck();
	    },//设置树选择是否支持级联
	    setChooseChildCheck:function(){
	    	var g=this;
	    	if (!g.treeManager) {
	           return;
	        }
	        setTimeout(function(){
	        	var _chooseChildCheck=$('.chooseChildCheck',g.div).is(':checked');
	        	g.treeManager.options.autoCheckboxEven = _chooseChildCheck;
	            if (g.userGroupTreeManager) {
	            	g.userGroupTreeManager.options.autoCheckboxEven =_chooseChildCheck;
	    	    } 
	         },0);
	    },
	    initializeUserGroup:function(){
	    	var g=this;
	    	Public.ajax(web_app.name + "/userGroup/queryAvailableUserGroups.ajax", {}, function (data) {
	            g.isInitUserGroup = true;
	            $.each(data, function (i, o) {
	                var kindId = o['nodeKindId'];
	                if (kindId == 'node') {
	                    o['icon'] = OpmUtil.getOrgImgUrl('pos', 1);
	                }
	            });
	           g.userGroupTreeManager=UICtrl.tree($('.userGroupTree',g.div), {
	                data: data,
	                idFieldName: "id",
	                parentIDFieldName: "parentId",
	                textFieldName: "name",
	                checkbox: true,
	                nodeWidth: 180,
	                isLeaf: function (data){
	                	if (!data) return false;
	                    if(g.isUserGroupTreeOrgNode(data)){
	                    	data['icon'] = OpmUtil.getOrgImgUrl(data.orgKindId, 1);
	                    	return true;
	                    }else if(data.nodeKindId=='tree'){
	                    	return data.children ? false : true;
	                    }
	                    return false;
	                },
	                dataRender:function(data){
	                	return data['Rows'];
	                },
	                delay: function(e){
	                    var data = e.data;
	                    setTimeout(function(){
	                    	g.userGroupTreeManager._updateStyle(g.userGroupTreeManager.tree);
	                    },0);
	                    if(data.nodeKindId == "tree"){//分组数据直接加载
	                    	return true;
	                    }else if(data.nodeKindId=='node'){//定义分组下人员信息延迟加载连接
	                   	 	return { url: web_app.name + "/userGroup/queryAvailableUserGroupDetails.ajax",parms:{groupId:data.id}};
	                    }
	                    return false;
	                },
	                onDblclick: function (node) {
	                	var _data=$.extend({},node.data);
		        		_data.id = _data.orgNodeId;
		                delete _data.groupId;
	                	g.addDataOneNode(_data);
			        }
	            });
	        });
	    },//判断是否是用户组中的组织机构节点
	    isUserGroupTreeOrgNode:function(data){
	    	if(data.nodeKindId != "tree"&&data.nodeKindId!='node'){
	    		return true;
	    	}
	    	return false;
	    },
	    initializeOrgQueryGrid:function(param){
	    	var g=this,inputParams=this.inputParams;
	    	var _param=$.extend(param||{},{
	    		showAllChildrenOrg: 1,
	    		showProjectOrg: inputParams.showProjectOrg == 1 ? 1 : 0,
	    		showVirtualOrg: g.inputParams.showVirtualOrg == 1 ? 1 : 0,
	    		displayableOrgKinds: inputParams.showPosition ==1 ?"pos,psm":"psm"
	    	});
	    	g.orgQueryGridManager = UICtrl.grid($('div.orgQueryGrid',g.div), {
	            columns: [
	                { display: "common.field.name", name: "name", width: 80, minWidth: 60, type: "string", align: "left", frozen: true,
	                    render: function (item) {
	                        return '<div title="' + item.fullName + '">' + item.name + '</div>';
	                    }
	                },
	                { display: "common.field.fullname", name: "fullName", width: 400, minWidth: 60, type: "string", align: "left",
	                    render: function (item) {
	                        return '<div title="' + item.fullName + '">' + item.fullName + '</div>';
	                    }
	                }
	            ],
	            url: web_app.name + '/org/slicedQueryOrgs.ajax',
	            parms: _param,
	            height: $('div.orgQueryGrid',g.div).parent().height(),
	            heightDiff: -13,
	            inWindow:false,
	            checkbox: true,
	            usePager: false,
	            fixedCellHeight: true,
	            selectRowButtonOnly: true,
	            onDblClickRow: function (data, rowindex, rowobj) {
	                g.addDataOneNode(data);
	            }
	        });
	    },
	    isFromTreeView:function(){
	    	 return $('.orgTreeDiv',this.div).is(':visible');
	    },
	    isGroupTreeView:function(){
		   	 return $('.userGroupTree',this.div).is(':visible');
		},
		isQueryGridView:function(){
		  	 return $('.orgQueryGrid',this.div).is(':visible');
		},
		getChooseRowData:function(){// 获取选择的数据
			var g=this,rows=[];
			//组织机构树选择
		    if (g.isFromTreeView()) {
		        $.each(g.treeManager.getChecked(), function (i, o) {
		            rows.push(o.data);
		        });
		    } 
		    //组织机构列表选择
		    if(g.isQueryGridView()){
		        rows = g.orgQueryGridManager.getSelectedRows();
		    }
		    //用户组树选择
		    if(g.userGroupTreeManager&&g.isGroupTreeView()){
		        $.each(g.userGroupTreeManager.getChecked(), function (i, o) {
		        	if(g.isUserGroupTreeOrgNode(o.data)){
		        		var _data=$.extend({},o.data);
		        		_data.id = _data.orgNodeId;
		                delete _data.groupId;
		        		rows.push(_data);
		        	}
		        });
		    }
		    return rows;
		},
		cancelSelect:function(row){//取消选中
			if (this.isFromTreeView()) {
				if(this.treeManager){
					this.treeManager.cancelSelect(row);
			    }
			} 
			if(this.isQueryGridView()){
				if(this.orgQueryGridManager){
					this.orgQueryGridManager.unselect(row);
			    }
		    }
			if(this.isGroupTreeView()){
				if(this.userGroupTreeManager){
					this.userGroupTreeManager.cancelSelect(row);
			    }
		    }
		},
		initializeGrid:function(){
			var g=this;
			//显示已选择数据
			g.reloadGrid();
			//注册删除事件
			$("div.handlerGrid",g.div).on('click',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('i.icon-close')){
					var id=$clicked.parent().data('id');
					$clicked.parent().remove();
					g.deleteOneNode({id:id});
				}
			});
		},
		reloadGrid:function(){
			var g=this,div=$("div.handlerGrid",g.div).addClass('ui-component-wrap'),html=[];
			$.each(g.selectedData,function(i,data){
				html.push(g.parseSelectedDataHtml(data));
			});
			div.html(html.join(''));
		},
		parseSelectedDataHtml:function(data){
			var g=this,inputParams=g.inputParams;
			var showSendMessage =inputParams.showSendMessageColumn==1;
			var iconUrl=OpmUtil.getOrgImgUrl(data.orgKindId,data.status);
			var html=['<a class="ui-component-item" href="javascript:void(0);" data-id="',data['id'],'">'];
			html.push('<img class="icon-img" src="',iconUrl,'"/>');
			html.push('<span class="ui-component-text" title="',data['fullName'],'">',data['name'],'</span>');
			if(showSendMessage){
				html.push('<span class="check-group">');
				html.push('<input type="checkbox" value="1" data-id="',data['id'],'"/>&nbsp;','<i class="fa fa-paper-plane-o"></i>');
				html.push('</span>');
			}
			html.push('<i class="icon-close"></i>');
			html.push('</a>');
			return html.join('');
		},
		addDataOneNode:function(data){
			var g=this,inputParams=g.inputParams;
		    if (inputParams.selectableOrgKinds.indexOf(data.orgKindId) == -1) {
		        return true;
		    }
		    if (inputParams.multiSelect == 0 && g.selectedData.length > 0) {
		    	//当前业务不能选择多条组织数据。
		        Public.errorTip("common.selectorg.warning.moreorg");
		        return false;
		    }
		    var added = false;
		    for (var j = 0; j < g.selectedData.length; j++) {
		        if (g.selectedData[j].id == data.id) {
		            added = true;
		            break;
		        }
		    }
		    if (!added) {
		        var org = {};
		        for( var key in data ){
		        	org[key] = data[key];
		        }
		        g.selectedData[g.selectedData.length] = org;
		        $("div.handlerGrid",g.div).append(g.parseSelectedDataHtml(org));
		    }
		    g.cancelSelect(data);
		    return true;
		},
		addData:function(rows){
			rows = rows||this.getChooseRowData();
		    if (!rows || rows.length < 1) {
		    	//请选择组织节点!
		        Public.tip('common.warning.org.empty');
		        return false;
		    }
		    for (var i = 0; i < rows.length; i++) {
		        if(!this.addDataOneNode(rows[i])){
		        	return false;
		        }
		    }
		},
		deleteData:function(){
			var g=this;
			//您确定清空已选择的人员吗？
			UICtrl.confirm('common.confirm.clean.org', function () {
				g.selectedData=[];//清空已选
				$("div.handlerGrid",g.div).empty();
			});
		},
		deleteOneNode:function(data){
			 for (var j = 0; j < this.selectedData.length; j++) {
				 if (this.selectedData[j].id == data.id) {
					 this.selectedData.splice(j, 1);
			         break;
			     }
			 }
		},
		getSendMessageFlag:function(id){
			var sendMessage=0;
			$("div.handlerGrid",this.div).find('input').each(function(){
				if($(this).data('id')==id){
					sendMessage=$(this).is(':checked')?1:0;
					return false;
				}
			});
			return sendMessage;
		},
		getSelectedData:function(){
			var g=this,inputParams=g.inputParams,_data=[];
			var showSendMessage =inputParams.showSendMessageColumn==1,sendMessage=0;
			$.each(this.selectedData,function(i,o){
				sendMessage=0;
				if(showSendMessage){
					sendMessage=g.getSendMessageFlag(o.id);
				}
				_data.push($.extend({},o,{sendMessage:sendMessage}));
			});
			return _data;
		}
	});

})(jQuery);
