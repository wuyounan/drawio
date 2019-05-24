/** **************grid 扩展******************** */
GridEditorsUtil = {
    getSpinnerDataOptions: function(editor) { // 获取Spinner执行参数
        var datas = {},dataOptions = "";
        if (editor.min && !isNaN(parseInt(editor.min))) {
            datas['min'] = parseInt(editor.min);
        }
        if (editor.max && !isNaN(parseInt(editor.max))) {
            datas['max'] = parseInt(editor.max);
        }
        if (editor.defaultValue && !isNaN(parseInt(editor.defaultValue))) {
            datas['default_value'] = parseInt(editor.defaultValue);
        }
        if (!$.isEmptyObject(datas)) {
            dataOptions = [" dataOptions='", $.toJSON(datas), "'"].join('');
        }
        return dataOptions;
    },
    createRadioControl: function(dataSource, name, type) { // 构造radio或checkbox
        var html = ["<div class='textGrid'>"];
        var radioHtml = "<input type='" + type + "' id={id} name='{name}' value='{value}'/>&nbsp;<label for='{id}'>{label}</label>";
        if ($.isArray(dataSource)) {
            $.each(dataSource,
            function(i, o) {
                html.push(radioHtml.replace(/{id}/g, name + "_" + i).replace('{name}', name).replace('{value}', o).replace('{label}', o), '&nbsp;');
            });
        } else {
            $.each(dataSource,
            function(i, o) {
                html.push(radioHtml.replace(/{id}/g, name + "_" + i).replace('{name}', name).replace('{value}', i).replace('{label}', o), '&nbsp;');
            });
        }
        html.push("</div>");
        return html.join('');
    },
    createSelectControl: function(editor, inputHtml, isSelect) { // 获取select orlookup执行html
    	// groupId 存在代表 select 否则为lookup
        var html = [],dataOptions = $.extend({},editor.data);
        if (dataOptions.name && dataOptions.type) { // 存在 name 和 type 这两个参数
        	delete dataOptions['getParam'];
            if (isSelect) { // isSelect
                if (dataOptions.back) { // 输入形式入:type:'sys',name:'flexFieldDefine',back:{defineId:'003',fieldCname:'select'}
                    html.push('<div class="input-group ui-combox-wrap">');
                    html.push(inputHtml.replace('{dataOptions}', [" dataOptions='", $.toJSON(dataOptions), "'"].join('')));
                    html.push('<span class="input-group-btn"><button type="button" class="btn btn-primary">', '<i class="fa fa-caret-down"></i>', '</span></div>');
                }else{
                	return inputHtml.replace('{dataOptions}', '');
                }
            } else { // lookup
                html.push(inputHtml.replace('{dataOptions}', [" dataOptions='", $.toJSON(dataOptions), "'"].join('')));
            }
        } else {
            html.push(inputHtml.replace('{dataOptions}', ''));
        }
        return html.join('');
    },
    createInputHtml:function(editor,inputId){
    	var controlType = editor.type;
        var html = [];
        var inputHtml = "<input type='text' class='{class}' name='{name}' id='{id}' {required}{controlType}{maxLength}{style}{readOnly}{dataOptions}{mask}/>";
        //基本属性
        with(editor) {
            inputHtml = inputHtml.replace('{maxLength}', maxLength ? " maxLength='" + maxLength + "'": "");
            inputHtml = inputHtml.replace('{style}', style ? " style='" + style + "'": "");
            inputHtml = inputHtml.replace('{readOnly}', readOnly ? " readOnly='true'": "");
            inputHtml = inputHtml.replace('{required}', required ? " required='true' label='" + display + "'": "");
            inputHtml = inputHtml.replace('{mask}', mask ? " mask='" + mask + "'": "");
            inputHtml = inputHtml.replace('{id}', inputId);
            inputHtml = inputHtml.replace('{name}', name||'');  
        }
        // 处理控件类型显示
        with(GridEditorsUtil) {
            switch (controlType) {
            case 'string':
            case 'number':
            case 'text':
                // textbox
                html.push(inputHtml.replace('{class}', 'textGrid').replace('{controlType}', '').replace('{dataOptions}', ''));
                break;
            case 'dictionary':
            case 'tree':
            	var dataOptions = [" dataOptions='", $.toJSON(editor.data), "'"].join('');
                html.push(inputHtml.replace('{class}', 'textGrid').replace('{controlType}', " "+controlType+"='true'").replace('{dataOptions}', dataOptions));
                break;
            case 'combobox':
                // combobox
            	var dataOptions = "";
            	if(editor.data.url&&editor.data.mode){// 读取远程数据
            		dataOptions = [" dataOptions='", $.toJSON(editor.data), "'"].join('');
            	}else{
            		dataOptions = [" dataOptions='data:", $.toJSON(editor.data), "'"].join('');
            	}
                html.push(inputHtml.replace('{class}', 'textGrid').replace('{controlType}', " combo='true'").replace('{dataOptions}', dataOptions));
                break;
            case 'spinner':
                // spinner
                html.push(inputHtml.replace('{class}', 'textGrid').replace('{controlType}', " spinner='true'").replace('{dataOptions}', getSpinnerDataOptions(editor)));
                break;
            case 'date':
                // date
                html.push(inputHtml.replace('{class}', 'textGrid textDate').replace('{controlType}', " date='true'").replace('{dataOptions}', ''));
                break;
            case 'datetime':
            case 'dateTime':
                // dateTime
                html.push(inputHtml.replace('{class}', 'textGrid textDate').replace('{controlType}', " dateTime='true'").replace('{dataOptions}', ''));
                break;
            case 'radio':
                // radio
                html.push(createRadioControl(editor.data, inputId, 'radio'));
                break;
            case 'checkbox':
                // checkbox
                html.push(createRadioControl(editor.data, inputId, 'checkbox'));
                break;
            case 'select':
                // select
                html.push(createSelectControl(editor, inputHtml.replace('{class}', 'textGrid').replace('{controlType}', " select='true'"), true));
                break;
            case 'lookup':
                // lookup
                html.push(createSelectControl(editor, inputHtml.replace('{class}', 'textGrid').replace('{controlType}', " select='true'")));
                break;
            default:
                html.push(inputHtml.replace('{class}', 'textGrid').replace('{controlType}', 'textGrid').replace('{dataOptions}', ''));
                break;
            }
        }
        return html.join('');
    },
    beforeEdit:function(a,b){// 调整gird的滚动位置
    	var gridbody=this.gridbody;
        var gridWidth=gridbody.width()-20;
        var gridHeight=gridbody.height()-20;
        var sLeft=parseInt(gridbody.scrollLeft()),sTop=parseInt(gridbody.scrollTop());
        sLeft=isNaN(sLeft)?0:sLeft;
        sTop=isNaN(sTop)?0:sTop;
     	var position=$(b).position(),left=position.left,top=position.top;
     	var height=$(b).height(),width=$(b).width();
     	var isScrollLeft=true,isScrollTop=true;
     	// gridbody.get(0).scrollWidth
     	if(left>=0&&(left+width)<=gridWidth){
     		isScrollLeft=false;
     	}
     	if(isScrollLeft){
	     	this.isEditorScroll=true;
	     	if(left<0){
	     		sLeft=sLeft-Math.abs(left);
	     	}else {
	     		sLeft=sLeft+left+width-gridWidth;
	     	}
	 		sLeft=sLeft<0?0:sLeft;
	 		gridbody.scrollLeft(sLeft);
     	}
     	if(top>=0&&(top+height)<=gridHeight){
     		isScrollTop=false;
     	}
     	if(isScrollTop){
	     	this.isEditorScroll=true;
	     	if(top<0){
	     		sTop=sTop-Math.abs(top);
	     	}else {
	     		sTop=sTop+top+height-gridHeight;
	     	}
	     	sTop=sTop<0?0:sTop;
	 		gridbody.scrollTop(sTop);
     	}
    },
    getLookUpValue:function(editParm){
    	var editor=editParm['editor'];
    	var rowData=editParm.record;
    	var textField=editor['textField']||editParm.column.name;
    	var valueField=editor['valueField']||editParm.column.name;
    	if(!valueField) return {};
    	return {text:Public.isBlank(rowData[textField])?'':rowData[textField],value:Public.isBlank(rowData[valueField])?'':rowData[valueField]};
    },
    getBackData:function(editor){
    	var _back=editor.back;
    	if($.isPlainObject(_back)){
    		return _back;
    	}
    	_back=editor.data.back;
    	if($.isPlainObject(_back)){
    		return _back;
    	}
    	return null;
    },
    isLookUp:function(editor){
    	var isLookup= editor.isLookup===false?false:true;
    	var _back=GridEditorsUtil.getBackData(editor);
    	if($.isPlainObject(editor.back)){
			isLookup=false;
		}
    	return isLookup;
    }
};
/** *扩展GRID的编辑器** */
$.ligerDefaults =$.ligerDefaults||{};
$.ligerDefaults.Grid =$.ligerDefaults.Grid||{};
$.ligerDefaults.Grid.editors=$.ligerDefaults.Grid.editors||{};
$.each(['text','combobox','spinner','date','dateTime','radio','checkbox','select','tree','dictionary','lookup','dynamic'],function(i, editorName) {
    $.ligerDefaults.Grid.editors[editorName] = {
        create: function(container, editParm) {
        	var column = editParm.column,editor = column.editor;
            if($.isFunction(editor.getEditor)){
            	editor=editor.getEditor.call(window,editParm.record);
            	if(editor===false) return null;
            }
            editor = $.extend({
                maxLength: false,
                readOnly: false,
                style: false,
                required: false,
                mask: false,
                match:false,
                onAfterInit:null,// 初始化完成执行
                data: {},
                display:column.display,
                name:column.name
            },editor);
            editParm['editor']=editor;
            var _self = this,
            	rowindex = editParm.rowindex,
            	name = column.name,
                gridId = this.id,
                controlType = editor.type;
            	gridId = gridId.replace(/\|/, '_');
            	inputId = [gridId, '_', name, '_', rowindex],
            	input = $(GridEditorsUtil.createInputHtml(editor,inputId.join(''))); //创建输入框
            container.append(input);
            Public.autoInitializeUI(container);
            if($.isFunction(editor.onAfterInit)){
            	editor.onAfterInit.call(_self,container,editParm);
            }
            /** ***** */
            if(controlType=='combobox'||controlType=='select'||controlType=='lookup'||controlType=='tree'||controlType=='dictionary'){
            	var beforeChange=editor.beforeChange,beforeChangeFn=null;
            	if($.isFunction(beforeChange)){// 方法
            		beforeChangeFn=function(value){
            			return beforeChange.call(this,value,editParm);
            		};
            	}
        		var getParam=editor.data.getParam,fn=null;
        		if(getParam){// 存在获取参数的方法
        			if($.isFunction(getParam)){// 方法
            			fn=function(row){
            				return function(){
            					return getParam.call(window,row);
            				};
            			}(editParm.record);
            		}else if($.isPlainObject(getParam)){// json对象
            			fn=function(row){
            				return function(){
            					var param={};
            					$.each(getParam,function(p,o){
                					param[p]=row[o];
                				});
                				return param;
            				};
            			}(editParm.record);
            		}
        		}
        		var options={};
        		if($.isFunction(fn)){
        			options['getParam']=fn;
        		}
        		if($.isFunction(beforeChangeFn)){
        			options['beforeChange']=beforeChangeFn;
        		}
        		var _backData=GridEditorsUtil.getBackData(editor);
        		if($.isPlainObject(_backData)){
        			options['beforeChange']=function(value){
        				 if($.isFunction(beforeChangeFn)){
                			var flag=beforeChangeFn.call(window,value);
                			if(flag===false) return false;
                		 }
        				 $.each(_backData,function(p, o) {
                         	 _self._setValueByName(editParm.record,o,value[p]);
                             _self.updateCell(o, value[p], editParm.record);
                         });
        			};
        		}
        		// select 需要单独处理返回
                if (controlType == 'select') {
                    if(!$.isEmptyObject(options)){
                    	$(input.find('input:first')).combox(options);
                    }
                }else {
                	if(!$.isEmptyObject(options)){
                		$(input[0]).combox(options);
                	}
                }
        	}
            var _applyEditor=function(chooseRow,chooseColumn){
            	var obj=_self.getCellObj(chooseRow,chooseColumn);
	        	if(obj){
	        		_self.endEdit();
		        	_self._applyEditor(obj);
	        	}
            };
            var autoAddRow=_self.options.autoAddRow,autoAddRowByKeydown=_self.options.autoAddRowByKeydown;
            var autoApplyNextEditor=_self.options.autoApplyNextEditor;
        	if(Public.isBlank(autoAddRowByKeydown)) {autoAddRowByKeydown=false;}
        	if(Public.isBlank(autoApplyNextEditor)) {autoApplyNextEditor=true;}
            // 按键事件处理 _createEditor
            $("input", container).bind("keydown.gridEditors",function(e,flag) {
            	if (e.ctrlKey) {return;};
            	if(autoApplyNextEditor===false&&flag===true){
            		_self.endEdit();
            	}
		        if (e.keyCode === 13 || e.keyCode === 9||(flag===true&&autoApplyNextEditor)) {// Enter or Tab
		        	try{$.closeCombox();}catch(e){}// 2014-10-30 add 处理选择框不自动清空(记录错误数据)
		        	// 只寻找可以编辑的控件
		        	var editColumns=$.grep(_self.columns,function(n,i){
		        		return $.isPlainObject(n.editor);
		        	});
		        	var chooseColumn=null,chooseRow=editParm.record;
		        	var index=$.inArray(column,editColumns);// 当前索引
		        	if(index==-1) return;
		        	if (e.shiftKey) {// 寻找上一个
		        		if(index==0){// 下一行
			        		chooseColumn=editColumns[editColumns.length-1];
			        		chooseRow= $(_self.getRowObj(chooseRow['__id'])).prev();// 上一行
			                if (!chooseRow.length) return;
			                chooseRow=_self.getRow(chooseRow[0]);
			        	}else{
			        		chooseColumn=editColumns[index-1];
			        	}
		        	}else{// 寻找下一个
		        		if(index==editColumns.length-1){// 下一行
			        		chooseColumn=editColumns[0];
			        		chooseRow= $(_self.getRowObj(chooseRow['__id'])).next();// 下一行
			                if (!chooseRow.length){
			               	 	if(autoAddRow&&autoAddRowByKeydown){// 自动增加一行
			               	 		chooseRow=UICtrl.addGridRow(_self);
			               	 	}
			               	 	setTimeout(function(){// 重新执行选中下一行操作
			               	 		_applyEditor(chooseRow,chooseColumn);
			               	 	},10);
			               	 	return;
			                }
			                chooseRow=_self.getRow(chooseRow[0]);
			        	}else{
			        		chooseColumn=editColumns[index+1];
			        	}
		        	}
		        	setTimeout(function(){
		        		_applyEditor(chooseRow,chooseColumn);
		        	},10);
		        }
		        if(e.keyCode === 9){
		        	return Public.stopEvent(e);
		        }
		        e.stopPropagation();
		    });
            setTimeout(function() {// 控制光标
            	if (input.is('input.textDate') && input.is(':visible')) {
                    input.trigger('click.datepicker');
                }else if (input.is('input.textGrid') && input.is(':visible')) {
                    input.select();
                } else if(input.is('div.textGrid')){
                	input.find('input:first').focus();
                } else if(input.is('div.ui-combox-wrap')){
                  	try{input.find('input:first').trigger('click.combox');}catch(e){}
                } else if(input.attr('combo')||input.attr('tree')||input.attr('dictionary')){
                	try{input.parent().find('input.ui-combox-input').trigger('click.combox');}catch(e){}
                } else{
                    container.find('input[type="text"]:visible').focus();
                }
            },10);
            return input;
        },
        getValue: function(input, editParm) {
        	var editor=editParm['editor'];
        	var _getValue=function(){
            	if (input.is('input')) {
                    return input.getValue();
                } else {
                    return $(input.find('input:first')).getValue();
                }
            };
            var afterEdit=function(){
            	setTimeout(function(){
	            	if($.isFunction(editor.onAfterEdit)){
	        			editor.onAfterEdit.call(window,editParm.record);
	        		}
            	},0);
            };
            var controlType = editor.type || 'text',isLookup= GridEditorsUtil.isLookUp(editor);
            var data={},textField=editor['textField']||editParm.column.name,valueField=editor['valueField']||editParm.column.name;
            switch (controlType) {
	            case 'text':
	            case 'spinner':
	            case 'date':
	            case 'dateTime':
	            	afterEdit();
	            	return _getValue();
	                break;
	            case 'select':
	                return editParm.record[editParm.column.name];
	                break;
	            case 'combobox':
	            case 'tree':
	            case 'dictionary':
	            case 'lookup':
	            	if(isLookup){
	            		data[textField]=$(input[0]).combox('getText');
		            	data[valueField]=_getValue();
	            	}else{
	            		return editParm.record[editParm.column.name];
	            	}
	                break;
	            case 'radio':
	            	data[textField]=$('input:checked', input).next('label').text();
	            	data[valueField]=_getValue();
	                break;
	            case 'checkbox':
	            	var texts = [];
	                $('input:checked', input).next('label').each(function() {
	                    texts.push($(this).text());
	                });
	                data[textField]=texts.join(',');
		            data[valueField]=_getValue();
	                break;
	            default:
	            	return _getValue();
	                break;
	        }
            if(!$.isEmptyObject(data)){
            	// setTimeout(function(){//处理关联数据
            		$.extend(editParm.record, data);
            	// },0);
            }
            afterEdit();
            return data[textField];
        },
        setValue: function(input, value, editParm) {
        	var editor=editParm['editor'];
        	var controlType = editor.type || 'text',isLookup= GridEditorsUtil.isLookUp(editor);
            var data=GridEditorsUtil.getLookUpValue(editParm);
            switch (controlType) {
                 case 'text':
                 case 'spinner':
                	 $(input[0]).setValue(value);
                     break;
                 case 'date':
                 case 'dateTime':
                	 $(input[0]).setValue(value).trigger('blur');
                     break;
                 case 'combobox':
                	 $(input[0]).combox('setValue', data.value);
                     break;
                 case 'radio':
                	 $(input.find('input:first')).setValue(data.value);
                     break;
                 case 'checkbox':
                	 $(input.find('input')).setValue(data.value);
                     break;
                 case 'select':
                	 $(input.find('input:first')).setValue(value);
                     break;
                 case 'tree':
                 case 'dictionary':
                 case 'lookup':
                	 if(isLookup){
	                	 $(input[0]).combox('setValue', {
	                		 fieldValue: data.value,
	                         lookUpValue: data.text
	                     });
                	 }else{
                		 $(input.next().find('input:visible')).setValue(value);
                	 }
                     break;
                 default:
                	 $(input[0]).setValue(value);
                     break;
            }
        },
        resize: function(input, width, height, editParm) {
            var controlType =editParm['editor']['type'] || 'text';
            if (controlType == 'text' || controlType == 'radio' || controlType == 'checkbox'||controlType == 'date' || controlType == 'dateTime') {
                input.css({position:'relative',width:width+3,height:height+3});
                if(($.browser.msie && $.browser.version <= 8)){
                	input.css({width:width+2,marginTop:-1});
                }
            } else {
                var ui = null,_diffWidth;
                if (controlType == 'select') {
                    ui = input;
                    _diffWidth=30;
                } else if (controlType == 'spinner') {
                    ui = input.parent('div.ui-spinner');
                    _diffWidth=17;
                    width++;
                }else {
                    ui = input.next('div.ui-combox-wrap');
                    _diffWidth=30;
                }
                ui.css({width:width});
                ui.find('input').css({width:width-_diffWidth});
            }
        },
        destroy:function(input, editParm){
        	input.removeAllNode();
        }
    };
});

