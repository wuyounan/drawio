/*******************************************************************************
 * title : 数据查询条件生成 高级自定义查询 
 * author: xx 
 * date : 2018-10-16
 ******************************************************************************/
(function($) {
	
	//对话框打开条件生成器
	$.dataFilterDialog = function(op){
		var options=op||{};
		var _width=options['width']||700;
		if(_width > 1200){
			_width = 1200;
		}
		var _height=options['height']||400;
		if(_height > 600){
			_height= 600;
		}
		options['width']=_width;
		options['height']=_height;
		options['id']='showDataFilterDialog';
		UICtrl.showDialog({
   		 	 id:options.id,
	         title: options.title||$.i18nProp('common.dialog.choose'),
	         content:'<div class="dom-overflow-auto"><div class="data-filter"></div></div>',
	         width: options.width,
	         height: options.height,
	         parent: options.parent||window['ajaxDialog'],
	         init: function (div) {
	        	 $('div.dom-overflow-auto',div).height(div.height()-10);
	        	 $('div.data-filter',div).dataFilter({
	        		 fields:options['fields'],
	        		 data:options['data'],
	        		 editors:options['editors'],
	        		 addDefault:options['addDefault']===false?false:true
	        	 });
	        	 if($.isFunction(options.onInit)){
	        		 options.onInit.call(this,div);
	        	 }
	         },
	         ok: function(div){
	        	 var datas=$('div.data-filter',div).dataFilter('getData');
	        	 if($.isFunction(options.onOk)){
	        		 return options.onOk.call(this,datas,div);
		         }
	             return true;
	         },
	         okVal: 'common.button.ok',
	         close: function () {
	        	 if($.isFunction(options.onClose)){
	        		 return options.onClose.call(this);
		         }
			     return true;
			 }
	     });
	};
	
	$.fn.dataFilter = function(op){
		var obj=$(this).data('uiDataFilter');
		if(!obj){
			new dataFilter(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['getData','setData','setOperators','setFields','setEditors'],function(i,m){
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
	
	function dataFilter(el, op) {
		this.element = $(el);
		this.options = {};
		this.operators = {};// 字段类型 - 运算符 的对应关系
		this.editorCounter = 0; //输入框计算器
		this.set(op);
		this.init();
		this.element.data('uiDataFilter', this);
	}

	$.extend(dataFilter.prototype, {
		set:function(op){
		    this.options = $.extend({
		    	strings:{
		    		"and" : "并且",
		    		"or" : "或者",
		    		"equal" : "相等",
		    		"notequal" : "不相等",
		    		"startwith" : "以..开始",
		    		"endwith" : "以..结束",
		    		"like" : "相似",
		    		"greater" : "大于",
		    		"greaterorequal" : "大于或等于",
		    		"less" : "小于",
		    		"lessorequal" : "小于或等于",
		    		"in" : "包括在...",
		    		"notin" : "不包括...",
		    		"addgroup" : "增加分组",
		    		"addrule" : "增加条件",
		    		"deleterule" : "删除条件",
		    		"deletegroup" : "删除分组"
		    	},
		    	addDefault:true,
		    	editors:{},
		    	fields:[]
		    }, this.options, op||{});
		    this.setOperators();
		},
		setOperators:function(){
			var g = this, p = this.options;
			this.operators['string'] = this.operators['text'] = ["equal", "notequal", "startwith", "endwith", "like", "in", "notin"];
		    this.operators['number'] = ["equal", "notequal", "greater", "greaterorequal", "less", "lessorequal", "in", "notin"];
		    this.operators['date'] = ["equal", "notequal", "greater", "greaterorequal", "less", "lessorequal"];
		    if($.isFunction(p.setOperators)){
		    	p.setOperators.call(g);
		    }
		},
        setFields: function (fields){  //设置字段列表
            var g = this, p = this.options;
            p.fields = $.map(fields||p.fields,function (field){
            	if(field['filter']===false){
            		return null;
            	}
            	if(!field.display||!field.name){
            		return null;
            	}
                var o = {
                	type:'string',
                	name:field.name,
                	display:field.display,
                	editor:p.editors[field.name]||field.editor,
                	alias:field.alias,
                	column:field.column,
                	formula:field.formula||'',
                	operator:field.operator,
                	used:field.used||'value'
                };
                if($.i18nProp){
                	o['display']=$.i18nProp(o.display||'');
                }
                var isNumber = field.type == "int" || field.type == "number" || field.type == "float"|| field.type == "money";
                var isDate = field.type == "date"||field.type == "dateTime"||field.type == "datetime";
                if (isNumber){
                	o.type = "number";
                }else if (isDate){
                	o.type = "date";
                }
                return o;
            });
            if (g.group){
            	g.group.remove();
            }
            g.group = $(g._bulidGroupHtml()).appendTo(g.element);
           
        },
        setEditors:function(editors){
        	var g = this, p = this.options;
        	p.editors=editors;
        	$.each(p.fields,function (i,field){
        		field.editor=p.editors[field.name]||field.editor;
        	});
        },
		init:function(){
			var g = this, p = this.options;
			$(g.element).bind("click", function (e){
				e.preventDefault(); 
	            var $click = $((e.target || e.srcElement));
	            if ($click.hasClass("addgroup")){
	                g.addGroup($click.parent().parent());
	            }else if ($click.hasClass("deletegroup")){
	                g.deleteGroup($click.parent().parent());
	            }else if ($click.hasClass("addrule")){
	                g.addRule($click.parent().parent());
	            }else if ($click.hasClass("deleterole")){ 
	                g.deleteRule($click.parent().parent());
	            }
	        });
			this.setFields();
			//存在数据
			if($.isPlainObject(p.data)&&!$.isEmptyObject(p.data)){
				this.setData(p.data);
			}else{
				if(p.addDefault){
					g.addRule(g.group);
		        }
			}
		},
        _bulidGroupHtml: function (altering, allowDelete){//获取一个分组的html
            var g = this, p = this.options;
            var _html = [];
            _html.push('<div class="ui-filter-group alert m-b-xs p-xxs ',altering?'alert-info':'alert-warning','">');
            _html.push('<div class="ui-filter-rowlast m-xxs clearfix">');
            //and or
            _html.push('<select class="select groupopsel">');
            _html.push('<option value="and">',p.strings['and'],'</option>');
            _html.push('<option value="or">',p.strings['or'],'</option>');
            _html.push('</select>');
            //add group
            _html.push('<input type="button" value="',p.strings['addgroup'],'" class="btn btn-info btn-xs m-l-xs addgroup">');
            //add rule
            _html.push('<input type="button" value="',p.strings['addrule'],'" class="btn btn-warning btn-xs m-l-xs addrule">');
            if (allowDelete){
            	_html.push('<input type="button" value="',p.strings['deletegroup'],'" class="btn btn-danger btn-xs m-l-xs deletegroup">');
            }
            _html.push('</div>');
            _html.push('</div>');
            return _html.join('');
        },
        _bulidRuleRowHtml: function (){//获取字段值规则的html
            var g = this, p = this.options;
            var fields = p.fields;
            var _rowHtml = [];
            var fieldType = fields && fields.length && fields[0].type ? fields[0].type : "string";
            _rowHtml.push('<div class="ui-filter-field m-xxs hg-form clearfix" fieldtype="',fieldType,'">');
            _rowHtml.push('<div class="col-xs-5 col-sm-3 ui-filter-column">');
            _rowHtml.push('<select class="select fieldsel">');
            $.each(fields,function(i,field){
            	_rowHtml.push('<option value="',field.name,'"');
                if (i == 0){
                	_rowHtml.push(" selected ");
                }
                _rowHtml.push('>');
                _rowHtml.push(field.display);
                _rowHtml.push('</option>');
            });
            _rowHtml.push("</select>");
            _rowHtml.push('</div>');
            //操作符
            _rowHtml.push('<div class="col-xs-5 col-sm-2 ui-filter-op">');
            _rowHtml.push('<select class="select opsel">'); 
            _rowHtml.push(g._bulidOpSelectOptionsHtml(fieldType, fields && fields.length ? fields[0].operator : null));
            _rowHtml.push('</select>');
            _rowHtml.push('</div>');
            //值
            _rowHtml.push('<div class="col-xs-8 col-sm-4 ui-filter-value">');
            //_rowHtml.push('<input type="text" class="text valtxt" />');
            _rowHtml.push('</div>');
            //删除条件
            _rowHtml.push('<div class="col-xs-4 col-sm-3 p-t-sm">');
            _rowHtml.push('<input type="hidden" value="" name="fullId">');
            _rowHtml.push('<input type="hidden" value="" class="fullName">');
            _rowHtml.push('<input type="button" value="',p.strings['deleterule'],'" class="btn btn-danger btn-xs m-l-xs deleterole">');
            _rowHtml.push('</div>');
            _rowHtml.push('</div>');
            return _rowHtml.join('');
        },
        _bulidOpSelectOptionsHtml: function (fieldType, operator){//获取一个运算符选择框的html
            var g = this, p = this.options;
            var ops = g.operators[fieldType];
            var opHtmlArr = [];
            if (operator){
            	if($.isArray(operator)){
            		ops = operator;
            	}else if(operator.length){
            		ops = operator.split(',');
            	}
            }
            if (!ops || !ops.length){
                ops = ["equal", "notequal"];
            }
            $.each(ops,function(i,op){
            	opHtmlArr.push('<option value="' , op , '">');
            	opHtmlArr.push(p.strings[op]);
            	opHtmlArr.push('</option>');
            });
            return opHtmlArr.join('');
        },
        addGroup: function (jgroup){//增加分组
            var g = this, p = this.options;
            var altering = !jgroup.hasClass("alert-info");
            var _group = $(g._bulidGroupHtml(altering, true));
            $(">div.ui-filter-rowlast", jgroup).before(_group);
            if (p.addDefault){
                g.addRule(_group);
            }
            return _group;
        },
        deleteGroup: function (jgroup){//删除分组 
            var g = this, p = this.options;
            $("div.ui-filter-value", jgroup).each(function (){
                var rulerow = $(this).parent();
                $("select.fieldsel", rulerow).off();
                g.removeEditor(rulerow);
            });
            $(jgroup).remove();
        },
        addRule: function (jgroup){//增加一个条件
            var g = this, p = this.options;
            jgroup = jgroup || g.group;
            var lastrow = $(">div.ui-filter-rowlast", jgroup);
            var rulerow = $(g._bulidRuleRowHtml());
            lastrow.before(rulerow);
            if (p.fields.length){
                //如果第一个字段启用了自定义输入框
                g.appendEditor(rulerow, p.fields[0]);
            }
            //事件：字段列表改变时
            $("select.fieldsel", rulerow).on('change', function (){
                g._fieldChange(this,rulerow);
            });
            return rulerow;
        },
        _fieldChange:function(fieldsel,rulerow){//选择字段改变
        	var g = this, p = this.options;
        	var jopsel = $(fieldsel).parent().next().find("select:first");
            var fieldName = $(fieldsel).val();
            if (!fieldName){
            	return;
            }
            var field = g._getField(fieldName);
            if(field){
                //字段类型处理
                var fieldType = field.type || "string";
                var oldFieldtype = rulerow.attr("fieldtype");
                if (fieldType != oldFieldtype){
                    jopsel.html(g._bulidOpSelectOptionsHtml(fieldType,field.operator));
                    rulerow.attr("fieldtype", fieldType);
                }
                //删除旧的输入框 
                g.removeEditor(rulerow);
                //添加新的输入框
                g.appendEditor(rulerow, field);
            }
        },
        deleteRule: function (rulerow){//删除一个条件
            $("select.fieldsel", rulerow).off();
            this.removeEditor(rulerow);
            $(rulerow).remove();
        },
        removeEditor: function (rulerow){//删除编辑器
            var g = this, p = this.options;
            $("div.ui-filter-value", rulerow).each(function (){
                $("input", this).off();
                $(this).empty();
            });
            $('input:hidden',rulerow).val('');
        },
        appendEditor: function (rulerow, field){//附加一个输入框
            var g = this, p = this.options;
            var editor = field.editor||{};
            if($.isFunction(editor.getEditor)){
            	editor=editor.getEditor.call(window);
            	if(editor===false) return;
            }
            editor = $.extend({
            	type:field.type,
            	name:'value',
                display:field.display,
                maxLength: false,
                readOnly: false,
                style: false,
                required: false,
                mask: false,
                match:false,
                onAfterInit:null,// 初始化完成执行
                data: {}
            },editor);
            var controlType = editor.type;
            //方便统一赋值 逻辑管理 将select 设置为lookup
            if(controlType=='select'){
            	controlType='lookup';
            }
            editor.type=controlType;
            var _container = $('div.ui-filter-value',rulerow);
            var _input = $(GridEditorsUtil.createInputHtml(editor,field.name));
            _container.append(_input);
            Public.autoInitializeUI(_container);
            $('#value_text',_container).attr('name','text');
            if($.isFunction(editor.onAfterInit)){
            	editor.onAfterInit.call(g,_container);
            }
            if(controlType=='combobox'||controlType=='select'||controlType=='lookup'||controlType=='tree'||controlType=='dictionary'){
            	var beforeChange=editor.beforeChange,beforeChangeFn=null;
            	if($.isFunction(beforeChange)){// 方法
            		beforeChangeFn=function(value){
            			return beforeChange.call(this,value,_container);
            		};
            	}
        		var getParam=editor.data.getParam,fn=null;
        		if(getParam){// 存在获取参数的方法
        			if($.isFunction(getParam)){// 方法
            			fn=function(row){
            				return function(){
            					return getParam.call(window,row);
            				};
            			}({});
            		}else if($.isPlainObject(getParam)){// json对象
            			fn=function(row){
            				return function(){
            					var param={};
            					$.each(getParam,function(p,o){
                					param[p]=row[o];
                				});
                				return param;
            				};
            			}({});
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
        					 $('input[name="'+o+'"]',rulerow).val(value[p]);
                         });
        			};
        		}
        		// select 需要单独处理返回
                if (controlType == 'select') {
                    if(!$.isEmptyObject(options)){
                    	$(_input.find('input:first')).combox(options);
                    }
                }else {
                	if(!$.isEmptyObject(options)){
                		$(_input[0]).combox(options);
                	}
                }
        	}
        },
        _getField: function (fieldname){//根据fieldName 获取 字段
            var g = this, p = this.options;
            var _field = null;
            $.each(p.fields,function(i,field){
            	if(field.name == fieldname){
            		_field=field;
            		return false;
            	}
            });
            return _field;
        },
        getData: function (group){ //获取分组数据
            var g = this, p = this.options;
            group = group || g.group;
            var groupData = {};
            $(">div", group).each(function (i, row){
                if ($(row).hasClass("ui-filter-group")){//分组
                	if (!groupData.groups) groupData.groups = [];
                    groupData.groups.push(g.getData(row));//递归获取下级分组
                }else if ($(row).hasClass("ui-filter-rowlast")){
                    groupData.op = $(">.groupopsel", row).val();
                }else{
                    var fieldName = $("select.fieldsel:first", row).val();
                    var field = g._getField(fieldName);
                    var op = $(".opsel:first", row).val();
                    var values = g._getRuleValue(row, field);
                    var type = $(row).attr("fieldtype") || "string";
                    if (!groupData.rules) groupData.rules = []; 
                    if (values != null){
                        groupData.rules.push($.extend(values,{
                            name: fieldName,
                            op: op,
                            type: type,
                            column:field.column,
                            alias:field.alias,
                            formula:field.formula,
                            used:field.used
                        }));
                    }
                }
            });
            return groupData;
        },
        _getRuleValue: function (rulerow){
        	var values={};
        	$.each(['value','text','fullId','fullName'],function(i, o) {
        		values[o]="";
        		var _input=$('input[name="'+o+'"]',rulerow)
        		if(_input.length){
        			values[o]=_input.val();
        		}
            });
            return values;
        },
        _setRuleValue: function (rulerow,values){
        	$.each(['value','text','fullId','fullName'],function(i, o) {
        		$('input[name="'+o+'"]',rulerow).val(values[o]||'');
            });
        },
        setData: function (group, jgroup){//设置规则  分组数据 分组 dom jQuery对象
            var g = this, p = this.options;
            jgroup = jgroup || g.group;
            var lastrow = $(">div.ui-filter-rowlast", jgroup);
            if (jgroup){
                jgroup.find(">div.ui-filter-group").remove();
            }
            $("select:first", lastrow).val(group.op);
            if (group.rules){
                $(group.rules).each(function (){
                    var rulerow = g.addRule(jgroup),
                    	fieldName = this.name,
                    	field = g._getField(fieldName);
                    
                    $("select.opsel", rulerow).html(g._bulidOpSelectOptionsHtml(this.type || "string", field.operator ));
                    rulerow.attr("fieldtype", this.type || "string");
                    $("select.opsel", rulerow).val(this.op);
                    var _fieldsel=$("select.fieldsel", rulerow);
                    _fieldsel.val(fieldName);
                    //创建输入字段
                    g._fieldChange(_fieldsel,rulerow);
                    //设置值
                    g._setRuleValue(rulerow,this);
                });
            }
            if (group.groups){
                $(group.groups).each(function (){
                    var subjgroup = g.addGroup(jgroup);
                    g.setData(this, subjgroup);
                });
            }
        }
	});
	
	$.dataFilter = function () { };
	$.dataFilter.filterTranslator = {
			 translateGroup: function (group){
	            var out = [];
	            if (group == null) return " 1==1 ";
	            var appended = false;
	            out.push('(');
	            if (group.rules != null){
	                for (var i in group.rules){
	                    if (i == "indexOf")
	                        continue;
	                    var rule = group.rules[i];
	                    if (appended){
	                    	out.push(this.getOperatorQueryText(group.op));
	                    } 
	                    out.push(this.translateRule(rule));
	                    appended = true;
	                }
	            }
	            if (group.groups != null){
	                for (var j in group.groups){
	                    var subgroup = group.groups[j];
	                    if (appended){
	                    	out.push(this.getOperatorQueryText(group.op));
	                    }
	                    out.push(this.translateGroup(subgroup));
	                    appended = true;
	                }
	            }
	            out.push(')');
	            if (appended == false){
	            	return " 1==1 ";
	            }
	            return out.join('');
	        },
	        translateRule: function (rule){
	            var out = [];
	            if (rule == null){
	            	return " 1==1 ";
	            }
	            var _value=rule[rule.used];
	            if(Public.isBlank(_value)){
	            	return " 1==1 ";
	            }
	            if (rule.op == "like" || rule.op == "startwith" || rule.op == "endwith"){
	                out.push('/');
	                if (rule.op == "startwith"){
	                	out.push('^');
	                }
	                out.push(_value);
	                if (rule.op == "endwith"){
	                	out.push('$');
	                }
	                out.push('/i.test(');
	                out.push('o["');
	                out.push(rule.name);
	                out.push('"]');
	                out.push(')');
	            }else{
	            	out.push('o["');
		            out.push(rule.name);
		            out.push('"]');
		            out.push(this.getOperatorQueryText(rule.op));
		            out.push('"');
		            out.push(_value);
		            out.push('"');
	            }
	            return out.join('');
	        },
	        getOperatorQueryText: function (op){
	            switch (op){
	                case "equal":
	                    return " == ";
	                case "notequal":
	                    return " != ";
	                case "greater":
	                    return " > ";
	                case "greaterorequal":
	                    return " >= ";
	                case "less":
	                    return " < ";
	                case "lessorequal":
	                    return " <= ";
	                case "and":
	                    return " && ";
	                case "or":
	                    return " || ";
	                default:
	                    return " == ";
	            }
	        }
	  };
	 
	  $.dataFilter.getFilterFunction = function (condition){
		  if ($.isArray(condition)){
			  condition = { op: "and", rules: condition };
	      }
	      var fnbody = ' return  ' + $.dataFilter.filterTranslator.translateGroup(condition);
	      return new Function("o", fnbody);
	  };

})(jQuery);