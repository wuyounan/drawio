<#-- 定义宏formatdate-->
<#macro formatDate date>
<#if date??&&date?string!=''>
${date?string("yyyy-MM-dd")}<#rt/>
</#if>
</#macro>
<#macro formatDateTime date>
<#if date??&&date?string!=''>
${date?string("yyyy-MM-dd HH:mm:ss")}<#rt/>
</#if>
</#macro>
<style type="text/css">
	body {font-family: SimSun;font-size:11pt;}
	.billTitle{text-align:center;font-weight:bold;font-size:30px;}
	.center{text-align:center;}
	.left{text-align:left;}
	.right{text-align:right;}
	.fontBold{font-family:"Microsoft YaHei",SimSun;font-weight:bold;}
	.navTitle{font-family:"Microsoft YaHei",SimSun;font-weight:bold;margin:5px;padding-bottom:5px;font-size:13pt;text-align:left;border-bottom:1px dotted #000000;}
    .navLine{ border-bottom: 1px solid #000000;}
    .breakWord{width:100%;word-break:break-all;word-wrap: break-word;}
    .backgroundGray{background:#E4E4E4;}
    .height_40{height:40px;}
    .radio, .checkbox, .radio-checked, .checkbox-checked {
	    height: 13px;
	    width: 13px;
	    display: inline-block;
	    margin-right: 5px;
	}
	.radio, .radio-checked {
	    background: url('${imgHttpUrl?default("")?html}/themes/css/ui/radio.gif') no-repeat 0 0;
	}
	.checkbox, .checkbox-checked {
	    background: url('${imgHttpUrl?default("")?html}/themes/css/ui/checkbox.gif') no-repeat 0 0;
	}
	.checkbox-checked, .radio-checked {
	    background-position: -13px -13px;
	}
    .font_sm,.font_sm td{
  		font-size:12px;
  	}
    table.tablePrint{
    	width:100%;
    	table-layout:fixed; 
    	word-break:break-strict;
        border:0px;
    }
    table.tablePrint td{
    	height:28px;
       	word-wrap: break-word; 
       	word-break:break-strict;
        padding-left:5px;
    }
    table.tableBorder {
		width: 100%;
		table-layout: fixed;
		word-break:break-strict;
		border: 1px solid #000;
		border-width: 1px 0 0 1px;
		border-collapse: collapse;
		position: relative;
	}	
	table.tableBorder td {
		border: 1px solid #000;
		border-width: 0 1px 1px 0;
		padding: 2px 3px 1px 2px;
		height: auto;
		vertical-align: middle;
		word-wrap:break-word;
		word-break:break-strict;
		min-height: 23px;
	}
	table.tableBorder td.title{
		font-family:"Microsoft YaHei",SimSun;
		text-align:center;
		font-weight:bold;
		background:#E4E4E4;
	}
	table.tableBorder td span.labelSpan {
		position: relative;
		left: 10%;
	}
	.blank_div {
		text-align: center;
		height: 5px;
		clear: both;
	}
    @page {
    	size: 210mm 297mm; 
		margin:5mm 3mm; 
    } 
</style> 
 