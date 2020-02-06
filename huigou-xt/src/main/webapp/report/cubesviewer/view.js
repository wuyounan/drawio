var cubeCodesParam;
$(function(){
	
	getQueryParameters();
	initCubesViewer();
    loadData();
    
    function getQueryParameters(){
    	cubeCodesParam = Public.getQueryStringByName("cubesViewCodes");
    	if (Public.isBlank(cubeCodesParam)){
    		Public.errorTip("参数“cubeCodes”不能为空。");
    		return;
    	}
    }
    
    function initCubesViewer(){
    	var cubesUrl = $("#cubesUrl").val();
        cubesviewer.init({ cubesUrl: cubesUrl, backendUrl: cubesUrl, cubeCode: cubeCodesParam, actionPostfix: ".ajax", debug: true });
    }
    
    function loadData(){
    	var cubeCodes = cubeCodesParam.split(",");
    	Public.ajax(web_app.name + '/cubesViewer/getCubesViewerDefinition.ajax',
    			{ cubeCodes: cubeCodes },
    	    	function (data) {
    	    		$("#cubeName").html(data.cubeName);
    	    		buildCubesView(data.cubes); 
    	});
    }
    
    function buildCubesView(cubes){
    	var html = [];
    	
    	for(var i = 0; i < cubes.length; i++){
    		 html.push('<div style="border: none; margin-bottom: 22px;">');
    		 html.push('<div id="cv-view-' + i + '" style="width: 100%; min-height: 120px;">正在读数，请稍候...</div>');
     		 html.push('</div>');
    	}
    	
    	$("#contentWrapper").html(html.join(''));
    	
    	cubesviewer.apply(function(){
    		var views = [];
    		var cubeViewPefix = "#cv-view-";
    		for (var i = 0; i < cubes.length; i++ ){
    			views[i] = cubesviewer.createView(cubeViewPefix + i , "cube", cubes[i]);
    		}    		
    	});
    }
});