$.ligerDefaults.Grid.formatters=$.ligerDefaults.Grid.formatters||{};
$.ligerDefaults.Grid.formatters['money'] = function (value, column){
	return Public.currency(value);
};
$.ligerDefaults.Grid.formatters['date'] = function (value, column){
	if(Public.isBlank(value)) return "";
	if(value.length>10) return value.substring(0,10);
	return value;
};
$.ligerDefaults.Grid.formatters['datetime'] = function (value, column){
	if(Public.isBlank(value)) return "";
	if(value.length>16) return value.substring(0,16);
	return value;
};


(function($) {

	$.gridColumnSetting=function(options) {
		return new GridColumnSetting(options);
	};
    
	$.fn.gridColumnSetting=function(options) {
		var obj=this.data('ui_grid_column_setting');
		if(!obj){
			obj=new GridColumnSetting(options);
			$(this).data('ui_grid_column_setting',obj);
			this.on('click.grid.column.setting',function(){
				obj.showDialog();
			});
		}
		return this;
	};
	
	function GridColumnSetting(options) {
    	this.set(options);
    	this.sorting=false;
    	this.sorterFields=[];
    }
	
    $.extend(GridColumnSetting.prototype,{
    	set:function(op){
			this.options=$.extend({
				title:null,
				width:null,
				height:null,
				top:null,
				left:null,
				gridObject:null,
				gridManager:null
			},op||{});
		},
		getGridManager:function(){
			var p=this.options;
			if(!p.gridManager){
				p.gridManager=$(p.gridObject).ligerGetGridManager();
			}
			return p.gridManager;
		},
		showDialog:function(){
    		var _self = this,p=this.options;
    		this.dialog=Public.dialog({
    			title: p.title||'common.grid.setup.title',
    			width:p.width,
				height:p.height,
				top:p.top,
				left:p.left,
    			content: _self._createContent(),
    		    onClick: function ($el) {
    		    	_self._onClickEvent($el);
    		    }
    		});
    		//创建字段列表
    		var _height=this._createToggleCol();
    		if(_height > 300){
    			_height = 300;
    		}
    		this.dialog.find('div.dom-overflow-auto').height(_height+20);
    	},
    	_createContent:function(){
    		var g = this.getGridManager(),gp = g.options;
    		var html=['<div class="row-fluid">'];
    		html.push('<div class="col-xs-9 dom-overflow-auto" style="height:10px;">');
    		html.push('<table class="table"><colgroup><col width="20%"/><col width="80%"/></colgroup><tbody>');
    		html.push('</tbody></table>');
    		html.push('</div>');
    		html.push('<div class="col-xs-3 no-padding">');
    		html.push('<div class="col-xs-12 m-b-xs no-padding" style="text-align:center;">');
    		html.push('<button type="button" class="btn btn-primary column-up" title="up"><i class="fa fa-arrow-circle-up"></i></button>');
    		html.push('</div>');
    		html.push('<div class="col-xs-12 m-b-xs no-padding" style="text-align:center;">');
    		html.push('<button type="button" class="btn btn-primary column-down" title="down"><i class="fa fa-arrow-circle-down"></i></button>');
    		html.push('</div>');
    		//判读是否存在多字段排序
    		if(gp.enabledSort && gp.enabledMultiColumnSort){
    			html.push('<div class="col-xs-12 m-b-xs no-padding cancel-sort-button-div ui-hide" style="text-align:center;">');
        		html.push('<button type="button" class="btn btn-info cancel-sort" title="cancel sort"><i class="fa fa-cog"></i></button>');
        		html.push('</div>');
    			html.push('<div class="col-xs-12 m-b-xs no-padding column-sort-button-div" style="text-align:center;">');
        		html.push('<button type="button" class="btn btn-warning column-sort" title="sort"><i class="fa fa-sort-amount-asc"></i></button>');
        		html.push('</div>');
        		html.push('<div class="col-xs-12 m-b-xs no-padding do-sort-button-div ui-hide">');
        		html.push('<button type="button" class="btn btn-sm btn-success do-sort" title="search sort"><i class="fa fa-search-plus"></i></button>');
        		html.push('&nbsp;');
        		html.push('<button type="button" class="btn btn-sm btn-danger clean-sort" title="clean sort"><i class="fa fa-close"></i></button>');
        		html.push('</div>');
    		}
    		html.push('</div>');
    		html.push('</div>');
    		return html.join('');
    	},
    	_createToggleCol:function(){
    		var _self = this,g = this.getGridManager();
    		var _table=this.dialog.find('.table');
    		var html=[],_height=0;
    		$(g.columns).each(function (i, column){
    			if (column.issystem) return;
    			if (column.isAllowHide == false) return;
    			var chk = 'checked="checked"';
    			if (column._hide) chk = '';
    			html.push('<tr><td><input type="checkbox" style="margin:1px 0 0 0;" ', chk , ' columnindex="' , i , '"/></td><td>' , column.display , '</td></tr>');
    			_height+=40;
    	    });
    		_table.find('tbody').html(html.join(''));
    		this.sorting=false;
    		this.dialog.find('.do-sort-button-div').addClass('ui-hide');
    		this.dialog.find('.cancel-sort-button-div').addClass('ui-hide');
    		this.dialog.find('.column-sort-button-div').removeClass('ui-hide');
    	    return _height;
    	},
    	_onClickEvent:function($el){
    		var _self = this,g = this.getGridManager(); 
    		if($el.is(':checkbox')){//字段显示&隐藏
	    		setTimeout(function(){
	    			if ($('input:checked', _self.dialog).length + 1 <= 1){
		                return false;
		            }
		    		g.toggleCol(parseInt($el.attr("columnindex")), $el.is(':checked'), false);
	    		},10);
	    		return false;
	    	}
	    	if($el.is('td')){//字段选中
	    		if($el.parent().hasClass('disable')){
	    			return;
	    		}
	    		$el.parent().addClass('active').siblings().removeClass('active');
	    	}
	    	if($el.hasClass('column-up')){//上移
	    		_self._onColumnMove('up');
	    	}
	    	if($el.hasClass('column-down')){//下移
	    		_self._onColumnMove('down');
	    	}
	    	if($el.hasClass('column-sort')){//创建排序字段定义视图
	    		_self._createColumnSortView();
	    	}
	    	if($el.hasClass('clean-sort')){//清除排序字段
	    		_self._cleanSorterField();
	    		return false;
	    	}
	    	if($el.hasClass('cancel-sort')){//取消排序显示
	    		_self._createToggleCol();
	    		return false;
	    	}
	    	if($el.hasClass('do-sort')){//执行排序
	    		_self._doSortGrid($el);
	    		return false;
	    	}
	    	if($el.hasClass('sorting')){//字段排序顺序改变
	    		_self._changeColumnSort($el);
	    		return false;
	    	}
    	},
    	_onColumnMove:function(direction){
    		var _self = this,g = this.getGridManager(); 
    		var _active=_self.dialog.find('tr.active'),_other=null;
    		if(!_active.length) return;
    		_other=_active[direction=='up'?'prev':'next']();
    		if(!_other.length) return;
    		if(_other.hasClass('disable')) return;
    		_active[direction=='up'?'insertBefore':'insertAfter'](_other);
    		if(_self.sorting){//处理排序字段顺序
    			var _from=_active.find('.sorting').data('name'),  
    				_to=_other.find('.sorting').data('name');
    			_self._changeSortField(_from,_to);
    		}else{//非排序 需要处理grid显示
	    		var _from=parseInt(_active.find('input').attr('columnindex')),
	    			_to=parseInt(_other.find('input').attr('columnindex'));
	    		g.changeCol(_from,_to,direction=='down');
	    		_active.find('input').attr('columnindex',_to);
	    		_other.find('input').attr('columnindex',_from);
	    		//移动表头位置后会修改可见状态，这里根据复选框是否选中重置表头显示
	    		$('input:checkbox', _self.dialog).each(function(){
	    			g.toggleCol(parseInt($(this).attr("columnindex")), $(this).is(':checked'), false);
	    		});
    		}
    	},
    	//创建排序字段定义视图
    	_createColumnSortView:function(){
    		var g = this.getGridManager(); 
    		this.sorterFields=$.map(g.getSorterField(),function(c){return $.extend({},c);});
    		this._createColumnSortHtml();
    		this.sorting=true;
    		this.dialog.find('.column-sort-button-div').addClass('ui-hide');
    		this.dialog.find('.do-sort-button-div').removeClass('ui-hide');
    		this.dialog.find('.cancel-sort-button-div').removeClass('ui-hide');
    	},
    	_createColumnSortHtml:function(){
    		var _self = this,g = this.getGridManager();
    		var _table= this.dialog.find('.table');
    		var html=[],_column=null;
    		var _fields=$.map(_self.sorterFields,function(field){
    			_column=g._getColumnByName(field._column);
    			if(_column){
    				html.push('<tr><td><span class="sorting" data-name="',_column.name,'"><i class="fa fa-sort-amount-',field.direction.toLowerCase(),'"></i></span></td>');
    				html.push('<td>' , _column.display , '</td></tr>');
    			}
    			return field._column
    		});
    		$(g.columns).each(function (i, column){
    			if (column.issystem) return true;
    			if (column.isAllowHide == false) return true;
    			if (column.isSort == false) return true;
    			if ($.inArray(column.name,_fields) > -1) return true;
    			html.push('<tr class="disable"><td><span class="sorting" data-name="',column.name,'"><i class="fa fa-sort"></i></span></td><td>' , column.display , '</td></tr>');
    	    });
    		_table.find('tbody').html(html.join(''));
    	},
    	_setSorterField:function(sortField, columnName, direction){
    		var isNew=true;
	        //判断原来是否存在该字段
	        this.sorterFields=$.map(this.sorterFields,function(field){
	        	if(field._column==columnName){
	        		isNew=false;
	        		return direction==false?null:$.extend(field,{_column:columnName,name:sortField,direction:direction});
	        	}
	        	return field;
	        });
	        //排序参数恒等于false代表只删除
	        if(direction!==false&&isNew===true){
	        	//数组前插入
	        	this.sorterFields.push({_column:columnName,name:sortField,direction:direction});
	        }
       },
       _changeColumnSort:function($el){
    		var _self = this,g = this.getGridManager(); 
    		var _column=g._getColumnByName($el.data('name'));
    		if(!_column) return;
    		var columnName = sortField = _column.name,direction='';
            if(_column.sortField) sortField=_column.sortField;
    		if ($el.find('i').hasClass("fa-sort-amount-asc")){
            	direction=g._getNextSorterDirection(_column,'asc');
            }else if ($el.find('i').hasClass("fa-sort-amount-desc")){
            	direction=g._getNextSorterDirection(_column,'desc');
            }else{
            	direction=g._getNextSorterDirection(_column,false);
            }
    		//设置排序字段
    		_self._setSorterField(sortField, columnName, direction);
            _self._createColumnSortHtml();
    	},
    	//改变排序字段顺序
    	_changeSortField:function(from,to){
    		var _tmpField=null,_fromIndex=0,_toIndex=0;
    		$.each(this.sorterFields,function(i,field){
    			if(field._column==from){
    				_tmpField=field;
    				_fromIndex=i;
    			}
    			if(field._column==to){
    				_toIndex=i;
    			}
    		});
    		this.sorterFields[_fromIndex]=this.sorterFields[_toIndex];
    		this.sorterFields[_toIndex]=_tmpField;
    	},
    	_cleanSorterField:function(){
    		var _self = this,g = this.getGridManager();
    		g.cleanSorterField();
    		g.options.newPage = 1;
    		if (g.isDataChanged && !confirm(g.options.isContinueByDataChanged)){
    			return false;
    		}
    		g.loadData();
    		this.close();
    	},
    	_doSortGrid:function(){
    		var _self = this,g = this.getGridManager();
    		g.setSorterFields(_self.sorterFields);
    		g.options.newPage = 1;
    		if (g.isDataChanged && !confirm(g.options.isContinueByDataChanged)){
    			return false;
    		}
    		g.loadData();
    		this.close();
    	},
    	close:function(){
    		this.dialog.find('a.ui-public-dialog-close').triggerHandler('mousedown');
    	}
    });
	
})(jQuery);