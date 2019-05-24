<#if parameters.base?default(false)>
	<link href='${parameters.webApp?default("")?html}/themes/bootstrap/css/bootstrap.min.css' rel='stylesheet' type='text/css'/>
	<link href='${parameters.webApp?default("")?html}/themes/fontawesome/css/font-awesome.min.css' rel='stylesheet' type='text/css'/>
	<link href='${parameters.webApp?default("")?html}/themes/css/style.css?v=20181010' rel='stylesheet' type='text/css'/>
	<link href='${parameters.webApp?default("")?html}/themes/css/ui.css?v=20181009' rel='stylesheet' type='text/css'/>
	<script src='${parameters.webApp?default("")?html}/javaScript/WEB_APP.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.min.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.json-2.4.min.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.i18n.properties.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.utils.js?v=20180518' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/bootstrap/bootstrap.min.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.md5.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.maskinput.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.textarea.js?v=20181009' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.spinner.js?v=20181012' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.dragEvent.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.chineseletter.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.tab.js?v=20180327' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.tooltip.js?v=20181008' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.toolBar.js?v=20180601' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.formButton.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.form.js?v=20180319' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.contextmenu.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.tip.js?v=20180319' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquey.scrollLoad.js?v=20181011' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/ligerUI/core/base.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/plugins/screenfull.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/javaScript/common.js?v=20181013' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/javaScript/extend.js?v=20181121' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/javaScript/UICtrl.js?v=20181018' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/i18n.load' type='text/javascript'></script>
</#if>
<#if parameters.dialog?default(false)>
	<link href='${parameters.webApp?default("")?html}/themes/lhgdialog/idialog.css' rel='stylesheet' type='text/css'/>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/lhgdialog.js?v=20181012' type='text/javascript'></script>
</#if>
<#if parameters.grid?default(false)>
	<link href='${parameters.webApp?default("")?html}/themes/ligerUI/Aqua/css/ligerui-grid.css?v=20181217' rel='stylesheet' type='text/css'/>
	<link href='${parameters.webApp?default("")?html}/themes/ligerUI/Gray/css/grid.css?v=20181207' rel='stylesheet' type='text/css'/>
	<script src='${parameters.webApp?default("")?html}/lib/ligerUI/plugins/ligerResizable.js' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/ligerUI/plugins/ligerGrid.js?v=20181218' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.gridExtend.js?v=20181218' type='text/javascript'></script>
</#if>
<#if parameters.tree?default(false)>
	<link href='${parameters.webApp?default("")?html}/themes/ligerUI/Aqua/css/ligerui-tree.css' rel='stylesheet' type='text/css'/>
	<script src='${parameters.webApp?default("")?html}/lib/ligerUI/plugins/ligerTree.js?v=20181108' type='text/javascript'></script>
</#if>
<#if parameters.fullcalendar?default(false)>
	<link href='${parameters.webApp?default("")?html}/themes/fullcalendar/fullcalendar.css' rel='stylesheet' type='text/css'/>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/fullcalendar.min.js' type='text/javascript'></script>
</#if>
<#if parameters.layout?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.layout.js?v=20181010' type='text/javascript'></script>
</#if>
<#if parameters.attachment?default(false)>
	<link href='${parameters.webApp?default("")?html}/themes/css/attachment/style.css?v=20181008' rel='stylesheet' type='text/css'/>
	<script src='${parameters.webApp?default("")?html}/lib/webUploader/webuploader.js?v=20181010' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/webUploader/jquery.webuploader.js?v=20181010' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.attachment.js?v=20181012' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.thickbox.js?v=20181008' type='text/javascript'></script>
</#if>
<#if parameters.dateTime?default(false)|| parameters.datetime?default(false)|| parameters.date?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.datepicker.js' type='text/javascript'></script>
</#if>
<#if parameters.monthpicker?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.monthpicker.js' type='text/javascript'></script>
</#if>
<#if parameters.combox?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.combox.js?v=20181019' type='text/javascript'></script>
</#if>
<#if parameters.commonTree?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.commonTree.js' type='text/javascript'></script>
</#if>
<#if parameters.comboDialog?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.comboDialog.js?v=20181217' type='text/javascript'></script>
</#if>
<#if parameters.formButton?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.formButton.js' type='text/javascript'></script>
</#if>
<#if parameters.flexField?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.flexField.js' type='text/javascript'></script>
</#if>
<#if parameters.floatdialog?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.floatdialog.js' type='text/javascript'></script>
</#if>
<#if parameters.selectOrg?default(false)>
	<script src='${parameters.webApp?default("")?html}/system/opm/organization/SelectOrgCommonPage.js?v=1' type='text/javascript'></script>
	<script src='${parameters.webApp?default("")?html}/system/opm/js/OpmUtil.js' type='text/javascript'></script>
</#if>
<#if parameters.fixedtable?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.fixedtable.js?v=20180630' type='text/javascript'></script>
</#if>
<#if parameters.echart?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/echart/echarts.min-3.6.2.js' type='text/javascript'></script>
</#if>
<#if parameters.excelImp?default(false)>
	<script src='${parameters.webApp?default("")?html}/system/excelimport/AssignCodeImp.js?v=20180725' type='text/javascript'></script>
</#if>
<#if parameters.dataFilter?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.dataFilter.js?v=20181019' type='text/javascript'></script>
</#if>
<#if parameters.wizard?default(false)>
	<script src='${parameters.webApp?default("")?html}/lib/jquery/jquery.wizard.js?v=20181119' type='text/javascript'></script>
</#if>