<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List,java.util.Map" %>
<%@ page import="com.huigou.context.MessageSourceContext" %>
<%@ page import="com.huigou.context.ContextUtil" %>
<%@ page import="com.huigou.context.Operator" %>
<%@ page import="com.huigou.util.Constants" %>
<%@ page import="com.huigou.cache.DictUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1, shrink-to-fit=no"/>
	<meta name="format-detection" content="telephone=no,email=no,address=no"> 
	<!--[if lt IE 8]>
    <meta http-equiv="refresh" content="0;url='ie.html'"/>
    <![endif]-->
	<title><c:out value="${f:systemParameter('SYSTEM.NAME')}" /></title>
	<x:link href="/themes/font-face.css"/>
	<x:base include="base,dialog"/>
	<x:link href="/index/css/index.css"/>
	<x:link href="/themes/extend.css"/>
	<!--[if lt IE 9]>
	<x:script src='/lib/bootstrap/html5shiv.min.js'/>
	<x:script src='/lib/bootstrap/respond.min.js'/>
	<![endif]-->
	<x:script src='/common/Context.jsp'/>
	<x:script src='/lib/jquery/jquery.slimscroll.min.js'/>
	<x:script src='/lib/jquery/jquery.contenttabs.js'/>
	<x:script src='/lib/jquery/jquery.functionMenus.js'/>
	<x:script src='/index/js/index.plus.js'/>
	<x:script src='/system/userPanel/UpdatePassword.js'/>
	<x:script src='/index/js/index.js'/>
	<x:script src='/lib/jquery/jquery.base64.js'/>
</head>
<%
	Operator operator = (Operator) ContextUtil.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
	if (operator == null) {
	    response.sendRedirect("Login.jsp"); 
	    return;
    }
	String token = (String)ContextUtil.getSession().getAttribute(Constants.CSRF_TOKEN);
	if(token==null||token.equals("")){
	    response.sendRedirect("Login.jsp"); 
	    return;
	}
	MessageSourceContext.setLocale(request, response);
	request.setAttribute("token", token);
	List<Map<String, Object>> i18nLanguages=DictUtil.getDictionaryList("i18nLanguage");
	request.setAttribute("isI18nLanguage", false);
	if(i18nLanguages!=null&&i18nLanguages.size()>0){
	    request.setAttribute("isI18nLanguage", true);
		request.setAttribute("i18nLanguages", i18nLanguages);
	}  
