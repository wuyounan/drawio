/*---------------------------------------------------------------------------*\
|  title:         通用选择对话框                                                                                                                                                        |
|  Author:        xx                                                          |
|  Created:       2014-01-28                                                  |
|  Description:   集成通用树及查询列表控件，支持单选及多选                                                                                                    |
\*---------------------------------------------------------------------------*/
(function($) {
	
	$.fn.comboDialog = function(op){
		var obj=this.data('ui-combo-dialog');
		if(!obj){
			new ComboDialog(this,op);
		}else{
			if (typeof op == "string") {
				var _self=this,value,args = arguments;
				$.each(['enable','disable','getSelectedRows','getSelectedTreeNode'],function(i,m){
					if(op==m){
						args = Array.prototype.slice.call(args, 1);
						value=obj[m].apply(obj,args);
						return false;
					}
				});
				return value;
			}else{
				obj.set(op);
			}
		}
		return this;
	};
	
	//通用选择对话框类定义
	function ComboDialog(el,op){
		this.options={};
		this.gridManager=null;//查询列表管理对象
		this.dialogManager=null;//对话框管理对象
		this.treeManager=null;//树结构管理对象
		this.gridSelectedManager = null;//已选择表格对象
		this.kindId = null;//当前选中节点类别ID
		this.folderId='';//当前选中树节点ID
		this.columns=null;//从配置文件中读取的查询表头
		this.triggerEventData=null;
		this.disabled = false;
		this.columnWidth=0;//宽度
		this.element=$(el);
		//利用onCheckRow将选中的行记忆下来，并利用isChecked将记忆下来的行初始化选中
		this.checkedCustomer=new PublicMap();
		this.set(op);
		this.init();
		this.element.data('ui-combo-dialog',this);
	}
	ComboDialog.getDialogContent=function(){
		var html=["<div class='combo-dialog-main-div container-fluid'>"];
		html.push("    <div class='combo-dialog-center'>");
		html.push("        <div class='blank_div clearfix'></div>");
		html.push("        <div class='row-fluid hg-form'>");
		html.push("            <div class='col-sm-3 hidden-xs'>");
		html.push("                <label class='hg-form-label'>&nbsp;",$.i18nProp('common.field.keyvalue'),"&nbsp;:</label>");
		html.push("             </div>");
		html.push("            <div class='col-xs-8 col-sm-6'>");
		html.push("              <div class='input-group'>");
		html.push("                <input type='text' name='name' maxlength='30' value='' class='text keyValue' />");
		html.push("                <span class='input-group-btn'>");
		html.push("                     <button type='button' class='btn btn-default combo-dialog-clear' style='padding:6px;'><i class='fa fa-times-circle'></i></button>");
		html.push("                </span>");
		html.push("              </div>");
		html.push("             </div>");
		html.push("            <div class='col-xs-4 col-sm-3'>");
		html.push("                <button type='button' class='btn btn-primary combo-dialog-search'><i class='fa fa-search'></i>&nbsp;",$.i18nProp('common.button.query'),"</button>");
		html.push("             </div>");
		html.push("         </div>");
		html.push("         <div class='blank_div clearfix'></div>");
		html.push("    	    <div class='comboChooseGrid'></div>");
		html.push("    </div>");
		html.push("</div>"); 
		return html.join('');
	};
	$.extend(ComboDialog.prototype, {
		set:function(op){
			this.options=$.extend({
				url:web_app.name+'/easySearch/getComboDialogConfig.ajax',//查询配置URL
				gridUrl:web_app.name+'/easySearch/comboGridSearch.ajax',//列表查询URL
				type:'',//类别
				name:'',//名称
				width:null, 
				height:null,
				leftWidth:null,
				rightWidth:null,
				lock:true,
				parent:null,
				getParam  : null,//参数取值函数
				columnRender:{},
				manageType:null,
				dataIndex:'id',
				triggerEvent : 'click.comboDialog',//显示触发方法
				checkbox:false,//单选OR多选
				showSelected:false,//多选时是否显示已选
				title:'common.dialog.choose',//标题
				onChoose:function(){},//确定按钮
				onClose:function(){},//取消按钮
				onInit:null,//初始化时执行
				onShow:null,//打开对话框前执行
				treeOptions:{},//树参数
				gridOptions:{}//列表参数
			},this.options, op||{});
		},
		init:function(){
			var opt=this.options,_self=this;
			this.element.on(opt.triggerEvent,function(e,data){
				if(_self.disabled) return false;
				if($.isFunction(opt.onShow)){
					if(opt.onShow.call(window,e,data)===false){
						return false;
					}
				}
				_self.triggerEventData=data;
				_self.show();
				return false;
			});
		},
		initTree:function(doc){
			if(!this.kindId) return;
			var opt=this.options,_self=this,layoutDiv=$('div.combo-dialog-main-div',doc);
			var treeOpts={};
			if(this.kindId=='org'){
				treeOpts={
					loadTreesAction:'org/queryOrgs.ajax',
					parentId :'orgRoot',
					getParam : function(e){
						if(e){
							//return {showDisabledOrg:0,showPosition:0,displayableOrgKinds : "ogn,dpt"};
							return {showDisabledOrg:0,displayableOrgKinds : "ogn,dpt,pos"};
						}
						return {showDisabledOrg:0};
					},
					changeNodeIcon:function(data){
						data[this.options.iconFieldName]= OpmUtil.getOrgImgUrl(data.orgKindId, data.status);
					},
					IsShowMenu:false,
					onClick : function(data){
						var html=[],fullId='',fullName='';
						if(!data){
							html.push($.i18nProp('common.field.list.title',''));
						}else{
							fullId=data.fullId,fullName=data.fullName;
							html.push('<span class="tomato-color">[',fullName,']</span>',$.i18nProp('common.field.list.title',''));
						}
						layoutDiv.layout('setCenterTitle',html.join(''));
						if (gridManager&&fullId!='') {
							_self.folderId=fullId;
							UICtrl.gridSearch(_self.gridManager, {folderId:fullId});
						}else{
							_self.folderId='';
							_self.gridManager.options.parms['fullId']='';
						}
					}
				};
			}else{
				treeOpts={
					kindId : _self.kindId,
					IsShowMenu : false,
					onClick:function(data,folderId){
						if(_self.kindId==folderId) folderId='';
						layoutDiv.layout('setCenterTitle',"<span class='tomato-color'>["+ data.name + "]</span>"+ $.i18nProp('common.field.list.title',''));
						_self.folderId=folderId;
						UICtrl.gridSearch(_self.gridManager, {folderId:folderId});
					}
				};
			}
			var treeOptions=opt.treeOptions||{};
			if($.isFunction(treeOptions)){
				treeOptions=treeOptions.call(_self,doc);
			}
			treeOpts=$.extend(treeOpts,treeOptions);
			if(opt.manageType){//加入管理权限
				treeOpts['manageType']=opt.manageType;
			}
			this.treeManager=$('ul.comboChooseTree',doc).commonTree(treeOpts);
		},
		initGrid:function(doc){
			var opt=this.options,_self=this,columns=[];
			this.columnWidth=0;
			$.each(_self.columns, function(i,o){
				var width=parseInt(o.width),type=o.type,align=o.align;
				if(type=='hidden') return;
				if(type=='text') type='string';
				_self.columnWidth+=(isNaN(width)?100:width);
				columns.push({ display:o.name, name:o.code, width:isNaN(width)?100:width, minWidth: 60,render:opt.columnRender[o.code]||null,type:type==''?'string':type, align:align==''?'left':align});
			});
			var param={configType:opt.type,queryName:opt.name,folderId:''};
			if(opt.manageType){//加入管理权限
				param[Public.manageTypeParmName]=opt.manageType;
			}
			if($.isFunction(opt.getParam)){
				var p=opt.getParam.call(window);
				param=$.extend({},p||{},param);
			}
			var gridOptions=opt.gridOptions||{};
			if($.isFunction(gridOptions)){
				gridOptions=gridOptions.call(this,doc);
			}
			var _height=$(doc).height()-65;
			if(_self.kindId||(opt.checkbox&&opt.showSelected)){//存在布局的配置
				_height=$(doc).height()-95;
			}
			var gridOpts=$.extend({
				dataAction : 'server',
				columns:columns,
				pageSize : 10,
				width :'100%',
				height :_height,
				isChecked: function(data){
					if (typeof _self.findCheckedCustomer(data) == 'undefined')
		                return false;
		            return true;
				}, 
				onCheckRow: function(checked,data){
					 if (checked){
						 _self.addCheckedCustomer(data);
					 }else{
						 _self.removeCheckedCustomer(data);
					 }
					 _self.reloadGridSelectedData();
				},
				onCheckAllRow: function(checked){
					for (var rowid in this.records){
		                if(checked){
		                	_self.addCheckedCustomer(this.records[rowid]);
		                }else{
		                	_self.removeCheckedCustomer(this.records[rowid]);
		                }
		            }
					_self.reloadGridSelectedData();
				}
			},gridOptions,{
				url:opt.gridUrl,
				checkbox:opt.checkbox,
				parms:param
			});
			var gridDiv=$('div.comboChooseGrid',doc);
			this.gridManager = UICtrl.grid(gridDiv,gridOpts);
			gridDiv.find('span.l-bar-text').hide();
			//已选择表格
			this.initSelectedGrid(doc,columns);
		},
		initSelectedGrid:function(doc,columns){//已选择表格
			var _self=this,_opt=this.options;
			if(!_opt.checkbox||!_opt.showSelected){
				return;
			}
			var toolbarOptions = UICtrl.getDefaultToolbarOptions({ 
				deleteHandler: function(){
					_removeSelected(_self.gridSelectedManager.getSelectedRows());
				},
				cleanSelecteds:{id:'cleanSelecteds',text:'common.button.clean',img:'fa-close',click:function(){
					_removeSelected(_self.gridSelectedManager.getData());
				}} 
			});
			//避免应用同一对象
			var _columns=$.map(columns,function(c){return $.extend({},c);});
			var gridDiv=$('div.comboSelectedGrid',doc);
			var _height=$(doc).height()-88;
			this.gridSelectedManager = UICtrl.grid(gridDiv,{
				dataAction : 'local',
				columns:_columns,
				usePager:false,
				width :'100%',
				height :_height,
				checkbox:true,
				toolbar: toolbarOptions
			});
			gridDiv.find('span.l-bar-text').hide();
			var _removeSelected=function(datas){
				if (!datas || datas.length < 1) {
			        return false;
			    }
				var dataIndexs=$.map(datas,function(o){
			    	_self.removeCheckedCustomer(o);
			    	return o[_opt.dataIndex];
			    });
				//取消查询表格中的已选中的样式
				$.each(_self.gridManager.getData(),function(i,o){
					if($.inArray(o[_opt.dataIndex],dataIndexs)>=0){
						_self.gridManager.unselect(o);
					}
				});
				_self.reloadGridSelectedData();
			};
		},
		addCheckedCustomer:function(data){
			var dataIndex=this.options.dataIndex;
			var dataIndexValue=data[dataIndex]||data['myRownum'];
			this.checkedCustomer.put(dataIndexValue,data);
		},
		removeCheckedCustomer:function(data){
			var dataIndex=this.options.dataIndex;
			var dataIndexValue=data[dataIndex]||data['myRownum'];
			this.checkedCustomer.remove(dataIndexValue);
		},
		reloadGridSelectedData:function(){
			if(this.gridSelectedManager){
				var datas=$.map(this.checkedCustomer.values(),function(d){
					return $.extend({},d,{ "__id": "","__previd": "","__index": "","__status": "", "__nextid": ""});
				});
				this.gridSelectedManager.setData({Rows: datas});
				this.gridSelectedManager.reload();
			}
		},
		findCheckedCustomer:function(data){
			var dataIndex=this.options.dataIndex;
			var dataIndexValue=data[dataIndex]||data['myRownum'];
			return this.checkedCustomer.get(dataIndexValue);
        },
		initLayout:function(doc){
			var _self=this,_opt=this.options,layoutDiv=$('div.combo-dialog-main-div',doc);
			var center=$('div.combo-dialog-center',layoutDiv),_needLayout=false;
			if(_self.kindId){//存在树配置
				$('<div position="left" title="'+$.i18nProp('common.field.kind.title','')+'"><ul class="comboChooseTree"></ul></div>').insertBefore(center);
				_needLayout=true;//需要布局
			}
			//多选时显示已选择
			if(_opt.checkbox&&_opt.showSelected){
				$('<div position="right" title="'+$.i18nProp('common.field.kind.selected','')+'"><div class="blank_div clearfix"></div><div class="comboSelectedGrid"></div></div>').insertAfter(center);
				_needLayout=true;//需要布局
			}
			if(_needLayout){
				center.attr('position','center').attr('title',$.i18nProp('common.field.list.title',''));
				layoutDiv.layout({leftWidth:this.options.leftWidth,rightWidth:this.options.rightWidth,heightDiff:-7,onSizeChanged:function(){
					UICtrl.onGridResize(_self.gridManager);
				}});
			}
		},
		initButton:function(doc){
			var input=$('input.keyValue',doc),_self=this;
			input.bind('keyup.queryForm',function(e){
				var k =e.charCode||e.keyCode||e.which;
				if(k==13){//回车
					UICtrl.gridSearch(_self.gridManager, {folderId:_self.folderId,paramValue:encodeURI(input.val())});
				}
			});
			$('.combo-dialog-search',doc).on('click',function(){
				UICtrl.gridSearch(_self.gridManager, {folderId:_self.folderId,paramValue:encodeURI(input.val())});
			});
			$('.combo-dialog-clear',doc).on('click',function(){
				input.val('');
				UICtrl.gridSearch(_self.gridManager, {folderId:_self.folderId,paramValue:''});
			});
			
		},
		show:function(){
			var _defWidth=420,_defHeight=435,_defLeftWidth=180,_defLeftCol=4,_defRightCol=5;
			var _self=this,opt=_self.options,_width=opt.width,_height=opt.height;
			var _leftCol=opt.leftWidth,_rightCol=opt.rightWidth;
			_self.folderId='';
			if(!this.columns){
				Public.ajax(opt.url,{configType:opt.type,queryName:opt.name},function(data){
					_self.kindId=data['kindId'];
					_self.columns=data['columns'];
					//处理弹出框大小
					if (Public.isBlank(_width)) {
						_width = _defWidth;
					}
					if (Public.isBlank(_height)) {
						_height = _defWidth;
					}
					//存在树结构
					if(_self.kindId){
						if (Public.isBlank(opt.width)) {
							_width=_width+_defLeftWidth;
						}
						if (Public.isBlank(opt.leftWidth)) {
							_leftCol=4;
						}
					}
					//多选时显示已选择
					if(opt.checkbox&&opt.showSelected){
						if (Public.isBlank(opt.width)) {
							_width=_width+_defWidth;
						}
						if (Public.isBlank(opt.leftWidth)) {
							_leftCol = _defLeftCol/2;
						}
						if (Public.isBlank(opt.rightWidth)) {
							_rightCol=_defRightCol;
						}
					}
					opt.width=_width;
					opt.height=_height;
					opt.leftWidth=_leftCol;
					opt.rightWidth=_rightCol;
					_show();
				});
			}else{
				_show();
			}
			function _show(){
				if(_self.columns.length==0){
					tips({type:1, content : 'no grid heard!'});
					return;
				}
				var dialogOptions=opt.dialogOptions||{};
				if($.isFunction(dialogOptions)){
					dialogOptions=dialogOptions.call(_self);
				}
				//小页面不显示分类树
				var parentWidth=getDefaultDialogWidth();
				if(parentWidth < 768){
					_self.kindId=null;
				}
				var dialogOpts=$.extend(dialogOptions,{
					content:ComboDialog.getDialogContent(),
					title:opt.title,
					parent:opt.parent,
					lock:opt.lock,
					width:_width,
					height:opt.height,
					ok:function(){
						if($.isFunction(opt.onChoose)){
							return opt.onChoose.call(_self,this);
						}
					},
					close:function(){
						if($.isFunction(opt.onClose)){
							return opt.onClose.call(_self,this);
						}
					},
					init:function(doc){
						_self.doc=doc;
						_self.checkedCustomer.clear();
						_self.initLayout(doc);
						_self.initTree(doc);
						_self.initGrid(doc);
						_self.initButton(doc);
						if($.isFunction(opt.onInit)){
							opt.onInit.call(_self,doc);
						}
						setTimeout(function(){
							$('input.keyValue',doc).focus();
						},0);
						this['gridManager']=_self.gridManager;
					}
				});
				_self.dialogManager=UICtrl.showDialog(dialogOpts);
				$.closePicker();
			}
		},
		getSelectedRows:function(){
			if(this.options.checkbox){
				return this.checkedCustomer.values();
			}
			return this.gridManager.getSelectedRows();
		},
		getSelectedRow:function(){
			return this.gridManager.getSelectedRow();
		},
		getSelectedTreeNode:function(){
			return this.treeManager.commonTree('getSelected');
		},
		closeDialog:function(){
			this.dialogManager.close();
		},
		enable: function() {
			this.disabled = false;
			this.element.removeAttr('disabled');
		},
		disable: function() {
			this.disabled = true;
			this.element.attr('disabled',true);
		}
	});
})(jQuery);