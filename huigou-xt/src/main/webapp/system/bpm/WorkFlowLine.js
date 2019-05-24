$(document).ready(function() {
	initUI();
	initLineIcon();
});
function initUI(){
	//结果处理
	$('span.handle-result').each(function(){
		var result=$(this).data('result');
		var resultName=HandleResult.getDisplayName(result);
		if (!Public.isBlank(resultName)) {
			var className="";
			switch (result) {
		    	 case HandleResult.AGREE:
		    		 className = "label-success";
		            break;
		    	 case HandleResult.DISAGREE:
		    		 className = "label-danger";
		            break;
		    	 case HandleResult.HAVEPASSED:
		    		 className = "label-warning";
		            break;
		    	 case HandleResult.REPLENISH:
		    		 className = "label-default";
		             break;
		          default:
		        	  className = "";            
		    }
			$(this).html(resultName).addClass(className).removeClass('ui-hide');
		}
	});
	//任务状态处理
	$('div.blocks').each(function(){
		var statusId=$(this).data('statusId'),className='';
		var handleStatus=$(this).find('span.handle-status');
		switch (statusId) {
			case TaskStatus.READY:
			case TaskStatus.EXECUTING:
				className = "info";
		        break;
			case TaskStatus.COMPLETED:
		    	className = "success";
		    	break;
			case TaskStatus.SLEEPING:
			case TaskStatus.RETURNED:
			case TaskStatus.WAITED:
			case TaskStatus.SUSPENDED:
				className = "warning";
		        break;
		    case TaskStatus.CANCELED:
		    case TaskStatus.ABORTED:
		    case TaskStatus.DELETED:
		    	className = "danger";
		        break;
		    default:
		    	className = "default";
	    }
		$(this).addClass('alert-'+className);
		handleStatus.addClass('label-'+className);
	});
}

function initLineIcon(){
	$('div.time-line-div').each(function(){
		var procUnitId=$(this).data('procUnitId');
		switch (procUnitId) {
			case 'Apply':
				$(this).find('.proc-unit-name').addClass('label-success');
				$(this).find('.title').html('<i class="fa fa-star btn btn-success"></i>');
		        break;
			case 'Approve':
				$(this).find('.proc-unit-name').addClass('label-warning');
				$(this).find('.title').html('<i class="fa fa-star btn btn-warning"></i>');
		        break;
		    default:
		    	$(this).find('.proc-unit-name').addClass('label-info');
		    	$(this).find('.title').html('<i class="fa fa-star btn btn-info"></i>');
	    }
	});
}