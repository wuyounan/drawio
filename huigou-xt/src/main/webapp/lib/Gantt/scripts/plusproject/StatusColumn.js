//状态列
PlusProject.StatusColumn = function (optons) {
    return mini.copyTo({
        name: "Status",
        width: 60,
        header: '<div class="mini-gantt-taskstatus"></div>',
        formatDate: function (date) {
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        },
        renderer: function (e) {
            var record = e.record;
            var s = "";
          	if (record.Status == 2) {
          		s += '<div class="mini-gantt-aborted" title="任务已中止"></div>';
          	}else if (record.Status == 3) {
          		s += '<div class="mini-gantt-unfinish" title="任务置为未完"></div>';
          	}else if (record.Status == 1) {
	            if (record.PercentComplete == 100) {
	                var t = record.ActualFinish ? "任务完成于 " + this.formatDate(record.ActualFinish) : "";
	                if(record.FinishDate<record.ActualFinish){
	                	t+=",延迟完成!";
	                	s += '<div class="mini-gantt-finished" title="' + t + '"></div>';
	                }else{
	              	   s += '<div class="mini-gantt-finished-tick" title="' + t + '"></div>';
	                }
	            }
            }else if (record.Status == 0) {
            	if(record.FinishDate<new Date()){//延时且未完成
            		s += '<div class="mini-gantt-bullet-red" title="延时且未完成"></div>';
            	}else{
            		  if(record.StartDate<new Date()){
            		  		s += '<div class="mini-gantt-bullet-green" title="进行中"></div>';
          			  }
            		  if (record.Summary && record.FixedDate) {//SUMMARY摘要任务FIXED_DATE限制日期（摘要任务专用）
                          var t = "此任务固定日期，从开始日期 " + this.formatDate(record.Start)
                                  + " 到完成日期 " + this.formatDate(record.Finish);
                          s += '<div class="mini-gantt-constraint3" title=\'' + t + '\'></div>';
                      } else if (record.ConstraintType >= 2 && mini.isDate(record.ConstraintDate)) {//CONSTRAINT_TYPE任务限制类型//CONSTRAINT_DATE任务限制日期
                          var ct = mini.Gantt.ConstraintType[record.ConstraintType];
                          if(ct){
                              var t = "此任务有 "+ct.Name+" 的限制，限制日期 " + this.formatDate(record.ConstraintDate);
                              s += '<div class="mini-gantt-constraint' + record.ConstraintType + '" title=\'' + t + '\'></div>';
                          }
                      }
                      if (record.Milestone) {
                          s += '<div class="mini-gantt-milestone" title="里程碑"></div>';
                      }
                      if (record.Remark) {
                          var t = '备注：' + record.Remark;
                          s += '<div class="mini-gantt-notes" title="' + t + '"></div>';
                      }
            	}
            }else{
        		s += '<div class="mini-gantt-conflict" title="异常状态计划"></div>';            	
            }
           

            //如果有新的任务状态图标显示, 请参考以上代码实现之......mini-gantt-conflict

            return s;
        }
    }, optons);
}