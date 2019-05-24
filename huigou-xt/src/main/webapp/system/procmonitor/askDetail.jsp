<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/system/procmonitor/askDetail.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/system/procmonitor/askUtil.js"/>'
	type="text/javascript"></script>
</head>
<div class="container-fluid">
	<div class="hg-form" method="post" action="" id="submitForm">
		<x:hidden name="procId" id="procId" />
		<div id='infoTab' style="margin: 5px; width: 98.5%;">
			<div class="ui-tab-links">
				<ul id="menu_ul">
					<li id="biz_tab" divid="biz_div">业务信息</li>
					<li id="ask_tab" divid="ask_div">询问</li>
				</ul>
			</div>
			<div class="ui-tab-content" style="margin: 0 auto;">
				<div class="layout" id='biz_div' style="height: 100%;">
					<iframe id="flowIframe" frameborder="0"
						style="width: 100%; height: 500px; padding: 0px;"></iframe>
				</div>
				<div class="layout" id='ask_div' style="height: 100%;">
					<x:hidden name="id" id="id" />
					<x:hidden name="mode" id="mode" />
					<x:hidden name="bizId" id="bizId" />
					<x:hidden name="kindId" id="kindId" />
					<x:hidden name="askStatus" id="askStatus" />
					<x:hidden name="askDetailId" id="askDetailId" />
					<x:hidden name="procId" id="procId" />
					<div class="hg-form-row">
						<x:inputC name="bizTitle" id="bizTitle" disabled="true"
							label="业务标题" />
						<x:inputC name="samplingTime" disabled="true" label="抽检时间" />
					</div>
					<div class="hg-form-row">
						<x:inputC name="statusName" id="title" disabled="true"
							label="询问状态" />
						<x:inputC name="sponsor" disabled="true" label="询问发起人" />
					</div>
					<div class="blank_div clearfix"></div>
					<div id="maingrid" style="margin: 0px;"></div>
					<div class="blank_div clearfix"></div>
					<div id="ask" style="display: none;">
						<x:hidden name="accepterId" />
						<div class="hg-form-row">
							<x:inputC name="accepterName" id="accepterName" label="接受讯问人员" />
						</div>
						<div class="hg-form-row">
							<x:textareaC name="askContent" label="我的问题" rows="5" />
						</div>
						<div class="blank_div clearfix"></div>
						<span class="ui-button" style="margin-left: 200px"> <span
							class="ui-button-left"></span> <span class="ui-button-right"></span>
							<input name="" value="提 交" class="ui-button-inner"
							onclick="submitAsk()" hidefocus="" type="button">
						</span>
						<div class="blank_div"></div>
					</div>

					<div id="ask_reply" style="display: none;">
						<div class="hg-form-row">
							<x:textareaC name="replyContent" label="回复内容" rows="5"
								cssStyle="height:150px;" />
						</div>
						<div class="hg-form-row">
							</span> <span class="ui-button" style="margin-left: 200px"> <span
								class="ui-button-left"></span> <span class="ui-button-right"></span>
								<input name="" value="提 交" class="ui-button-inner"
								onclick="replyAsk()" hidefocus="" type="button">
							</span>
						</div>

						<div id="ask_end" style="display: none;">
							<div class="hg-form-row">
								<x:textareaC name="summaryContent" label="总结内容" rows="5"
									cssStyle="height:150px;" />
							</div>
							</table>
							<span class="ui-button" style="margin-left: 200px"> <span
								class="ui-button-left"></span> <span class="ui-button-right"></span>
								<input name="" value="终 结" class="ui-button-inner"
								onclick="stopAsk()" hidefocus="" type="button">
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
