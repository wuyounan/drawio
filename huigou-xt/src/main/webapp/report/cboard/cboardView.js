var tableQueryDefinitions = [];
var pageParam = { url: "" };
var uiParamKind = {
		BIZ_ORG_TREE_VIEW: "BIZ_ORG_TREE_VIEW",
		DATE_PICKER: "DATE_PICKER",
		MONTH_PICKER: "MONTH_PICKER",
		MANUAL_INPUT: "MANUAL_INPUT",
		SELECT_INPUT: "SELECT_INPUT"
};

$(function() {	
	var cboardCode; //业务编码	

	var inputParams = { 
			doQuery: false,
			comp_id: "",  //地市名
			comp_name: "",  //地市名
			organ_id: "", //县区名称
			organ_name: "" //县区名称
			//year_month: "",     //业务时间
			//start_year_month: "", 
			//end_year_month: ""
	};
	
	var cboardDefinition; //Cboard定义
	
	getQueryParameters();
	
	loadCboardDefinition();
	
	pageParam.url = web_app.name + "/cboard/getAggregateData.ajax";
	
	/**
	 * 获取查询字符串参数
	 */
	function getQueryParameters() {
		cboardCode = Public.getQueryStringByName("cboardCode");
		//组织
		inputParams.comp_id = Public.getQueryStringByName("bizOrgId");
		inputParams.comp_name = Public.getQueryStringByName("bizOrgName");
		inputParams.start_year_month = Public.getQueryStringByName("startYearMonth");
		inputParams.end_year_month = Public.getQueryStringByName("endYearMonth");
		inputParams.doQuery = Public.getQueryStringByName("doQuery");
	}
	
	/**
	 * 获取指标预警明细定义
	 */
	function loadCboardDefinition(){
		 Public.ajax(web_app.name + '/cboardDefinition/loadCboardDefinitionByCode.ajax', { code: cboardCode }, function (result) {
		        if (!result){
		        	return;
		        }		        
		        cboardDefinition = result;
		        createUIParams(result.uiparams);
		        createTables(result.tables);
		        if (inputParams.doQuery){
		        	query("#queryMainForm");
		        }
		    });
	}

    /**
    获取定义
    */
	function findTableByCode(code){
		var result = null;	
		//查找表格
		var tables = cboardDefinition.tables;
		if (!tables){
			return result;
		}
		$.each(tables, function (i, o) {
        	if (o.code === code) {
                result = o;
                return false;
            }
        });
       return result;
	}
    
	function setObjectInitValue(object){
		var id = object.attr("id");
    	if (inputParams[id]){
    		object.val(inputParams[id]);
    	}		
	}
	
    function createUIParams(uiParams){
    	if (!uiParams || uiParams.length == 0){
    		return;
    	}
    	var uiParamHtml = [];
    	
    	uiParamHtml.push('<div id="navTitle" class="navTitle row-fluid">');
    	uiParamHtml.push('  <a href="javascript:void(0);" hidefocus=""');
    	uiParamHtml.push('	   class="togglebtn" hidetable="#queryMainForm" hideindex=""');
    	uiParamHtml.push('	   title="show or hide"> <i class="fa fa-angle-double-down"></i>');
    	uiParamHtml.push('  </a>');
    	uiParamHtml.push('  <i class="fa fa-search"></i>&nbsp; <span class="titleSpan">搜索</span>');
    	uiParamHtml.push('</div>');    
    	
    	uiParamHtml.push('<div class="navline"></div>');    	
    	
    	uiParamHtml.push('<form class="hg-form" method="post" action="" id="queryMainForm">');
    	uiParamHtml.push('		<div class="hg-form-row">');
    	
        $.each(uiParams, function(i, o){
        	uiParamHtml.push('			<div class="col-xs-4 col-sm-1">');
        	uiParamHtml.push('				<label class="hg-form-label required" id="'+ o.code  +'"_label" title="'+ o.name +'">' + o.name+ '&nbsp;:</label>');
        	uiParamHtml.push('			</div>');
        	uiParamHtml.push('			<div class="col-xs-8 col-sm-2 col-white-bg">');
        	uiParamHtml.push('				<input type="text" ctlKind="' + o.uiParamKind + '" name="' + o.code + '" id="' + o.code + '" label="' + o.name + '" required="true">');
        	uiParamHtml.push('			</div>');
        	
        	inputParams[o.code] = inputParams[o.code] || "";
        });				
    				
    	uiParamHtml.push('			</div>');
    	uiParamHtml.push('			<div class="col-xs-12 col-sm-3">');
    	uiParamHtml.push('				<button type="button" class="btn btn-primary"');
    	uiParamHtml.push('					onclick="query(this.form)">');
    	uiParamHtml.push('					<i class="fa fa-search"></i>&nbsp;查询');
    	uiParamHtml.push('				</button>');
    	uiParamHtml.push('				&nbsp;&nbsp;');
    	uiParamHtml.push('				<button type="button" class="btn btn-primary"');
    	uiParamHtml.push('					onclick="resetForm(this.form)">');
    	uiParamHtml.push('					<i class="fa fa-history"></i>&nbsp;重置');
    	uiParamHtml.push('				</button>');
    	uiParamHtml.push('				&nbsp;&nbsp;');
    	uiParamHtml.push('			</div>');
    	uiParamHtml.push('		</div>');
    	uiParamHtml.push('</form>');
    	
        $("#params").html(uiParamHtml.join(" "));     
        //日期
        $("input[ctlKind='"+ uiParamKind.DATE_PICKER +"']").each(function(i,o){
        	$(this).datepicker().mask('9999-99-99');
        	setObjectInitValue($(this));
        });
        
        //年月
        $("input[ctlKind='"+ uiParamKind.MONTH_PICKER +"']").each(function(i, o){
        	$(this).monthpicker().mask('9999-99');
        	setObjectInitValue($(this));
        });
        var bizOrgId = $("#bizOrgId").val();
        //组织
        $("input[ctlKind='"+ uiParamKind.BIZ_ORG_TREE_VIEW +"']").treebox({
        	type: 'hana',
        	name: 'bizOrgTreeForCity',
        	param: { rootParentId: bizOrgId, parentId: bizOrgId,  businessType: 'CIGARETTE' },
        	width: 300
        });
        //下拉列表
        $("input[ctlKind='"+ uiParamKind.SELECT_INPUT +"']").combox();
        $("input[ctlKind='"+ uiParamKind.SELECT_INPUT +"']").each(function(i, o){
        	$.mcs.loadCboardData(o.name, {}, function (data, table) {
                var columns = JSON.parse("{" + table.tableColumnsJson + "}").columns;
                if(!columns[0].field) return;
        		var selectData = new Array();
        		$.each(data, function(j, p){
        			selectData[selectData.length] = p[columns[0].field];
                });
        		$("input[ctlKind='"+ uiParamKind.SELECT_INPUT +"'][name='"+ o.name +"']").combox('setData',selectData);
            });
        });

        UICtrl.autoGroupAreaToggle($("#params"));
    }
    
    function getTableCascade(tableId){
        var table = findTableByCode(tableId);
        if (!table){
        	return;
        }
        var cascade = table.cascade;
        cascade = $.parseJSON("{" + cascade + "}");
        if (!cascade || !cascade.cascade){
        	return;
        } 
        cascade = cascade.cascade;
        return cascade;
    }
    
    function createTables(tables) {    	
        var tabHtml = [], gridHtml = [];
        tabHtml.push('<ul id="content-wrapper" class="nav nav-tabs">');
        gridHtml.push('<div id="tab-content" class="tab-content">');
        $.each(tables, function (i, o) {
            if (i == 0) {
                tabHtml.push('<li id="li_' + o.code + '" class="active">');
            } else {
                tabHtml.push('<li id="li_' + o.code + '">');
            }
            tabHtml.push('<a href="#tb-' + o.code + '-wrapper" data-toggle="tab">');
            tabHtml.push(o.name);
            tabHtml.push('</a>');
            tabHtml.push('</li>');

            if (i == 0) {
                gridHtml.push('<div class="tab-pane fade in active" id="tb-' + o.code + '-wrapper">');
            } else {
                gridHtml.push('<div class="tab-pane fade" id="tb-' + o.code + '-wrapper">');
            }
            gridHtml.push('<table id="tb-' + o.code + '"></table>');
            gridHtml.push('</div>');

        });
        tabHtml.push("</ul>");

        gridHtml.push('</div>');

        $("#tables").html(tabHtml.join(" ") + gridHtml.join(" "));

        $.each(tables, function (i, o) {
        	var tableColumnsJson = JSON.parse("{" + o.tableColumnsJson + "}");
            var tableId = "#tb-" + o.code;
            var queryParams = { definitionId: cboardDefinition.id, tableId: o.id  };
            var cascade = getTableCascade(o.code);
        	
            if (cascade){
        		//跳转参数-->H2Dataset filter
        		var fromQueryParams = cascade.fromQueryParams;
        		var splits, cascadeParams ;
        		if (!Public.isBlank(fromQueryParams)){
        			cascadeParams = {};
        			splits = fromQueryParams.split(",");
        			$.each(splits, function(i, o){
                		cascadeParams[o] = inputParams[o]; 
                	});            			
        		}
        		//跳转参数-->Source Dataset query parameter
        		var datasetQueryParams = cascade.datasetQueryParams;
        		if (!Public.isBlank(datasetQueryParams)){
        			cascadeParams = cascadeParams || {};
        			cascadeParams.datasetQueryParams = {};
        			splits = datasetQueryParams.split(",");
        			$.each(splits, function(i, o){
                		cascadeParams.datasetQueryParams[o] = inputParams[o]; 
                	});
        		}
        		queryParams.cascadeParams  = cascadeParams;
        	}
            
            var sortName="", sortOrder="asc";
            if (o.sortJson){
            	var sort = $.parseJSON("{" + o.sortJson + "}").sort;
            	sortName = sort.name;
            	sortOrder = sort.order;
            }
            tableQueryDefinitions.push({ tableId: tableId , queryParams: queryParams});
            
            $(tableId).bootstrapTable({
                dataType: "json",
                method: 'get',
                cache: false,                
                toolbarAlign: "right",
                striped: true,                      //是否显示行间隔色
                //cache: false,                     //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: true,                     //是否启用排序
                sortName: sortName,
                sortOrder: sortOrder,                   //排序方式
                queryParams: queryParams, //传递参数（*）
                sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
                search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: false,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "",                     //每一行的唯一标识，一般为主键列
                showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                showExport: true,                     //是否显示导出
		        exportDataType: "all",
		        exportTypes:["excel"],
                columns: tableColumnsJson.columns,                
                onDblClickCell: function (field, value, row, $element) {
                    var isBaseInfo = false;
                    switch (field) {
                        case "cust_code":
                        case "cust_name":
                        	var customerId = row.cust_id || row.cust_code;
                        	if (customerId){
                        		$.showCustomer({params: { customerId: customerId }});
                        		isBaseInfo = true;
                        	}
                    		break;
                        case "item_code":
                        case "item_name":
                        	var itemId = row.item_id || row.item_code;
                        	var compId = row.organ_id || row.comp_id;
                        	if (itemId){
                        		$.showItem({ params: { itemId: itemId, compId: compId } });
                        		isBaseInfo = true;
                        		isBaseInfo = true;
                        	}
                    		break;
                        case "order_number":
                        	if (row.order_number){
                        		$.showSaleOrder({params: { orderNumber: row.order_number }});
                        		isBaseInfo = true;
                        	}
                            break;
                    }
                    if (isBaseInfo) {
                        return;
                    }
                    
                    var tableId = $element.parents("table").attr("id");
                    
                    var cascade = getTableCascade(tableId.substr(3));
                    if (!cascade) return;

                    if (cascade.code) {
                    	var formParams = $("#queryMainForm").formToJSON({encode: false});
                    	
                    	var selector = '#content-wrapper #li_' + cascade.code + ' a';
                    	var cascadeParams = {};
                    	var cascadeFields = cascade.fields;

                    	//from Query Parameter and dataset row --> cascadeParams 
                    	var splits;
                    	var fromQueryParams = cascade.fromQueryParams;
                		if (!Public.isBlank(fromQueryParams)){
                			splits = fromQueryParams.split(",");
                			$.each(splits, function(i, o){
                        		cascadeParams[o] = formParams[o] || inputParams[o]; 
                        	});
                		}
                    	
                    	splits = cascadeFields.split(",");
                    	$.each(splits, function(i, o){
                    		cascadeParams[o] = row[o]; 
                    	});
                    	//from Query Parameter and dataset row--> cascadeParams.datasetQueryParams
                    	var datasetQueryParams = cascade.datasetQueryParams;
                		if (!Public.isBlank(datasetQueryParams)){
                			splits = datasetQueryParams.split(",");                			
                			cascadeParams.datasetQueryParams = {};
                			$.each(splits, function(i, o){
                				cascadeParams.datasetQueryParams[o] = row[o] || formParams[o] || inputParams[o]; 
                        	});
                			covertQueryParam(cascadeParams.datasetQueryParams);
                		}
                		
                    	$(selector).bsTab('show');                    	
                        $("#tb-" + cascade.code).bootstrapTable("refresh", {url: pageParam.url, query: {cascadeParams: Public.encodeJSONURI(cascadeParams)}});
                    }
                }
            });
        });
    }
});

