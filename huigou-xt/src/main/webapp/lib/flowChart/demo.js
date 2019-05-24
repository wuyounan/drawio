var gridManager=null;
$(document).ready(function() {
	initializeUI();
	bindEvent();
});

function initializeUI(){
	 UICtrl.layout("#layout", {leftWidth :420,heightDiff : -5,
		 onSizeChanged:function(){
	    	var width=$('#chartCenter').width();
	    	var height=$('#chartCenter').height();
	    	 $('#flowChartDiv').flowChart('reInitSize',width,height);
	    }
	 });
	 var width=$('#chartCenter').width();
 	 var height=$('#chartCenter').height();
	 $('#flowChartDiv').flowChart({width:width,height:height,onBtnSaveClick:function(){
		 saveAll();
	 }});
}

function bindEvent(){
	Public.ajax(web_app.name + '/flowNode/queryFlowNode.ajax', {}, function (data) {
		$('#flowChartDiv').flowChart('initData',data);
	});
	
}

function saveAll(){
	var datas=$('#flowChartDiv').flowChart('exportData');
	var nodes=[],lines=[];
	$.each(datas.nodes,function(id,node){
		node['id']=id;
		nodes.push(node);
	});
	$.each(datas.lines,function(id,line){
		line['id']=id;
		lines.push(line);
	});
	Public.ajax(web_app.name + '/flowNode/saveFlowNode.ajax', {nodes:Public.encodeJSONURI(nodes),lines:Public.encodeJSONURI(lines)}, function (data) {
		bindEvent();
	});
}
