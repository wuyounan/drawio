var inputParams = {  comp_id: "", comp_name: "", start_date: "", end_date: ""};
var indexEntryId;
$(document).ready(function () {
	bindEvent();
});

function bindEvent(){
    /*$("#bizOrgName").treebox({
    	type: 'hana',
    	name: 'bizOrgTreeForCity',
    	param: { rootParentId: '10000', parentId: '10000',  businessType: 'CIGARETTE' },
		onChange: function(values, data) {
			$('#bizOrgId').val(data.orgId);
			$('#bizOrgName').val(data.orgName);
			$('#bizOrgName_text').val(data.orgName);
		}
    });*/
	$('#indexEntryName').searchbox({
        type: 'mcs',
        name: 'selectIndexEntry',
		onChange: function(values, data) {
			$('#indexEntryId').val(data.indexEntryId);
			$('#indexEntryName_text').val(data.indexName);
			$('#indexEntryTableId').val('');
			$('#indexEntryTableName_text').val('');
		}
    });
	$('#indexEntryTableName').searchbox({
        type: 'mcs',
        name: 'selectIndexEntryTab',
        getParam: function () {
        	var entryId = $('#indexEntryId').val();
        	if(Public.isBlank(entryId)){
        		Public.tip("请先选择指标。");
        		return false;
        	}
            return { indexEntryId : entryId };
        },
		onChange: function(values, data) {
			$('#indexEntryTableId').val(data.id);
			$('#indexEntryTableName_text').val(data.name);
		}
    });		
}

function getQueryParameters() {
	indexEntryId = $('#indexEntryId').val();
	inputParams.comp_id = $('#bizOrgId').val();
	inputParams.comp_name = $('#bizOrgName').val();
	inputParams.start_date = getStartDate();
	inputParams.end_date = getEndDate();
}

function loadIndexAlertDetailDefinition(){
	 Public.ajax(web_app.name + '/indexAlert/loadIndexAlertDetailDefinition.ajax', { indexEntryId: indexEntryId, bizOrgId: inputParams.comp_id }, function (result) {
	        if (!result) return;
			$.each(result.tabs, function(i, o){
				if(o.id == $('#indexEntryTableId').val()){
					loadDetailData(o);
				}
        	});  
	    });
}

function loadDetailData(tab){
    var queryParams = { indexEntryId: indexEntryId, indexEntryTabId: tab.id  };
    queryParams.startDate = inputParams.start_date;
    queryParams.endDate = inputParams.end_date;
    queryParams.bizOrgId = inputParams.comp_id;

    var cascade = tab.cascade;
    cascade = $.parseJSON("{" + cascade + "}");
    cascade = cascade.cascade;
	if (cascade){
		var fromQueryParams = cascade.fromQueryParams;
		var splits, cascadeParams;
		if (!Public.isBlank(fromQueryParams)){
			cascadeParams = {};
			splits = fromQueryParams.split(",");
			$.each(splits, function(i, o){
        		cascadeParams[o] = inputParams[o]; 
        	});            			
		}
		var datasetQueryParams = cascade.datasetQueryParams;
		if (!Public.isBlank(datasetQueryParams)){
			cascadeParams = cascadeParams || {};
			cascadeParams.datasetQueryParams = {};
			splits = datasetQueryParams.split(",");
			$.each(splits, function(i, o){
        		cascadeParams.datasetQueryParams[o] = inputParams[o]; 
        	});
		}
		queryParams.cascadeParams = Public.encodeJSONURI(cascadeParams);
	}
	
    var sortName="", sortOrder="asc";
    if (tab.sortJson){
    	var sort = $.parseJSON("{" + tab.sortJson + "}").sort;
    	sortName = sort.name;
    	sortOrder = sort.order;
    }
	var tableColumnsJson = JSON.parse("{" + tab.tableColumnsJson + "}");

	$("#maingrid").empty();		
	$("#maingrid").append('<table id="statisticsGrid"></table>');
    $("#statisticsGrid").bootstrapTable("destroy").bootstrapTable({
        url: web_app.name + "/indexAlert/getAggregateAllData.ajax",
        dataType: "json",
        method: 'get',
        cache: false,
        toolbarAlign: "right",
        striped: true,                      //是否显示行间隔色
        //cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
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
                case "cust_id":
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
                	}
            		break;
                case "order_number":
                case "co_num": 
                	if (row.order_number || row.co_num){
                		$.showSaleOrder({params: { orderNumber: row.order_number || row.co_num }});
                		isBaseInfo = true;
                	}
                    break;
            }
        }
    });
	
}

function getStartDate(){
	return $("#startDate").val();
}

function getEndDate(){
	return $("#endDate").val();
}

//查询
function query(obj) {
	var param = $(obj).formToJSON();
	if(param){
		getQueryParameters();
		loadIndexAlertDetailDefinition();
	}
}

//重置表单
function resetForm(obj) {
    $(obj).formClean();
}