/*---------------------------------------------------------------------------*\
|  title:         扩展字段使用                                                                                                                                                           |
|  Author:        xx                                                          |
|  Created:       2017-03-22                                                  |
|  Description:   扩展字段的读取及数据存储                                                                                                                                    |
|   select dataOptions: type:'sys',name:'flexFieldDefine',back:{id:'003',fieldCname:'select'} 只能匹配同一分组下的字段
|   lookup dataOptions: type:'sys',name:'flexFieldDefine',textField:'fieldCname',valueField:'defineId'
|   lookup 只有一条显示数据时 dataOptions: type:'sys',name:'flexFieldDefine'
\*---------------------------------------------------------------------------*/
(function($) {	
	$.fn.flexField = function(op){
		var obj=this.data('ui-flex-field');
		if(!obj){
			new FlexField(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['getFieldValues'],function(i,m){
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
	
	function FlexField(el,op){
		this.options={};
		this.element=$(el);
		this.bizId=null;
		this.bizCode=null;
		this.set(op);
		this.element.data('ui-flex-field',this);
	}
	
	$.extend(FlexField.prototype, {
		set:function(op){
			this.options=$.extend({
				url:web_app.name+'/flexField/queryFlexFieldBizGroupFieldStorage.ajax',
				bizCode:'',//业务类别
				bizId:'',//业务ID
				param:{},//其他业务参数
				needTitle:true,
				width:'99%',
				onInit:function(){}
			},this.options, op||{});
			var bizId = this.options.bizId;
			var bizCode=this.options.bizCode;
			if (Public.isBlank(this.bizId)) {
				this.bizId=bizId;
				this.bizCode=bizCode;
				this.init();
			}else{
				if(this.bizId!=bizId){
					this.bizId=bizId;
					this.element.empty();
					this.init();
				}
			}
		},
		init:function(){//初始化加载分组信息
		    var opt = this.options, _self = this, html = [];
		    var needTitle=opt.needTitle;
		    var param= $.extend(this.options.param,{bizCode: this.bizCode, bizId: this.bizId});
		    Public.ajax(opt.url,param, function (data) {
		    	//控件布局
		        $.each(data, function (i, groupData) {
		            try {
		            	if(needTitle){
		            		html.push("<div id='navTitle_" + groupData.id + "' class='navTitle row-fluid'>");
		            		html.push("<a href='javascript:void(0);' hidefocus class='togglebtn' hideTable='#flexField_" + groupData.id + "' title='show or hide'>");
		            		html.push("<i class='fa fa-angle-double-down'></i>","</a>");
		            		html.push("<i class='fa fa-windows'></i>&nbsp;<span class='titleSpan'>", groupData.name, "</span>");
		            		html.push("</div>", "<div class='navline'></div>");
		            	}
		                html.push(_self.fieldsLayout(groupData));
		            } catch (e) { 
		            	alert("扩展字段读取:" + e.message); 
		            }
		        });
				_self.element.html(html.join(''));
				//创建控件
				try {
					$.each(data, function (i, groupData) {
						_self.createFlexField(groupData);
					});
				} catch (e) { 
					alert("扩展字段读取:" + e.message); 
		        }
				if(needTitle){
					try{UICtrl.autoGroupAreaToggle(_self.element);}catch(e){}
				}
				if(Public.isReadOnly){//只读模式下修改输入框为只读
					UICtrl.setDisable(_self.element);
				}else if(!UICtrl.isApplyProcUnit()){//不是申请环节默认为只读
					UICtrl.setDisable(_self.element);
				}
				
				if($.isFunction(opt.onInit)){
					opt.onInit.call(window);
				}
			});
		},
		fieldsLayout:function(groupData){
			var showModel=groupData.showModel;
			var html = ["<div class='flexFieldDiv hg-form' groupId='",groupData.id,"' style='width:",this.options.width,"'>"];
			if(showModel==1){//表格布局
				html.push('<div class="hg-form-cols">');
			}
			html.push.apply(html,this.divLayout(groupData));
			if(showModel==1){//表格布局
				html.push('</div>');
			}
			html.push("</div>");
			return html.join('');
		},
		tableLayout:function(groupData){
			var col = parseInt(groupData.cols);
	        var index= 0, length = groupData.fields.length;
			var html=["<table cellspacing='0px' cellpadding='0px' class='tableInput' id='flexField_",groupData.id,"'>"];
			var tableLayout=groupData.tableLayout;
			if(!Public.isBlank(tableLayout)){
				html.push('<COLGROUP>');
				$.each(tableLayout.split(','),function(i,o){
					html.push("<COL width='",o,"'/>");
				});
				html.push('</COLGROUP>');
			}
			$.each(groupData.fields, function(i, fieldData){
				//不显示的字段定义为隐藏字段
				if(!fieldData.visible){
					return true;
	            }
	            var isNewLine = parseInt(fieldData.newLine), colSpan = parseInt(fieldData.colSpan);
	            colSpan = isNaN(colSpan) ? 1 : colSpan;
	            if(isNewLine==1){
	            	for(var j=0 ; j< col - index; j++){//补上一行空白
	                    html.push("<td class='title'>&nbsp;</td>");
	                }
	            	html.push("</tr>"); 
	            	html.push("<tr>");
	            	colSpan=col-1;//默认占一排
	            	index=col;
	            }else{
	            	if((index+1+colSpan)>col){
	                	colSpan=col-1-index;
	                }else if((index+1+colSpan)==col-1){
	                	colSpan=colSpan+1;
	                }
	                colSpan=colSpan<1?1:colSpan;
	                if(index%col==0){
	                	if(index==col){
	                		html.push('</tr>');
	                		index=0;
	                	}
	                	html.push('<tr>');
	                }
	                index=index+1+colSpan;
	                index=index>col?col:index;
	            }
	            html.push("<td class='title'><span class='labelSpan'>");
	            html.push($.i18nProp(fieldData.description));
	            if (!fieldData.nullable) {
	            	html.push("<font color='#FF0000'>*</font>");
	            }
	            html.push("&nbsp;:</span>","</td>");
	            var className='';
	            if(fieldData.readOnly){
	            	className=' disable';
	            }
	            html.push("<td  class='edit",className,"' colspan='",colSpan,"' id='show_",fieldData.flexFieldDefinitionId,"'>");
	            html.push("<div>",fieldData.fieldNameValue,"</div>");
	            html.push("</td>");
	        });
	        for(var i=0;i<col-index;i++){
	            html.push("<td class='title'>&nbsp;</td>");
	        }
	        html.push("</tr>","</table>");
	        return html;
		},
		divLayout:function(groupData){
			var col = parseInt(groupData.cols);
	        var index= 0, length = groupData.fields.length;
			var html=["<div class='row-fluid' id='flexField_",groupData.id,"'>"];
			$.each(groupData.fields, function(i, fieldData){
				//不显示的字段定义为隐藏字段
				if(!fieldData.visible){
					return true;
	            }
				var controlWidth=parseInt(fieldData.controlWidth),labelWidth=parseInt(fieldData.labelWidth);
				if(isNaN(controlWidth)||controlWidth<=0){
					controlWidth=2;
	            }
	            if(isNaN(labelWidth)||labelWidth<=0){
	            	labelWidth=2;
	            }
				if(col>0){
	            	var isNewLine = parseInt(fieldData.newLine),colSpan=labelWidth+controlWidth;
	            	if(isNewLine==1){
	            		if(col - index > 0){
	            			html.push("<div class='col-xs-12 hidden-xs col-sm-",(col - index),"'>&nbsp;</div>");
	            		}
		            	html.push("</div>"); 
		            	html.push("<div class='hg-form-row'>");
		            	index=colSpan;
		            }else{
		                if(index%col==0){
		                	if(index==col){
		                		html.push("</div>");
		                		index=0;
		                	}
		                	html.push("<div class='hg-form-row'>");
		                }
		                index=index+colSpan;
		                index=index>col?col:index;
		            }
            	}
                html.push("<div class='col-xs-4 col-sm-",labelWidth,"'>");
                html.push("<label class='hg-form-label",fieldData.nullable===0?" required":"","'>");
                html.push($.i18nProp(fieldData.description));
                html.push("&nbsp;:","</label>","</div>");
                html.push("<div class='col-xs-8 col-white-bg col-sm-",controlWidth,"' id='show_",fieldData.flexFieldDefinitionId,"'>");
                html.push("<div>",fieldData.fieldNameValue,"</div>");
                html.push("</div>");
            });
			if(col > 0){
				if(col - index > 0){
        			html.push("<div class='col-xs-12 hidden-xs col-sm-",(col - index),"'>&nbsp;</div>");
        		}
				html.push('</div>');
			}
			html.push('</div>');
            return html;
		},
		createFlexField:function(groupData){//根据分组加载字段
			var _self=this,groupId=groupData.id;
			var container=$('#flexField_'+groupId);
            $.each(groupData.fields, function(i, fieldData){
            	var fieldContainer=$('#show_'+fieldData.flexFieldDefinitionId,container);
            	var fieldElement=null;
            	var fieldInfo=[" fieldName='",fieldData.fieldName,"' name='f_",fieldData.flexFieldDefinitionId,"' value='",fieldData.fieldValue,"' storageId='", fieldData.storageId, "'" ];
                if(!fieldData.visible){//处理隐藏字段
                	container.prepend(["<input type='hidden'",fieldInfo.join(''),"/>"].join(''));
                	return true;
                }
                //清空显示域
                fieldContainer.empty();
                //字段长度
                var maxLength = [];
                var fieldLength=parseInt(fieldData.fieldLength);
                if (!isNaN(fieldLength)&&fieldLength > 0){
                    maxLength.push(" maxlength='" , fieldLength , "' ");
                }else{
                	maxLength.push(" maxlength='40' ");
                }
                var className='';
                //只读
                var readOnly =[];
                if(fieldData.readOnly){
                	className=' textReadonly';
                	readOnly=[" readonly='readonly'"];
                }
                //数据输入框HTML
                var inputHtml="<input type='text' class='{class}' "+fieldInfo.join('')+"{required}{maxLength}{readOnly}/>";
                inputHtml=inputHtml.replace('{maxLength}',maxLength.join(''))
                			.replace('{readOnly}',readOnly.join(''))
                			.replace('{required}',fieldData.nullable?'':' required="true" label="'+$.i18nProp(fieldData.description)+'"');
               
                //处理控件类型显示
                switch (parseInt(fieldData.controlType)) {
                    case 1: //textbox
                    	fieldElement=$(inputHtml.replace('{class}',className)).appendTo(fieldContainer);
                        break;
                    case 2: //combox 
                    	fieldElement=$(inputHtml.replace('{class}',className)).appendTo(fieldContainer);
                    	if ($.fn.combox) {
                    		fieldElement.combox(_self.getComboxDataOptions(fieldData));
                    	}
                        break;
                    case 3: //spinner
                    	fieldElement=$(inputHtml.replace('{class}',className)).appendTo(fieldContainer);
                    	if ($.fn.spinner) {
                    		fieldElement.spinner(_self.getSpinnerDataOptions(fieldData));
                    	}
                        break;
                    case 4: //date
                    	fieldElement=$(inputHtml.replace('{class}',className+' textDate')).appendTo(fieldContainer);
                    	if ($.fn.datepicker) {
                    		fieldElement.datepicker().mask('9999-99-99');
                    	}
                        break;
                    case 5: //dateTime
                    	fieldElement=$(inputHtml.replace('{class}',className+' textDate')).appendTo(fieldContainer);
                    	if ($.fn.datepicker) {
                    		fieldElement.datepicker({useTime: true}).mask('9999-99-99 99:99');
                    	}
                        break;
                    case 6: //radio
                    	$(_self.createControl(fieldData,'radio')).appendTo(fieldContainer);
                        break;
                    case 7: //checkbox
                    	$(_self.createControl(fieldData,'checkbox')).appendTo(fieldContainer);
                        break;
                    case 8: //select
                    	var html=[];
                    	html.push('<div class="input-group ui-combox-wrap">');
        				html.push(inputHtml.replace('{class}',className));
        				html.push('<span class="input-group-btn"><button type="button" class="btn btn-primary">', '<i class="fa fa-caret-down"></i>', '</span></div>');
                    	$(html.join('')).appendTo(fieldContainer);
                    	fieldElement=fieldContainer.find('input');
                    	var dataOptions=_self.getSelectDataOptions(fieldData,container);
                    	if($.fn.combox&&dataOptions){
                    		fieldElement.searchbox(dataOptions);
                    	}
                    	break;
                    case 9: //lookup
                    	fieldElement=$(inputHtml.replace('{class}',className)).appendTo(fieldContainer);
                    	var dataOptions=_self.getSelectDataOptions(fieldData,container);
                    	if($.fn.combox&&dataOptions){
                    		fieldElement.searchbox(dataOptions);
                    	}
                    	break;
                    case 10: //tree 
                    	fieldElement=$(inputHtml.replace('{class}',className)).appendTo(fieldContainer);
                    	var dataOptions=_self.getSelectDataOptions(fieldData,container);
                    	if($.fn.combox&&dataOptions){
                    		fieldElement.treebox(dataOptions);
                    	}
                        break;
                    default:
                    	fieldElement=$(inputHtml.replace('{class}',className)).appendTo(fieldContainer);
                        break;
                }
                var mask=_self.getNumberMask(fieldData);
                if(fieldElement&&mask&&$.fn.mask){
                	fieldElement.mask(mask, {number: true});
                }
            });
		},
		getJSONDataSource:function(fieldData){//读取Json数据源
			try{
				var dataSource=fieldData.dataSource;
				if(dataSource && typeof dataSource=='string'){
					if (dataSource.substring(0, 1) != "{"){
						dataSource = ["{" , dataSource , "}"].join('');
					}
					dataSource=(new Function("return "+dataSource))();
				}
				return dataSource;
			}catch(e){
				alert('读取字段'+fieldData.description+'数据源错误!'+e.message);
				return {};
			}
		},
		getArrayDataSource:function(fieldData){//读取集合数据源
			try{
				var dataSource=fieldData.dataSource;
				if(dataSource && typeof dataSource=='string'){
					if (dataSource.substring(0, 1) != "["){
						dataSource = ["[", dataSource , "]"].join('');
					}
					dataSource=(new Function("return "+dataSource))();
				}
				return dataSource;
			}catch(e){
				alert('读取字段'+fieldData.description+'数据源错误!'+e.message);
				return [];
			}
		},
		createControl:function(fieldData, type){
			var html=[],_self=this,dataSource;
			var radioHtml="<label><input type='"+type+"' id={id} name='{name}' value='{value}' fieldName='{fieldName}' {checked}/>&nbsp;{label}</label>";
			var getChecked =function(value){
				var flag=false;
				if(type=='radio'){
					flag=value==fieldData.fieldValue;
				}else{
					$.each(fieldData.fieldValue.split(','),function(i,o){
						if(value==o){
							flag=true;
							return false;
						}
					});
				}
				return flag?"checked='checked'":"";
			};
			switch (parseInt(fieldData.dataSourceKindId)) {
			 case 2: //集合
				 dataSource=_self.getArrayDataSource(fieldData);
				 $.each(dataSource,function(i,o){
					 html.push(radioHtml.replace(/{id}/g,fieldData.fieldName+"_"+i)
			                    .replace('{name}','f_'+fieldData.flexFieldDefinitionId)
			                    .replace('{value}',o)
			                    .replace('{fieldName}',fieldData.fieldName)
			                    .replace('{checked}',getChecked(o))
			                    .replace('{label}',o),'&nbsp;');
				 });
                 break;
			 case 3: //数据字典
			 case 4: //JSON
				 dataSource=_self.getJSONDataSource(fieldData);
				 $.each(dataSource,function(i,o){
					 html.push(radioHtml.replace(/{id}/g,fieldData.fieldName+"_"+i)
			                    .replace('{name}','f_'+fieldData.flexFieldDefinitionId)
			                    .replace('{value}',i)
			                    .replace('{fieldName}',fieldData.fieldName)
			                    .replace('{checked}',getChecked(i))
			                    .replace('{label}',o),'&nbsp;');
				 });
                 break;
			}
			return html.join('');
		},
		getSpinnerDataOptions:function(fieldData){//获取Spinner执行参数
			var dataOptions={},flag=false;
			if(fieldData.minValue&&!isNaN(parseInt(fieldData.minValue))){
				dataOptions['min']=parseInt(fieldData.minValue);
				flag=true;
			}
			if(fieldData.maxValue&&!isNaN(parseInt(fieldData.maxValue))){
				dataOptions['max']=parseInt(fieldData.maxValue);
				flag=true;
			}
			if(fieldData.defaultValue&&!isNaN(parseInt(fieldData.defaultValue))){
				dataOptions['default_value']=parseInt(fieldData.defaultValue);
				flag=true;
			}
			if(flag){
				return dataOptions;
			}else{
				return {};
			}
		},
		getNumberMask:function(fieldData){//获取number的格式化参数
			if(parseInt(fieldData.fieldType)!=2) return null;//fieldType ==2 为number类型
			var length=parseInt(fieldData.fieldLength),precision=parseInt(fieldData.fieldPrecision);
			var mask=[];
			if(length&&!isNaN(length)){
				for(var i=0;i<length;i++){
					mask.push('n');
				}
			}
			if(precision&&!isNaN(precision)&&precision>0){
				mask.push('.');
				for(var i=0;i<precision;i++){
					mask.push('n');
				}
			}
			if(mask.length>0){
				return mask.join('');
			}else{
				return null;
			}
		},
		getComboxDataOptions:function(fieldData){//获取Combox执行参数
			var data={},_self=this;
			switch (parseInt(fieldData.dataSourceKindId)) {
			 case 2: //集合
				var dataSource=_self.getArrayDataSource(fieldData);
				//转化数组为json对象
				$.each(dataSource,function(i,o){
					data[o]=o;
				});
				return {data:data};
                break;
			 case 3: //数据字典
				 data=_self.getJSONDataSource(fieldData);
				 return {data:data};
				 break;
			 case 4: //JSON
				 data=_self.getJSONDataSource(fieldData);
				 if(data.url&&data.mode){//读取远程数据
					data=$.extend({
						mode : 'remote',
						url  : ComboxNamespace.dictionaryUrl,//默认查询地址
						queryDelay:1000,
						maxHeight:250,
						onDefaultSelected:ComboxNamespace.defaultSelected
					 },data);
					 return data;
				 }else if(!$.isEmptyObject(data)){
					 return {data:data};
				 }
                break;
			}
		},
		getSelectDataOptions:function(fieldData,container){//获取select or lookup执行执行参数
			var containerId=container.attr('id');
			var dataOptions=this.getJSONDataSource(fieldData);//只能从Json中获取数据
			if(dataOptions.name){//存在 name
				if(dataOptions.back){//输入形式入:type:'sys',name:'flexFieldDefine',back:{defineId:'003',fieldCname:'select'}
					dataOptions['callBackControls']={};
					$.each(dataOptions.back,function(p,o){
						dataOptions['callBackControls'][p]='#'+containerId+' input[fieldName="'+o+'"]';
					});
					delete dataOptions['back'];
				}
				var getParam=dataOptions.getParam;
				if(getParam){
					if($.isPlainObject(getParam)){
						var fn=function(par,div){
							return function(){
								var param={};
								$.each(par,function(p,o){
	                				param[p]=$('input[fieldName="'+o+'"]',div).val();
	                			});
	                			return param;
							};
            			};
            			dataOptions['getParam']=fn(getParam,container);
					}
				}
				if(parseInt(fieldData.controlType)!=8){
					dataOptions['fieldValue']=fieldData['fieldValue'];
					dataOptions['lookUpValue']=fieldData['lookUpValue'];
				}
			}else{
				dataOptions=null;
			}
			return dataOptions;
		},
		getLookUpValue:function(groupDiv,name){//获取关联字段的值
			var lookUp=$(groupDiv).find('input[name="'+name+'_text"]');
			if(lookUp.length>0){
				return lookUp.getValue();
			}else{
				return '';
			}
		},
		getFieldValues:function(isEncode){//获取扩展字段数据
			isEncode=typeof isEncode=='boolean'?isEncode:true;//默认转码
			var opt=this.options,_self=this,fields=[],flag=false;
			this.element.find('div.flexFieldDiv').each(function(){
				var groupDiv=$(this),els = $(":input",groupDiv),el, name,values=[],processed={};
				var groupId=groupDiv.attr('groupId');
				for ( var i = 0, length = els.length; i < length; i++) {
					//el = els.get(i),name = el.name;
					el = els.get(i);
					name = el.name;
					if (!name || processed[name]||name.endsWith('_text')){
						continue;
					}
					flag = $(el).defaultCheckVal();
					if (flag === false){// 验证不通过
						return false;
					}else{
						flag = true;
					}
					values.push({
						id: $(el).attr('storageId'),
						flexFieldDefinitionId: name.replace('f_',''),//字段name统计加入前缀f_
						fieldValue:$(el).getValue(),
						flexFieldGroupId: groupId,
						fieldName:$(el).attr('fieldName'),
						lookUpValue:_self.getLookUpValue(groupDiv, name)
					});
					processed[name] = true;
				}
				fields.push.apply(fields,values);
			});
			if(!flag){
				return false;
			}else{
				return isEncode ? {flexField_: Public.encodeJSONURI(fields)}:fields;
			}
		}
	});
})(jQuery);
//解析内容替换	input对象
function parseFlexFieldHtmlToView(el){
	var obj=$(el).data('ui-flex-field');
	if(!obj) return '';
	if($(el).html()=='') return '';
	var clearItem = $('#clear-use-memory');
	if (clearItem.length == 0) {
		jQuery('<div/>').hide().attr('id', 'clear-use-memory').appendTo('body');
		clearItem = jQuery('#clear-use-memory');
	}
	clearItem.empty().append($(el).clone());
	clearItem.find('div[bizCode]').width('100%');
	//删除不显示的对象
	clearItem.find('font').remove();
	clearItem.find('a').remove();
	//只读不删除
	clearItem.find('input.ui-combox-element').not('input.textReadonly').remove();
	//替换输入框
	clearItem.find('input:text').each(function(){
		var parent=$(this).parent();
		var div=$('<div class="textLabel"></div>').html($(this).val());
		if(parent.hasClass('ui-combox-wrap')){
			div.insertAfter(parent);
		}else{
			div.insertAfter($(this));
		}
	});
	//替换单选
	clearItem.find('input:radio').each(function(){
		$('<span class="radio'+($(this).is(':checked')?'checked':'')+'"></span>').insertAfter($(this));
	});
	//替换复选
	clearItem.find('input:checkbox').each(function(){
		$('<span class="checkbox'+($(this).is(':checked')?'checked':'')+'"></span>').insertAfter($(this));
	});
	//删除输入框对象
	clearItem.find('div.ui-combox-wrap').remove();
	clearItem.find('input').remove();
	return clearItem.html();
}