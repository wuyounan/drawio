var pageParam = { };
var changeTime = 0;
var clickNum = 0;
$(document).ready(function(){
	pageParam.procUnitHandlerId = Public.getQueryStringByName("procUnitHandlerId");
	pageParam.bizId = Public.getQueryStringByName("bizId");	
	pageParam.procUnitId = Public.getQueryStringByName("procUnitId");
	
	writeSignature(function(data, svg, width, height, svgheight){
    });
});

//签字板数据接口，返回内容的base64编码
function writeSignature(callback){ 
	//3种笔画粗细，默认使用最细的画笔
	var dotSize = [2, 3, 4];    
    var colorObj = {"black": "#000000", "blue": "#2da2fc", "red": "red"}; //颜色种类,默认使用红色
    var $Jsignature = $("#signatureWrite-content");
    var initPenColor = colorObj["black"];
    pageParam.operationKindId = "view";
    
    var initPenDot = dotSize[0];
    var ratio = $(document).height()-$("#footer-content").height() ;
    var ratioW = '100%';
    
    var svgheight = 170;
    var opinion30;
    Public.syncAjax(web_app.name + '/workflow/loadProcUnitHandlerMaunscript.ajax', {
        procUnitHandlerId: pageParam.procUnitHandlerId }, function(data){
        	if (!Public.isBlank(data) && !Public.isBlank(data.opinion30)){
        		opinion30 = data.opinion30;
        		svgheight = data.height;
        	}
        });
    
    var signnatureOption = {
            'width':ratioW,
            'height':svgheight,
            'color': initPenColor,
            'decor-color': 'transparent',   //decor-color的颜色为透明
            'lineWidth': initPenDot,
            'UndoButton':true,
            'minFatFingerCompensation': -10
    };
    //初始化签字板
   $Jsignature.jSignature(signnatureOption); 
   
   if (opinion30){
	   setTimeout(function(){
	    	$Jsignature.jSignature("setData", "data:" + opinion30);
	   }, 1000);
   }
   
	//清空签字板
    $(".signatureReset").click(function(){  
        $Jsignature.jSignature('reset');
        $("#signatureWrite-content").empty();
        
        svgheight = 170;
        changeTime = 0;
        $Jsignature.jSignature({
            'width':ratioW,
            'height':svgheight,
            'color': initPenColor,
            'decor-color': 'transparent',   //decor-color的颜色为透明
            'lineWidth': initPenDot,
            'UndoButton':true,
            'minFatFingerCompensation': -10
    	});
    });
    
    $(".signaturAddpage").unbind('click').click(function(){ 
        changeTime++;
    	var imgData = $Jsignature.jSignature('getData', 'default');
        var datapair = $Jsignature.jSignature("getData","base30");
    	$Jsignature.jSignature('reset');
        //原始签字板清空
    	$("#signatureWrite-content").empty();
        svgheight = parseInt(svgheight) + changeTime * 170;
    	// 重新初始签字板，长度为原来长度的基础上加200px
        $Jsignature.jSignature({
            'width':ratioW,
            'height':svgheight,
            'color': initPenColor,
            'decor-color': 'transparent',   //decor-color的颜色为透明
            'lineWidth': initPenDot,
            'UndoButton':true,
            'minFatFingerCompensation': -10
    	});
        ratio = 170 + changeTime * 170;
      
        var chazhi = ratio  - $(window).height();
        if( $("#js_scroll").css("display") && chazhi>0){
            $("#js_scroll").show();
        }
        
        if(datapair){
		   $Jsignature.jSignature("setData", "data:" + datapair.join(","));
    	}       
    });
    
    $(".signaturDelpage").unbind('click').click(function(){
        if(changeTime > 0){
       		svgheight = parseInt(svgheight) - changeTime*170;
            changeTime--;
            var datapair = $Jsignature.jSignature("getData","base30");
            
            $Jsignature.jSignature('reset');
            $("#signatureWrite-content").empty();
            // 重新初始签字板，长度为原来长度的基础上加200px
            $Jsignature.jSignature({
                'width':ratioW,
                'height':svgheight,
                'color': initPenColor,
                'decor-color': 'transparent',   //decor-color的颜色为透明
                'lineWidth': initPenDot,
                'UndoButton':true,
                'minFatFingerCompensation': -10
            });
            ratio = 170 + changeTime * 170;
            
            var chazhi =ratio  - $(window).height();
            if( $("#js_scroll").css("display") && chazhi > 0){
                $("#js_scroll").show();
            }
           
            if(datapair){
            	$Jsignature.jSignature("setData", "data:" + datapair.join(","));
            } 
        }else{
            alert("当前画板已是最小长度。");
            return false;
        }
    });
    
    // 保存签字内容
    $(".signatureSave").unbind('click').click(function(){
        var opinion64 = $Jsignature.jSignature('getData', 'default');
        var opinion30 = $Jsignature.jSignature("getData","base30");        
        opinion30 =  opinion30.join(",");
        Public.ajax(web_app.name + '/workflow/saveProcUnitHandlerMaunscript.ajax', {
        	bizId: pageParam.bizId,
            procUnitHandlerId: pageParam.procUnitHandlerId,
            opinion64: opinion64,
            opinion30: opinion30,
            height: svgheight },function(data){
            	parent.writeSignatureDialog.opinion64 = opinion64;
            	parent.reLoadManuscript();
        	    parent.writeSignatureDialog.close();
           });
        });
    $("#js_scroll_down").unbind('click').click(function(){
        var mapNum = changeTime;
        var topValue = $(document).scrollTop();
        var dValue = $(document).height() - $(document).scrollTop(); 
        //页面有加长时点击模拟滚动条滚动
        if( changeTime > 0 && dValue > 0){
            clickNum++;
            $(document).scrollTop(100*clickNum);
        }else{
            return false;
        } 
    });

    //向上拉
    $("#js_scroll_up").unbind('click').click(function(){
        var startDist = ratio;
        var topValue = $(document).scrollTop();
        //页面有加长时点击模拟滚动条滚动
        if(topValue > 0){
            clickNum--;
            $(document).scrollTop(100*clickNum);
        }else{
            return false;
        }
    });

    window.addEventListener('orientationchange', function(){ window.location.reload(); }, false);
}