%>
<body class="fixed-sidebar full-height-layout gray-bg">
	<input type="hidden" id="csrfTokenElement" value="<%=token%>"/>
	<div id="wrapper">
		<!--左侧导航开始-->
		<div class="navbar-default navbar-static-side" role="navigation">
			<div class="nav-close">
				<i class="fa fa-times-circle"></i>
			</div>
			<div class="sidebar-collapse">
				<ul class="nav" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element">
							<div style="height:64px;">
								<img alt="image" id="showPersonPicture" class="img-circle" style="display:none;" src="<c:url value="/images/head.png"/>" width="64" height="64"/>
							</div>
							<a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0);">
								<span class="block-overflow"> 
									<span class="block m-t-xs">
										<strong class="font-bold"><c:out value="${sessionScope.sessionOperatorAttribute.deptName}"/></strong>
									</span>
									<span class="text-xs block">
										<c:out value="${sessionScope.sessionOperatorAttribute.name}"/>
										<b class="caret"></b>
									</span>
								</span>
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<li>
									<a data-id="controlPanel" data-code="controlPanel" href="javascript:void(0);" data-url="/personOwn/forwardUsercontrol.do?codeId=setUserInfo" title="<x:message key="index.user.own.info"/>"><x:message key="index.user.own.info"/></a>
								</li>
								<li>
									<a data-id="controlPanel" data-code="controlPanel" href="javascript:void(0);" data-url="/personOwn/forwardUsercontrol.do?codeId=setUserPassword" title="<x:message key="index.user.own.info"/>"><x:message key="index.user.own.password"/></a>
								</li>
								<li class="divider"></li>
								<li>
									<a href="<c:url value="/logout.do"/>"><x:message key="common.logout"/></a>
								</li>
							</ul>
						</div>
						<div class="logo-element"><c:out value="${sessionScope.sessionOperatorAttribute.name}"/></div>
					</li>
					<li>
						<a data-code="homepage" href="javascript:void(0);" data-url="<c:url value="/homePage.do?csrfToken=${token}"/>">
							<i class="fa fa-home"></i>
							<span class="nav-label"><x:message key="index.home.page"/></span> 
						</a>
					</li>
                    <c:import url="/index/taskKind.jsp"/>
				</ul>
				<div style="height:30px;">&nbsp;</div>
			</div>
		</div>
		<!--左侧导航结束-->
		<!--右侧部分开始-->
		<div id="page-wrapper" class="gray-bg dashbard-1">
			<div class="row border-bottom">
				<div class="navbar navbar-static-top" role="navigation" style="margin-bottom:0">
					<div class="navbar-header" style="height:60px">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-primary" href="javascript:void(0);"><i class="fa fa-bars"></i></a>
						<div class="visible-lg visible-md" style="white-space: nowrap;">
							<c:import url="/index/navbarHeader.jsp"/>
						</div>
					</div>
					<ul class="nav navbar-top-links navbar-right" style="display:none;margin-right:0\9;">
						<!-- <li class="dropdown">
							<a class="dropdown-toggle count-info" data-toggle="dropdown" href="javascript:void(0);"> 
								<i class="fa fa-envelope"></i>
								<span class="label label-warning">16</span>
							</a>
							<ul class="dropdown-menu dropdown-messages">
								<li class="m-t-xs">
									<div class="dropdown-messages-box">
										<a href="profile.html" class="pull-left"> 
											<img alt="image" class="img-circle" src="">
										</a>
										<div class="media-body">
											<small class="pull-right">46小时前</small> 
											<strong>小四</strong>新闻测试 <br> 
											<small class="text-muted">3天前 2014.11.8</small>
										</div>
									</div>
								</li>
								<li class="divider"></li>
							</ul>
						</li>
						<li class="dropdown">
							<a class="dropdown-toggle count-info" data-toggle="dropdown" href="javascript:void(0);">
								<i class="fa fa-bell"></i> 
								<span class="label label-primary">8</span>
							</a>
							<ul class="dropdown-menu dropdown-alerts">
								<li>
									<a href="mailbox.html">
										<div>
											<i class="fa fa-envelope fa-fw"></i> 您有16条未读消息 
											<span class="pull-right text-muted small">4分钟前</span>
										</div>
									</a>
								</li>
								<li class="divider"></li>
							</ul>
						</li> -->
						<li class="hidden-sm hidden-md hidden-lg dropdown dropdown-xs">
							<a class="dropdown-toggle" data-toggle="dropdown">
								<i class="fa fa-tasks"></i>
							</a>
							<ul class="dropdown-menu">
								<li><a href="javascript:void(0);" class="s-skin-0"><x:message key="index.skin.default"/></a></li>
								<li><a href="javascript:void(0);" class="s-skin-1"><x:message key="index.skin.blue"/></a></li>
								<li><a href="javascript:void(0);" class="s-skin-3"><x:message key="index.skin.yellow"/></a></li>
							</ul>
						</li>
						<li class="hidden-sm hidden-md hidden-lg">
							<a href="javascript:void(0);" class="count-info" onclick="closeTabItem()">
								<i class="fa fa-window-close"></i><x:message key="index.close"/>
							</a>
						</li>
						<li class="hidden-sm hidden-md hidden-lg dropdown dropdown-xs" id="page-tabs-menu">
							<a href="javascript:void(0);" class="dropdown-toggle count-info" data-toggle="dropdown">
								<i class="fa fa-address-book-o"></i><x:message key="index.pages"/>
							</a>
							<ul class="dropdown-menu"></ul>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="switchOperator()">
								<i class="fa fa-users"></i><span class="hidden-xs"><x:message key="index.switch.pos"/></span>
							</a>
						</li>
						<c:if test="${f:systemParameter('tenant.enable')=='true'}">
						<li class="hidden-xs">
							<a  href="javascript:void(0);" onclick="switchTenant()">
								<i class="fa fa-random"></i><x:message key="index.switch.tenant"/>
							</a>
						</li>
						</c:if>
						<li class="hidden-xs ui-full-screen">
							<a  href="javascript:void(0);" onclick="switchFullScreen()">
								<i class="fa fa-window-maximize"></i><x:message key="index.window.maximize"/>
							</a>
						</li>
						<li class="dropdown hidden-xs">
							<a class="right-sidebar-toggle" aria-expanded="false">
								<i class="fa fa-tasks"></i><x:message key="index.theme"/>
							</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="row content-tabs" id="content-tabs">
				<button class="roll-nav roll-left ui_tabLeft">
					<i class="fa fa-backward"></i>
				</button>
				<div class="page-tabs">
					<div class="page-tabs-content">
						<a href="javascript:void(0);" class="active ui_menuTab" data-id="homepage"><x:message key="index.home.page"/></a>
					</div>
				</div>
				<button class="roll-nav roll-right ui_tabRight">
					<i class="fa fa-forward"></i>
				</button>
				<div class="btn-group roll-nav roll-right">
					<button class="dropdown" data-toggle="dropdown">
						<x:message key="index.tab.operation"/><span class="caret"></span>
					</button>
					<ul role="menu" class="dropdown-menu dropdown-menu-right">
						<li class="ui_tabShowActive"><a><x:message key="index.tab.show.active"/></a></li>
						<li class="divider"></li>
						<li class="ui_tabCloseAll"><a><x:message key="index.tab.close.all"/></a></li>
						<li class="ui_tabCloseOther"><a><x:message key="index.tab.close.other"/></a></li>
					</ul>
				</div>
				<a href="<c:url value="/logout.do"/>" class="roll-nav roll-right ui_tabExit">
					<i class="fa fa fa-sign-out"></i>&nbsp;<x:message key="common.logout"/>
				</a>
			</div>
			<div class="row" id="content-main">
				<iframe id="homepage" name="homepage" style="width:100%;height:100%;border:none 0;" frameborder="0" allowfullscreen="true" allowtransparency="true" data-id="homepage" src="<c:url value="/homePage.do?csrfToken=${token}"/>"></iframe>
			</div>
			<div class="footer">
				<div class="pull-left">
					<c:out value="${f:systemParameter('SYSTEM.COPYRIGHT')}" escapeXml="false"/>
				</div>
				<div class="pull-right">
					<span><i class="fa fa-user"></i>&nbsp;&nbsp;<c:out value="${sessionScope.sessionOperatorAttribute.fullDisplayName}"/></span> 
				</div>
			</div>
		</div>
		<!--右侧部分结束-->
		<!--右侧边栏开始-->
		<div id="right-sidebar">
			<div class="sidebar-container">
				<ul class="nav nav-tabs navs-3">
					<li class="active">
						<a data-toggle="tab" href="#tab-1"><i class="fa fa-gear"></i><x:message key="index.theme"/></a>
					</li>
					<c:if test="${isI18nLanguage}">
					<li><a data-toggle="tab" href="#tab-2"><i class="fa fa-language"></i><x:message key="index.language"/></a></li>
					</c:if>
					<!--<li><a data-toggle="tab" href="#tab-3"> 项目进度 </a></li> -->
				</ul>
				<div class="tab-content">
					<div id="tab-1" class="tab-pane active">
						<div class="sidebar-title">
							<h3>
								<i class="fa fa-comments-o"></i>&nbsp;<x:message key="index.theme.setup"/>
							</h3>
							<small><i class="fa fa-tim"></i><x:message key="index.theme.info"/></small>
						</div>
						<div class="skin-setttings">
							<div class="title"><x:message key="index.theme.setup"/></div>
							<div class="setings-item">
								<label  class="checkbox-inline"><input type="checkbox" name="collapsemenu" id="collapsemenu">&nbsp;<x:message key="index.theme.collapsemenu"/></label>
							</div>
							<div class="setings-item">
								<label  class="checkbox-inline"><input type="checkbox" name="fixednavbar" id="fixednavbar">&nbsp;<x:message key="index.theme.fixednavbar"/></label>
							</div>
							<div class="setings-item">
								<label  class="checkbox-inline"><input type="checkbox" name="boxedlayout" id="boxedlayout">&nbsp;<x:message key="index.theme.boxedlayout"/></label>
							</div>
							<div class="title"><x:message key="index.skin.setup"/></div>
							<div class="setings-item default-skin nb">
                                <span class="skin-name">
                                	<a href="javascript:void(0);" class="s-skin-0"><x:message key="index.skin.default"/></a>
                                </span>
                       		</div>
                        	<div class="setings-item blue-skin nb">
                                <span class="skin-name">
                                	<a href="javascript:void(0);" class="s-skin-1"><x:message key="index.skin.blue"/></a>
                    			</span>
                       		</div>
                       		<div class="setings-item yellow-skin nb">
                                <span class="skin-name">
                                	<a href="javascript:void(0);" class="s-skin-3"><x:message key="index.skin.yellow"/></a>
                    			</span>
                        	</div>
						</div>
					</div>
					<c:if test="${isI18nLanguage}">
					<div id="tab-2" class="tab-pane">
						<div class="sidebar-title">
							<h3>
								<i class="fa fa-comments-o"></i>&nbsp;<x:message key="index.language.setup"/>
							</h3>
						</div>
						<div class="skin-setttings switch-language">
							<c:forEach items="${i18nLanguages}" var="language" varStatus="i">
							<c:set var="languageClassName" value="blue-skin" />
							<c:if test="${i.index%2==1}">
								<c:set var="languageClassName" value="yellow-skin" />
							</c:if>
							<div class="setings-item nb <c:out value="${languageClassName}"/>">
	                            <a href="javascript:void(0);" data-language="<c:out value="${language.value}"/>"><c:out value="${language.name}"/></a>
	                       	</div>
	                       	</c:forEach>
                       	</div>
					</div>
					</c:if>
					<!-- <div id="tab-3" class="tab-pane">
						<div class="sidebar-title">
							<h3>
								<i class="fa fa-cube"></i> 最新任务
							</h3>
							<small><i class="fa fa-tim"></i> 您当前有14个任务，10个已完成</small>
						</div>
						<ul class="sidebar-list">
							
						</ul>
					</div> -->
				</div>
			</div>
		</div>
		<!--右侧边栏结束-->
		<!-- <div id="small-chat">
			<span class="badge badge-warning pull-right">5</span>
			<a class="open-small-chat"> <i class="fa fa-comments"></i></a>
		</div> -->
	</div>
</body>
</html>