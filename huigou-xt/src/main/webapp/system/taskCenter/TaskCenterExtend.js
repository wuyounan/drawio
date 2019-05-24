function getTaskCenterGridColumns (){
	return  [
             { display:"common.field.taskKind",remark: "任务分类", name: "procSysName", width:80, minWidth: 60, type: "string", align: "left",
             	render: function (item) {
             		var procSysName=item.procSysName;
             		if(Public.isBlank(procSysName)){
             			procSysName='协同';
             		}
             		return procSysName;
             	}
             },
             { display:"common.field.procName",remark: "流程名称", name: "procName", width: 120, minWidth: 60, type: "string", align: "left" },
	         { display:"common.field.subprocunitname",remark: "环节名称", name: "name", width: 120, minWidth: 60, type: "string", align: "left" },
	         { display:"common.field.subject",remark: "标题", name: "description", width: 400, minWidth: 60, type: "string", align: "left" },
	         { display:"common.field.applicantPerson",remark: "申请人", name: "applicantPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
	         { display:"common.field.submitPerson",remark: "提交人", name: "creatorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
	         //{ display:"common.field.submitPersonOrg",remark: "提交人机构", name: "creatorOgnName", width: 80, minWidth: 60, type: "string", align: "left" },
	         { display:"common.field.submitPersonDpt",remark: "提交人部门", name: "creatorDeptName", width: 80, minWidth: 60, type: "string", align: "left" },
	         { display:"common.field.kind",remark: "类型", name: "catalogId", width: 80, minWidth: 60, type: "string", align: "left",
	              render: function (item) {
	                  var catalogName = "";
	                  if (item.catalogId == "process") {
	                      catalogName = $.i18nProp('common.field.task.process');//流程任务
	                  } else {
	                      catalogName = $.i18nProp('common.field.task.other');//协同任务
	                  }
	                  return catalogName;
	              }
	          },
	          { display:"common.field.status",remark: "状态", name: "statusName", width: 80, minWidth: 60, type: "string", align: "left" },
	          { display:"common.field.billcode",remark: "编码", name: "bizCode", width: 120, minWidth: 60, type: "string", align: "left" },
	          { display:"common.field.createdate",remark: "提交时间", name: "startTime", width: 140, minWidth: 60, type: "time", align: "left" },
	          { display:"common.field.executorPerson",remark: "执行人", name: "executorPersonMemberName", width: 80, minWidth: 60, type: "string", align: "left" },
	          { display:"common.field.executortime",remark: "执行时间", name: "endTime", width: 140, minWidth: 60, type: "time", align: "left" }
	 ];
}