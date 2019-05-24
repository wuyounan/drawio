<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<li>
	<a href="javascript:void(0);">
		<i class="fa fa-tasks"></i>
		<span lass="nav-label"><x:message key="index.task.center" /></span>
		<span class="fa arrow"></span>
	</a>
	<ul class="nav nav-second-level collapse">
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(1)">
				<i class="fa fa-tasks"></i><x:message key="index.task.center.todo" />
			</a>
		</li>
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(4)">
				<i class="fa fa-info-circle"></i><x:message key="index.task.center.draft" />
			</a>
		</li>
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(5)">
				<i class="fa fa-user-secret"></i><x:message key="index.task.center.own" />
			</a>
		</li>
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(3)">
				<i class="fa fa-send-o"></i><x:message key="index.task.center.submit" />
			</a>
		</li>
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(2)">
				<i class="fa fa-list"></i><x:message key="index.task.center.done" />
			</a>
		</li>
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(8)">
				<i class="fa fa-bars"></i><x:message key="index.task.center.procdone" />
			</a>
		</li>
		<li>
			<a data-code="taskCenter" href="javascript:void(0);" onclick="showTaskCenter(6)">
				<i class="fa fa-star-half-full"></i><x:message key="index.task.center.collect" />
			</a>
		</li>
	</ul>
</li>