<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="initial-scale=1.0, target-densitydpi=device-dpi" />
<meta name="viewport" content="initial-scale=1.0, width=device-height">
<script src='<c:url value="/system/writeSignature/writeSignature.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/writeSignature/bizWriteSignature.js"/>' type="text/javascript"></script>
<style type="text/css">
    html,body{font-size:62.5%;}
    div,body,p,span,ul,li{margin:0;padding:0;}
    .textC{text-align:center;}
    .mr15{margin-right:15px;}
    .mt10{
        margin-top:10px;
    }
    .dis-inline{
        display:inline-block;
    }
    .unvisible{
        visibility:hidden;
    }
    .visible{
        visibility:visible;
    }
    #signatureWrite-block{
        width:100%;
        height:auto;
        overflow-y:auto;
        overflow-x:hidden;
    }
    #signatureWrite-content{
        /*background-color:#eaeaea;*/
        background-color:#ccc;
    }
    #footer-content{
        position:fixed;
        bottom:0;
        left:0;
        right:0;
        margin:0 auto;
        padding-top:17px;
        border-top: 1px solid #acabac;
        background-color: #f8f8f8;
        min-height:42px;
        z-index:1;
    }
    .baseBtn{
        display:inline-block;
        padding: 4px 12px;
        background-color: #2aaaff;
        border: #00aaff 1px solid;
        cursor:pointer;
        text-align:center;
        font-size:1.4rem;
        border-radius:5px;
        -moz-border-radius:5px;  
        -webkit-border-radius:5px;
        color:#fff;
    }
    .baseBtn:hover{
        opacity: 0.8;
        -moz-opacity: 0.8;
        filter: alpha(opacity=80);
    }
    .btn-default{
        background-color: #ff7f2a;
        border: #ff7f00 1px solid;
        color:#fff;
    }
    #js_scroll{
        position:fixed;
        right:0;
        bottom:100px;
        z-index:1;
        width:32px;
        height:100px;
        background-color:#000;
        opacity: 0.5;
        -moz-opacity: 0.5;
        filter: alpha(opacity=50);
        display:none; 
    }
    .barBtn{
        cursor:pointer;
        display:inline-block;
        width:30px;
        height:30px;
    }
    #js_scroll_up{
        background: url(system/writeSignature/image/arrow_up.png) no-repeat 0 9px;
        padding:10px 0;
        border-bottom:#ccc 1px groove;
    }
    #js_scroll_down{
        background: url(system/writeSignature/image/arrow_down.png) no-repeat 0 8px;
        padding:10px 0;
    }
</style>
<title>手写输入</title>
</head>
<body>
  <div id="signatureWrite-block" class="signature-block">
    <div class="body-content-container">
        <div id="signatureWrite-content" class="mt10" ></div>
        <div id="footer-content" class="textC">
            <div class="dis-inline">
            	<span title="生成签名" class="signatureSave baseBtn btn-default">生成签名</span>
            	<span class="signatureReset baseBtn">清空</span>
                <span class="signaturAddpage baseBtn">加长签字板</span>
                <span class="signaturDelpage baseBtn">减短签字板</span>
                <div class="dis-inline jSignatureToolsBar"></div>
            </div>
        </div>
    </div>
    <div id="js_scroll">
        <span id="js_scroll_up" class="barBtn" title="向上"></span>
        <span id="js_scroll_down" class="barBtn" title="向下"></span>
    </div>
  </div>
</body>
</html>