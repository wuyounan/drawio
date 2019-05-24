$(document).ready(function() {
	UICtrl.setDisable($('body'));
	changeInputView();
	setTimeout(function(){
		$('#screenOverLoading').hide();
	},0);
});
function changeInputView(){
	$('body').find('input[type="text"]').each(function(){
		if($(this).is(':hidden')) return;
		var value=$(this).val();
		$(this).hide();
		$('<span class="ui-text-label"></span>').text(value).insertAfter($(this));
	});
	$('body').find('textarea').each(function(){
		if($(this).is(':hidden')) return;
		var value=$(this).val();
		$(this).textareaAutoHeight();
		value=value.replace(/\n/g, "<br />");
		$(this).hide();
		$('<span class="ui-textarea-label"></span>').html(value).insertAfter($(this));
	});
	$('body').find('select').each(function(){
		if($(this).is(':hidden')) return;
		var value=$(this).find("option:selected").text();
		UICtrl.disable($(this));
		$(this).hide();
		$('<span class="ui-text-label"></span>').text(value).insertAfter($(this));
	});
	//替换单选
	$('body').find('input:radio').each(function(){
		$('<span class="radio'+($(this).is(':checked')?'checked':'')+'"></span>').insertAfter($(this));
		$(this).hide();
	});
	//替换复选
	$('body').find('input:checkbox').each(function(){
		$('<span class="checkbox'+($(this).is(':checked')?'checked':'')+'"></span>').insertAfter($(this));
		$(this).hide();
	});
	$('body').find('div.ui-attachment-tools').hide();
	$('body').find('div.ui-float-top-button').hide();
	$('div.job-task-execution').find('div').each(function(){
		if($(this).hasClass('col-xs-4')&&$(this).hasClass('col-sm-2')){
			$(this).removeClass('col-xs-4');
			$(this).addClass('col-xs-2');
		}
		if($(this).hasClass('col-xs-8')&&$(this).hasClass('col-sm-2')){
			$(this).removeClass('col-xs-8');
			$(this).addClass('col-xs-2');
		}
	});
	
	setTimeout(function(){
		$('span.ui-text-label').show();
		window.print();
	},10);
}
