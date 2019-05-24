<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<x:base include="dialog,dateTime,tree,combox,echart" />
<x:script src='/system/taskCenter/TasksExecute.js'/>
<x:script src='/system/bpm/BpmUtil.js'/>
<x:script src='/javaScript/remind.js'/>
<x:script src='/javaScript/addFunction.js'/>
<x:script src='/javaScript/HomePage.js'/>
<x:script src='/javaScript/HomePageEcharts.js'/>
</head>
<body>
	<div class="container-gray-bg" >
		 <div class="row-fluid m-t-sm">
         	<div class="col-xs-12">
	            <div class="alert alert-block alert-info m-b-xs">
	            	<div class="main-function-box">
						<div class="main-function-left" id="mainFunctionBox"></div>
						<div class="main-function-right">
							<button type="button" class="btn btn-info btn-lg" id="mainAddFunction"><i class="fa fa-plus"></i></button>
						</div>		
					</div>		
				</div>
	        </div>
         </div>
		<div class="row-fluid m-t-sm">
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box red-bg">
					<span class="info-box-icon"><i class="fa fa-bell-o"></i></span>
					<div class="info-box-content">
						<span class="info-box-text">待办事项</span>
						<span class="info-box-number taskCount"><c:out value="${taskCount}" /></span>
						<div class="progress">
							<div class="progress-bar" style="width: 70%"></div>
						</div>
						<span class="progress-description"><span class="taskCount"><c:out value="${taskCount}" /></span>项工作等待处理</span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box navy-bg">
					<span class="info-box-icon"><i class="fa fa-thumbs-o-up"></i></span>
					<div class="info-box-content">
						 <span class="info-box-text">跟踪事项</span>
						 <span class="info-box-number trackingTaskCount"><c:out value="${trackingTaskCount}" /></span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box yellow-bg">
					<span class="info-box-icon"><i class="fa fa-calendar"></i></span>
					<div class="info-box-content">
						<span class="info-box-text">我的消息</span>
						<span class="info-box-number">16</span>
						<div class="progress">
							<div class="progress-bar" style="width: 70%"></div>
						</div>
						<span class="progress-description">
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box blue-bg">
					<span class="info-box-icon"><i class="fa fa-suitcase"></i></span>

					<div class="info-box-content">
						<span class="info-box-text">知识库</span>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row-fluid m-t-sm">
	        <div class="col-sm-6 col-xs-12">
	            <div class="box box-success">
		            <div class="box-header with-border">
		              <h3 class="box-title">天气预报</h3>
		              <div class="box-tools pull-right">
		                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		              <div class="echarts-demo" id="echarts-line-chart"></div>
		            </div>
	          	</div>
	        </div>
	        <div class="col-sm-6 col-xs-12">
	            <div class="box box-warning">
		            <div class="box-header with-border">
		              <h3 class="box-title">天气数据分析</h3>
		              <div class="box-tools pull-right">
		                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		              <div class="echarts-demo" id="echarts-bar-chart"></div>
		            </div>
	            </div>
            </div>
         </div>
         
         <div class="row-fluid m-t-sm col-padding-sm p-l-xs p-r-xs">
         	 <div class="col-sm-5 col-xs-12">
	            <div class="box box-danger">
		            <div class="box-header with-border">
		              <h3 class="box-title">待办任务</h3>
		              <div class="box-tools pull-right">
		                <button type="button" class="btn btn-box-tool" title="刷新" onclick="reloadGrid()"><i class="fa fa-repeat"></i></button>
		                <button type="button" class="btn btn-box-tool" title="更多" onclick="waitTaskMore()"><i class="fa fa-bars"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		            	<c:import url="/system/taskCenter/TasksWait.jsp"/>
		            </div>
	          	</div>
	        </div>
	        <div class="col-sm-5 col-xs-12">
	            <div class="box box-success">
		            <div class="box-header with-border">
		              <h3 class="box-title">跟踪任务</h3>
		              <div class="box-tools pull-right">
		                <button type="button" class="btn btn-box-tool" title="刷新" onclick="reloadGrid()"><i class="fa fa-repeat"></i></button>
		                <button type="button" class="btn btn-box-tool" title="更多" onclick="trackTaskMore()"><i class="fa fa-bars"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		            	<c:import url="/system/taskCenter/TasksTracking.jsp"/>
		            </div>
	          	</div>
	        </div>
	        <div class="col-sm-2 col-xs-12">
	            <div class="box box-info">
		            <div class="box-header with-border">
		              <h3 class="box-title">消息提醒</h3>
		              <div class="box-tools pull-right">
		                <button type="button" class="btn btn-box-tool" title="刷新" id="messageRemindRefresh"><i class="fa fa-repeat"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		              <div class="data-list-warp dom-overflow-auto" id="messageRemindContent"></div>
		            </div>
	          	</div>
	        </div>
         </div>
	</div>
</body>
</html>