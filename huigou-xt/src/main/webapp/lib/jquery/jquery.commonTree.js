/*---------------------------------------------------------------------------*\
 |  title:         通用文件夹树                                                                                                                                                         |
 |  Author:        xx                                                         |
 |  Created:       2014-01-24                                                 |
 |  Description:   公用树操作包含树节点编辑等操作                                                                                                                     |
 \*--------------------------------------------------------------------------*/
(function ($) {
    $.fn.commonTree = function (op) {
        var obj = this.data('ui-common-tree');
        if (!obj) {
            new CommonTree(this, op);
        } else {
            if (typeof op == "string") {
                var _self = this, value= null, args = arguments;
                var _ms=['refresh','refreshParent','reload'];
                _ms.push('expandById','updateNode','updateNodeName');
                _ms.push('getSelectedId', 'getSelected','getChecked','cancelSelect','selectNode');
                $.each(_ms, function (i, m) {
                    if (op == m) {
                        args = Array.prototype.slice.call(args, 1);
                        value = obj[m].apply(obj, Array.prototype.concat.call([_self], args));
                        return false;
                    }
                });
                return value;
            } else {
                obj.set(op);
            }
        }
        return this;
    };
    //公用树类定义
    function CommonTree(el, op) {
        this.options = {};
        this.treeManager = null;//树结构管理对象
        this.treeMenu = null;//菜单管理对象
        this.id = null;//当前选中节点ID
        this.kindId = null;//当前选中节点类别ID
        this.set(op);
        this.init(el);
        $(el).data('ui-common-tree', this);
    }

    $.extend(CommonTree.prototype, {
        set: function (op) {
            this.options = $.extend({
                loadTreesAction: 'commonTree/queryCommonTrees.ajax',
                addFolderAction: 'commonTree/forwardCommonTreeDetail.load',
                appendAction: 'commonTree/insertCommonTree.ajax',
                loadNodeAction: 'commonTree/loadCommonTree.load',
                updateAction: 'commonTree/updateCommonTree.ajax',
                deleteAction: 'commonTree/deleteCommonTree.ajax',
                updateSequenceAction: 'commonTree/updateCommonTreeSequence.ajax',
                dialogWidth:350,
                root: 'Rows',
                idFieldName: 'id',
                parentIDFieldName: 'parentId',
                textFieldName: "name",
                iconFieldName: "nodeIcon",
                sortnameParmName: 'sortname',        //页排序列名(提交给服务器)
                sortorderParmName: 'sortorder',      //页排序方向(提交给服务器)
                sortName: 'sequence',//排序列名
                sortOrder: 'asc',//排序方向
                manageType:null,//查询使用的管理权限类别
                nodeWidth: 180,
                nodeClassName:null,
                getParam: null,//动态获取参数方法
                kindId: 1,
                parentId: 0,
                onClick: null,
                onAfterAppend: null,
                changeNodeIcon: null,
                onbeforeShowMenu: null,
                onDelay:null,
                dataRender: null,
                isLeaf: null,
                menuName:'文件夹',
                IsShowMenu: true, //是否显示菜单
                formBar:true
            }, this.options, op || {});
        },
        init: function (el) {
            var opt = this.options, _self = this;
            var menuName=opt.menuName
            if (opt.IsShowMenu) {
                if (opt.formBar) {
	                var div=$('<div class="ui-form-bar dom-overflow"></div>').appendTo($(el).parent());
	                $('<div class="ui-hold-bar"></div>').insertAfter(div);
	        		var offset=$(el).offset();
	        		div.css({position:'absolute',textAlign:'center',zIndex:100});
	        		var html=['<div class="btn-group btn-group-sm">'];
	        		html.push('<button data-kind="add" title="',$.i18nProp('common.button.add'),'" type="button" class="btn btn-gray"><i class="fa fa-plus"></i></button>'); 
	        		html.push('<button data-kind="update" title="',$.i18nProp('common.button.update'),'" type="button" class="btn btn-gray"><i class="fa fa-edit"></i></button>'); 
	        		html.push('<button data-kind="delete" title="',$.i18nProp('common.button.delete'),'" type="button" class="btn btn-gray"><i class="fa fa-trash-o"></i></button>'); 
	        		html.push('<button data-kind="moveUp" title="',$.i18nProp('common.button.move.up'),'" type="button" class="btn btn-gray"><i class="fa fa-arrow-circle-up"></i></button>'); 
	        		html.push('<button data-kind="moveDown" title="',$.i18nProp('common.button.move.down'),'" type="button" class="btn btn-gray"><i class="fa fa-arrow-circle-down"></i></button>');
	        		html.push('<button data-kind="refresh" title="',$.i18nProp('common.button.refresh'),'" type="button" class="btn btn-gray"><i class="fa fa-repeat"></i></button>');
	        		html.push('</div>');
	        		div.html(html.join(''));
	        		div.on('click',function(e){
	        			var $clicked = $(e.target || e.srcElement);
	        			if($clicked.hasClass('fa')){
	        				$clicked=$clicked.parent();
	        			}
	        			if($clicked.is('button')){
	        				var _kind=$clicked.data('kind'),_eventName=_kind+'BarEvent';
	        				var node = _self.treeManager.getSelected();
	        				if($.isFunction(opt[_eventName])){
	        					opt[_eventName].call(_self,node);
	        					return;node
	        				}
	        				if(_kind=='refresh'){
	        					if(node){
	        						_self._refreshNode(node.data[opt.idFieldName]);
	        					}else{
	        						_self.reload();
	        					}
	        					return;
	        				}
	        				if (!node) {
	        					//新增根节点
	        					if(_kind=='add'){
	        						_self.id=opt.parentId;
	        						_self._addFolder();
		        					return;
	        					}else{
	        						Public.tip('common.warning.not.nodetree');
		        					return false;
	        					}
	        				}
	                        _self.id = node.data[opt.idFieldName];
	                        _self.kindId = node.data.kindId;
	        				if(_kind=='add'){
	        					 _self._addFolder();
	        					 return;
	        				}else if(_kind=='update'){
	        					 _self._updateFolder();
	        					 return;
	        				}else if(_kind=='delete'){
	        					_self._deleteFolder();
	        					return;
	        				}else if(_kind=='moveDown'){
	        					if(!_self._checkMoveFolderByClassName(node,'down')){
	        						return false;
	        					}
	        					_self._moveFolder(false);
	        					return;
	        				}else if(_kind=='moveUp'){
	        					if(!_self._checkMoveFolderByClassName(node,'up')){
	        						return false;
	        					}
	        					_self._moveFolder(true);
	        					return;
	        				}
	        			}
	        		});
                }
            }
            this.kindId = opt.kindId;
            var param = {kindId: opt.kindId};
            param[opt.parentIDFieldName]=opt.parentId;
            param[opt.sortnameParmName] = opt.sortName;
            param[opt.sortorderParmName] = opt.sortOrder;
            if(opt.manageType){
            	param[Public.manageTypeParmName] = opt.manageType;
            }
            if ($.isFunction(opt.getParam)) {
                var p = opt.getParam.call(this);
                param = $.extend(param, p || {});
            }
            
            _self.treeManager = UICtrl.tree(el, {
                url: web_app.name + '/' + opt.loadTreesAction,
                param: param,
                idFieldName: opt.idFieldName,
                parentIDFieldName: opt.parentIDFieldName,
                textFieldName: opt.textFieldName,
                iconFieldName: opt.iconFieldName,
                autoCheckboxEven:opt.autoCheckboxEven||false,
                nodeWidth: opt.nodeWidth,
                checkbox:opt.checkbox||false,
                nodeClassName:opt.nodeClassName,
                dataRender: function (data) {
                    if ($.isFunction(opt.dataRender)) {
                        return opt.dataRender.call(_self, data);
                    }
                    return data[opt.root]||data;
                },
                isLeaf: function (data) {
                    if ($.isFunction(opt.changeNodeIcon)) {
                        opt.changeNodeIcon.call(_self, data);
                    }
                    if ($.isFunction(opt.isLeaf)) {
                        return opt.isLeaf.call(_self, data);
                    }
                    if(!$.isArray(data.children)){
                    	data.children = [];
                    }
                    return parseInt(data.hasChildren) == 0;
                },
                onSelect: function (node, obj) {
                    //if (!$(obj).is('span')&&!$(obj).hasClass('l-checkbox')) return;
                    if (node.data[opt.parentIDFieldName] == -1) {
                        _self.id = 0;
                    } else {
                        _self.id = node.data[opt.idFieldName];
                    }
                    if ($.isFunction(opt.onClick)) {
                        opt.onClick.call(_self, node.data, _self.id, obj);
                    }
                },
                onContextmenu: function (node, e) {
                    return _self._onContextmenu.call(_self, node, e);
                },
                delay: function (e) {
                	if($.isFunction(opt.onDelay)){
                		return opt.onDelay.call(this,e,opt);
                	}
                    var param = {};
                    param[opt.parentIDFieldName]=e.data[opt.idFieldName];
                    param[opt.sortnameParmName] = opt.sortName;
                    param[opt.sortorderParmName] = opt.sortOrder;
                    if(opt.manageType){
                    	param[Public.manageTypeParmName] = opt.manageType;
                    }
                    if ($.isFunction(opt.getParam)) {
                        var p = opt.getParam.call(this, e.data);
                        param = $.extend(param, p || {});
                    }
                    return { url: web_app.name + '/' + opt.loadTreesAction, parms: param};
                }
            });
        },
        _checkMoveFolderByClassName:function(node,button){
        	if (node.target.className.indexOf('l-onlychild') >= 0) {
               return false;
            } else if (node.target.className.indexOf('l-first') >= 0) {
            	if(button=='up'){
            		return false;
            	}
            } else if (node.target.className.indexOf('l-last') >= 0) {
            	if(button=='down'){
            		return false;
            	}
            } 
        	return true;
        },
        /*显示菜单*/
        _onContextmenu: function (node, e) {
            var opt = this.options;
            if (!opt.IsShowMenu) {
                return false;
            }
            if ($.isFunction(opt.onbeforeShowMenu)) {
                if (opt.onbeforeShowMenu.call(this, node) === false) {
                    return false;
                }
            }
            if (!this.treeMenu) {
            	return false;
            }
            if (node.data.nodeUrl && node.data.nodeUrl == 'Folder') {
                this.id = node.data[opt.idFieldName];
                this.kindId = node.data.kindId;
                if (node.target.className == "l-first l-last l-onlychild ") {
                    this.treeMenu.setEnable("menuUp");
                    this.treeMenu.setDisable("menuDown");
                }
                if (node.target.className.indexOf('l-onlychild') >= 0) {
                    this.treeMenu.setDisable("menuUp");
                    this.treeMenu.setDisable("menuDown");
                } else if (node.target.className.indexOf('l-first') >= 0) {
                    this.treeMenu.setDisable("menuUp");
                    this.treeMenu.setEnable("menuDown");
                } else if (node.target.className.indexOf('l-last') >= 0) {
                    this.treeMenu.setEnable("menuUp");
                    this.treeMenu.setDisable("menuDown");
                } else {
                    this.treeMenu.setEnable("menuUp");
                    this.treeMenu.setEnable("menuDown");
                }
                this.treeMenu.show({top: e.pageY, left: e.pageX});
                var oldNode = this.treeManager.getSelected();
                if (oldNode) {
                    this.treeManager.cancelSelect(oldNode.target);
                }
                this.treeManager.selectNode(node.target);
            }
            return false;
        },
        /*新增节点*/
        _addFolder: function (callBack) {
            var opt = this.options, _self = this;
            var menuName=opt.menuName
            UICtrl.showAjaxDialog({
                url: web_app.name + "/" + opt.addFolderAction,
                param: {parentId: _self.id},
                title: menuName,
                width: opt.dialogWidth,
                ok: function (doc) {
                    var dialog = this;
                    $('form:first', doc).ajaxSubmit({url: web_app.name + '/' + opt.appendAction,
                        param: {parentId: _self.id, kindId: _self.kindId},
                        success: function (data) {
                        	dialog.close();
                        	if($.isFunction(callBack)){
                        		callBack.call(window);
                        	}else{
                        		if( _self.id==opt.parentId){
                            		_self.reload();
                            	}else{
                            		_self._refreshNode();
                            	}
                        	}
                        }
                    });
                }
            });
        },
        /*编辑节点*/
        _updateFolder: function () {
            var opt = this.options, _self = this;
            var menuName=opt.menuName;
            UICtrl.showAjaxDialog({
                url: web_app.name + "/" + opt.loadNodeAction,
                param: {id: _self.id},
                title: menuName,
                width: opt.dialogWidth,
                ok: function (doc) {
                    var dialog = this;
                    $('form:first', doc).ajaxSubmit({url: web_app.name + '/' + opt.updateAction,
                        param: {id: _self.id},
                        success: function (data) {
                            var name=$('input[name="'+opt.textFieldName+'"]',doc).val();
                            var node = _self.treeManager.getSelected();
                            if(node){
                            	 var newnodedata={};
                                 newnodedata[opt.textFieldName]=name;
                                 _self.treeManager.update(node.data,newnodedata);
                            }
                            dialog.close();
                        }
                    });
                }
            });
        },
        /*删除节点*/
        _deleteFolder: function () {
            var opt = this.options, _self = this;
            UICtrl.confirm('common.confirm.delete.unrecoverable', function () {
                Public.ajax(web_app.name + '/' + opt.deleteAction, {id: _self.id, kindId: _self.kindId}, function () {
                	_self.id = null;
                    var node = _self.treeManager.getSelected();
                    if (node) {
                    	var parentId=node.data[opt.parentIDFieldName];
                        if(parentId==opt.parentId){//删除的是根节点
                        	_self.reload();
                        }else{
                        	_self._refreshNode(parentId);
                        }
                    }
                });
            });
        },
        /*刷新节点*/
        _refreshNode: function (parentId) {
            var opt = this.options, _self = this;
            parentId=parentId||_self.id;
            var parentData =_self.treeManager.getDataByID(parentId);
            if (parentData) {
                var param = {kindId: _self.kindId, parentId: parentData[opt.idFieldName]};
                param[opt.sortnameParmName] = opt.sortName;
                param[opt.sortorderParmName] = opt.sortOrder;
                if ($.isFunction(opt.getParam)) {
                	var p = opt.getParam.call(this, parentData);
                    param = $.extend(param, p || {});
                }
                _self.treeManager.loadData(parentData, web_app.name + '/' + opt.loadTreesAction, param,
                	{
                		beforeAppend:function(){
                			 _self.treeManager.clear(parentData);
                             var parentNode = _self.treeManager.getNodeDom(parentData);
                             _self.treeManager.demotion(parentNode);
                             _self.treeManager._removeToggleNodeCallback(parentData);
                             return parentData;
                		}
	                }
                );
            }
        },
        /*移动节点*/
        _moveFolder: function (flag) {
            var opt = this.options, _self = this,node=null;
            var params = new PublicMap(), moveNode;
            if (_self.treeManager) {
                node = _self.treeManager.getSelected();
                if (node) {
                    if (flag) {
                        moveNode = _self.treeManager.getNodeData($(node.target).prev());
                    } else {
                        moveNode = _self.treeManager.getNodeData($(node.target).next());
                    }
                    if (moveNode) {
                        params.put(node.data.id, moveNode.sequence);
                        params.put(moveNode.id, node.data.sequence);
                    }
                }
            }
            if (params.isEmpty()) {
                return;
            }
            Public.ajax(web_app.name + '/' + opt.updateSequenceAction, {data: params.toString()}, function () {
                var parentId=node.data[opt.parentIDFieldName];
                if(parentId==opt.parentId){
                	_self.reload();
                }else{
                	_self._refreshNode(parentId);
                }
            });
        },
        refresh: function (el, id) {
            if(id){
                if(id==this.options.parentId){//删除的是根节点
                	this.reload();
                }else{
                	this._refreshNode(id);
                }
            }else{
            	this.reload();
            }
        },
        getSelectedId: function () {
            var node = this.treeManager.getSelected();
            if (node) {
                return node.data[this.options.idFieldName];
            }
            return null;
        },
        getSelected: function () {
            var node = this.treeManager.getSelected();
            if (node) {
                return node.data;
            }
            return null;
        },
        cancelSelect:function(node){
        	this.treeManager.cancelSelect(node);
        },
        getChecked:function(){
        	return this.treeManager.getChecked();
        },
        getNodeById:function(id){
        	var node = this.treeManager.getDataByID(id);
            return node;
        },
        refreshParent:function(el,id){
        	var node=this.getNodeById(id);
        	if(!node) return;
            this._refreshNode(node[this.options.parentIDFieldName]);
        },
        reload:function(){
        	var g = this.treeManager, p = this.options; 
            g.clear();
            g.loadData(null, web_app.name + '/' + p.loadTreesAction);
        },
        expandById:function(el,id){
        	this.treeManager.selectNode(id);
        	var node = this.treeManager.getSelected();
        	if (node) {
                var treeitembtn = $("div.l-body:first", $(node.target)).find("div.l-expandable-open:first,div.l-expandable-close:first");
                if (treeitembtn.hasClass('l-expandable-close')) {
                    treeitembtn.click();
                }
                return true;
            }
        	return false;
        },
        updateNodeName:function(el,id,nodeName){
        	var node=this.getNodeById(id);
        	if(!node) return;
        	var opt = this.options, newnodedata={};
            newnodedata[opt.textFieldName]=nodeName;
            this.treeManager.update(node,newnodedata);
        },
        updateNode:function(el,id,newNode){
        	var node=this.getNodeById(id);
        	if(!node) return;
        	var newnodedata=$.extend({},node,newNode);
            this.treeManager.update(node,newnodedata);
        },
        selectNode:function(el,id){
        	var node=this.getNodeById(id);
        	if(!node) return;
        	this.treeManager.selectNode(id);
        }
    });
})(jQuery);
/* 文件夹类型 */
var CommonTreeKind = CommonTreeKind || {};
/* 机构 */
CommonTreeKind.OrgType = 1;
/* 部门 */
CommonTreeKind.DptType = 2;
/* 岗位 */
CommonTreeKind.PosType = 3;
/* 角色 */
CommonTreeKind.Role = 4;
/* 扩展属性定义 */
CommonTreeKind.FlexFieldDefine = 5;
/* 扩展属性分组 */
CommonTreeKind.FlexFieldGroup = 6;
/* 系统参数 */
CommonTreeKind.Parameter = 7;
/* 单据编号 */
CommonTreeKind.SerialNumber = 8;
/* 系统字典 */
CommonTreeKind.Dictionary = 9;
/*数据导入模板分类*/
CommonTreeKind.ExpTemplet = 10;
/*消息提醒分类*/
CommonTreeKind.MessageRemind=15;
/*权限字段分类*/
CommonTreeKind.UIElement=16;
/*基础管理权限分类*/
CommonTreeKind.BaseManagementType=17;
/*业务管理权限分类*/
CommonTreeKind.BizManagementType=18;
/*附件配置分类*/
CommonTreeKind.AttachmentConfig=21;
/*任务类别分类*/
CommonTreeKind.TaskKind=22;
/*系统用户分组设置*/
CommonTreeKind.UserGroupKind = 25;
/*用户自定义分组设置*/
CommonTreeKind.UserCustomGroup=26;
/*审批驳回理由*/
CommonTreeKind.ApprovalRejectedReason=50;
/*国际化资源*/
CommonTreeKind.i18nProperties=55;