/**
 * 转换查询参数
 * @returns
 */
function covertQueryParam(datasetQueryParams){
	//年月 yyyy-mm -->yyyymm
	var ctrlId, yearMonth;
	$("input[ctlKind='"+ uiParamKind.MONTH_PICKER +"']").each(function(i, o){
		ctrlId = $(o).attr("id");
		if (datasetQueryParams.hasOwnProperty(ctrlId)){
			yearMonth = datasetQueryParams[ctrlId];
			if (!Public.isBlank(yearMonth)){
				datasetQueryParams[ctrlId] = yearMonth.replace(/-/g, '');
			}
		}
	});
}

//查询
function query(obj) {
	var params = $(obj).formToJSON({encode: false});
	if (!params){
		return;
	}
	
	var tableQueryDefinition;
	
	var localQueryParams = {};
	$.each(tableQueryDefinitions, function(i, o){
		tableQueryDefinition = o;
		$.extend(true, localQueryParams, tableQueryDefinition.queryParams);
		
		localQueryParams.cascadeParams = localQueryParams.cascadeParams || {};
		localQueryParams.cascadeParams.datasetQueryParams = localQueryParams.cascadeParams.datasetQueryParams || {};
		for (var key in params){
			localQueryParams.cascadeParams.datasetQueryParams[key] = params[key];
		}
		covertQueryParam(localQueryParams.cascadeParams.datasetQueryParams);
		
		$(tableQueryDefinition.tableId).bootstrapTable("refresh", {url: pageParam.url, query: {cascadeParams: Public.encodeJSONURI(localQueryParams.cascadeParams)} });
		
		tableQueryDefinition.queryParams = localQueryParams;
	});
}

function formatterHandler(value, row, index) {
	if (value=="#NULL") return "";
	return value;
}
//重置表单
function resetForm(obj) {
	$(obj).formClean();
}

