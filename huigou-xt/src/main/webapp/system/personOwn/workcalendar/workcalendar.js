var colors=['system','yellow','blue','green','red'];//进度条可用颜色
/**jquery fullcalendar 插件参数 **/
var fullcalendarOption={
	theme: true,
	monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],   
    dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],   
    dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],   
	today: ["今天"],
	buttonText: {
		today: '今天',   
        month: '月',   
        week: '周',   
        day: '日'  
    },   
	allDayText:'全天',
	columnFormat: {
		month: 'ddd',
		week: 'ddd M月d日',
		day: 'dddd M月d日'
	},
	timeFormat: {
		'': 'h(:mm)tt' // default
	},
	header: {
		left: 'prev,next today',
		center: 'title',
		right: 'month,agendaWeek,agendaDay'
	},
	editable: true,
	diableResizing:true,
	selectable: true,
	selectHelper: true,
	viewDisplay:function(){closeAddCalendarBubbleTip();},//每次日历加载以及日历的view改变的时候触发一次.
	dayClick: function(date, allDay, jsEvent, view) {//日历点击事件
		var offset=$(this).offset(),x=offset.left+$(this).width()/2,y=allDay?offset.top+$(this).height()/2:jsEvent.pageY;
		var endTime=addDateStep(date,allDay?0:30*60*1000),pos=getbuddlepos(x,y);//不是全天任务默认加一小时
        showAddCalendarBubbleTip(date,endTime,allDay,pos);
    },
	eventDrop: function(event,dayDelta,minuteDelta,allDay,revertFunc) {//拖动后执行
		dropUpdateCalendar(event,revertFunc);
    },
	eventResize:function(event, dayDelta, minuteDelta, revertFunc) {//拖动改变大小后执行
		dropUpdateCalendar(event,revertFunc);
	},
	eventClick: function(event, jsEvent, view) {//日程点击事件
		var offset=$(this).offset(),x=offset.left+$(this).width()/2,y=view.name=='month'?offset.top+$(this).height()/2:jsEvent.pageY;
        var bubble=getBubbleTip(),pos=getbuddlepos(x,y);
        if (pos.hide) {$("#bubble_prong").css("visibility", "hidden").hide();}else {$("#bubble_prong").css("visibility", "visible").show();}
		var date=event.start,endTime=event.end||date,timeView;
		bubble.data('chooseTime',{startTime:date,endTime:endTime});
		var ddds=fullcalendarOption.dayNamesShort[date.getDay()],ddde=fullcalendarOption.dayNamesShort[endTime.getDay()];
		if(event.allDay){
			if(date.getDate()==endTime.getDate()){//判断是否是同一天
				timeView=Public.formatDate(date,'%m月%d日')+'('+ddds+')';
			}else{
				timeView=Public.formatDate(date,'%m月%d日')+'('+ddds+')'+'&nbsp;-&nbsp;';
				timeView+=Public.formatDate(endTime,'%m月%d日')+'('+ddde+')';
			}
		}else{
			if(date.getDate()==endTime.getDate()){//判断是否是同一天
				timeView=Public.formatDate(date,'%m月%d日')+'('+ddds+')&nbsp;';
				timeView+=Public.formatDate(date,'%H:%I')+'&nbsp;-&nbsp;'+Public.formatDate(endTime,'%H:%I');
			}else{
				timeView=Public.formatDate(date,'%m月%d日')+'('+ddds+')&nbsp;'+Public.formatDate(date,'%H:%I')+'&nbsp;-&nbsp;';
				timeView+=Public.formatDate(endTime,'%m月%d日')+'('+ddde+')&nbsp;'+Public.formatDate(endTime,'%H:%I');
			}
		}
		var html=['<table class="bubble-input-table" cellSpacing="0" cellPadding="0"><colgroup><col width="50%"><col width="50%"></colgroup>'];
		html.push('<input type="hidden" id="bubble-id" value="',event.id,'">');
		html.push('<input type="hidden" id="bubble-isalldayevent" value="',event.allDay?1:0,'">');
		html.push('<tr><td colspan=2>');
        html.push('<ul class="showColor">');
		var className=event.className||'system';if(className=='') className='system';
		$.each(colors,function(i,o){html.push('<li class="change '+o+(o==className?' cur':'')+'" id="'+o+'"></li>');});
		html.push('</ul></td></tr>');
		html.push('<tr><td colspan=2><div class="econtent">',event.title,'</div></td></tr>');
		html.push('<tr><td colspan=2 style="border-bottom:1px solid #cccccc;">',timeView,'</td>','</tr>');
		html.push('<tr><td><a href="javascript:void(0);" class="doDel">删除</a>&nbsp;&nbsp;</td>');
		html.push('<td style="text-align:right;">&nbsp;&nbsp;<a href="javascript:void(0);" class="editC">详细信息<strong>»</strong>&nbsp;&nbsp;</a></td></tr>');
		html.push('</table>');
		$('#bubbleContent1').html(html.join(''));
		bubble.css({ "visibility": "visible", left: pos.left, top: pos.top }).show();
	},
	select: function(start, end, allDay,ev) {//选择事件
		showAddCalendarBubbleTip(start,end,allDay,{left:ev.pageX-200, top:ev.pageY-100, hide:true});
	}
};

/**时间计算 返回data 时间 加上 step(ms)后的时间**/
function addDateStep(date,step){
	var times=date.getTime()+step;
	return new Date(times);
}
//页面初始化创建日历
$(document).ready(function(){
    var h=$(this).height(),w=$(this).width();
	$('#calendar').fullCalendar($.extend(fullcalendarOption,{
		height:h-80,
		width:w-200,
		events:function(start, end, callback) {
			Public.ajax(web_app.name+'/personOwn/queryCalendars.ajax', {start:start.getTime(),end:end.getTime()}, function(data) {
				callback(data);
			});
		}
	}));
});
//计算显示对话框显示位置
function getbuddlepos(x, y) {
	 var tleft = x - 120; //先计算如果是显示箭头
	 var ttop = y - 217; //如果要箭头
	 var maxLeft,maxTop,de = document.documentElement;
	 maxLeft = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
	 maxTop = self.innerHeight || (de&&de.clientHeight)|| document.body.clientHeight;
	 var ishide = false;
	 if (tleft <= 0 || ttop <= 0 || tleft + 400 > maxLeft) {
		 tleft = x - 200 <= 0 ? 10 : x - 200;
		 ttop = y - 159 <= 0 ? 10 : y - 159;
		 if (tleft + 400 >= maxLeft) {
			 tleft = maxLeft - 410;
		 }
		 if (ttop + 164 >= maxTop) {
			 ttop = maxTop - 165;
		 }
		 ishide = true;
      }
	 return { left: tleft, top: ttop, hide: ishide };
}
//创建对话框层
function getBubbleTip(){
	var bubble=$('#cw_buddle-tip');
	if(bubble.length==0){
		bubble=$('<div id="cw_buddle-tip" class="bubble"><table class="bubble-table" cellspacing="0" cellpadding="0"><tbody><tr><td class="bubble-cell-side"><div id="tl1" class="bubble-corner"><div class="bubble-sprite bubble-tl"></div></div></td><td class="bubble-cell-main"><div class="bubble-top"></div></td><td class="bubble-cell-side"><div id="tr1" class="bubble-corner"><div class="bubble-sprite bubble-tr"></div></div></td></tr><tr><td class="bubble-mid" colspan="3"><div id="bubbleContent1" style="overflow: hidden;"></div></td></tr><tr><td><div id="bl1" class="bubble-corner"><div class="bubble-sprite bubble-bl"></div></div></td><td><div class="bubble-bottom"></div></td><td><div id="br1" class="bubble-corner"><div class="bubble-sprite bubble-br"></div></div></td></tr></tbody></table><div class="bubble-closebutton"></div><div id="bubble_prong" class="bubble-prong"><div class="bubble-sprite"></div></div></div>').appendTo(document.body);
		bubble.bind('click',function(e){
			var el=$(e.target || e.srcElement);
			e.preventDefault();
			e.stopPropagation();
			if(el.hasClass('bubble-closebutton')){//关闭提示框
				closeAddCalendarBubbleTip();
				return false;
			}
			if(el.is('li.color')){//颜色选择
				var color=el.attr('id');
				bubble.find('li.cur').removeClass('cur');
				el.addClass('cur');
				$('#bubble-className').val(color=='system'?'':color);
				return false;
			}
			if(el.is('li.change')){//颜色改变
				var color=el.attr('id'),id=$('#bubble-id').val();
				bubble.find('li.cur').removeClass('cur');
				el.addClass('cur');
				Public.ajax(web_app.name+'/personOwn/updateCalendarColor.ajax', {id:id,className:color}, function(data) {
					var event=$('#calendar').fullCalendar( 'clientEvents',id)[0];
					event.className=color=='system'?'':color;
					$('#calendar').fullCalendar('updateEvent',event);
				});
				return false;
			}
			if(el.is('a.doDel')){//删除日程
				var id=$('#bubble-id').val();
				//执行删除方法
				Public.ajax(web_app.name+'/personOwn/deleteCalendar.ajax', {id:id}, function(data) {
					$('#calendar').fullCalendar('removeEvents',id);
					closeAddCalendarBubbleTip();
				});
				return false;
			}
			if(el.is('a.editC')){//打开日程详细对话框
				showParticularCalendar();
				closeAddCalendarBubbleTip();
				return false;
			}
			if(el.hasClass('button')){//快捷保存
				var para={},flag=true,fields=['subject','startTime','endTime','isAlldayevent','className'];
				$.each(fields,function(i,o){
					var value=$('#bubble-'+o).val();
					if(o=='subject'){
						if(value==''){Public.tip('请填写内容!');$('#bubble-'+o).focus();flag=false;return false;}
						value=encodeURI(value);
					}
					para[o]=value;
				});
				if(!flag) return false;
				//执行保存方法
				Public.ajax(web_app.name+'/personOwn/saveCalendar.ajax', para, function(data) {
					closeAddCalendarBubbleTip();
					$('#calendar').fullCalendar('unselect');//清除选中
					//添加新日程到日历
					$('#calendar').fullCalendar('renderEvent',{
						id:data,
						title:decodeURI(para['subject']),
						start:para['startTime'],
						end:para['endTime'],
						allDay:para['isAlldayevent']=='1'?true:false,
						className:para['className']
					},false);
				});
			}
			if(el.is('input')){//打开日程详细对话框
				el.focus();
				return false;
			}
			return false;
		}).bind('mousedown',function(e){
			var el=$(e.target || e.srcElement);
			if(!el.hasClass('bubble-closebutton')){
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
		});
	}
	return bubble;
}
//关闭对话框
function closeAddCalendarBubbleTip(){
	var bubble=$('#cw_buddle-tip');
	if(bubble.length>0&&bubble.is(':visible')){
		bubble.removeData('chooseTime');
		bubble.css("visibility", "hidden").hide();
	}
}
//打开新增日程对话框
function showAddCalendarBubbleTip(startTime,endTime,allDay,pos){
	var bubble=getBubbleTip(),timeView,chooseTime=bubble.data('chooseTime');
	if(chooseTime&&chooseTime.startTime.getTime()==startTime.getTime()&&chooseTime.endTime.getTime()==endTime.getTime()){
		//起始,结束时间相同的不在重复执行
		return false;
	}
	bubble.data('chooseTime',{startTime:startTime,endTime:endTime});
    if (pos.hide) {$("#bubble_prong").css("visibility", "hidden").hide();}else {$("#bubble_prong").css("visibility", "visible").show();}
	var ddds=fullcalendarOption.dayNamesShort[startTime.getDay()],ddde=fullcalendarOption.dayNamesShort[endTime.getDay()];
	if(allDay){
		if(startTime.getDate()==endTime.getDate()){//判断是否是同一天
			timeView=Public.formatDate(startTime,'%m月%d日')+'('+ddds+')';
		}else{
			timeView=Public.formatDate(startTime,'%m月%d日')+'('+ddds+')'+'&nbsp;-&nbsp;';
			timeView+=Public.formatDate(endTime,'%m月%d日')+'('+ddde+')';
		}
	}else{
		if(startTime.getDate()==endTime.getDate()){//判断是否是同一天
			timeView=Public.formatDate(startTime,'%m月%d日')+'('+ddds+')&nbsp;';
			timeView+=Public.formatDate(startTime,'%H:%I')+'&nbsp;-&nbsp;'+Public.formatDate(endTime,'%H:%I');
		}else{
			timeView=Public.formatDate(startTime,'%m月%d日')+'('+ddds+')&nbsp;'+Public.formatDate(startTime,'%H:%I')+'&nbsp;-&nbsp;';
			timeView+=Public.formatDate(endTime,'%m月%d日')+'('+ddde+')&nbsp;'+Public.formatDate(endTime,'%H:%I');
		}
	}
	var html=['<table class="bubble-input-table" cellSpacing="0" cellPadding="0"><colgroup><col width="20%"><col width="80%"></colgroup>'];
	html.push('<input type="hidden" id="bubble-startTime" value="',Public.formatDate(startTime,'%Y-%M-%D %H:%I:%S'),'">');
	html.push('<input type="hidden" id="bubble-endTime" value="',Public.formatDate(endTime,'%Y-%M-%D %H:%I:%S'),'">');
	html.push('<input type="hidden" id="bubble-isAlldayevent" value="',allDay?1:0,'">');
	html.push('<tr><td style="text-align:center;">时&nbsp;&nbsp;间:</td>','<td>',timeView,'</td>','</tr>');
	html.push('<tr><td style="text-align:center;">内&nbsp;&nbsp;容:</td><td><input type="text" id="bubble-subject" class="text" maxlength="100"></td></tr>');
	html.push('<tr><td style="text-align:center;">颜&nbsp;&nbsp;色:</td><td>');
	html.push('<input type="hidden" id="bubble-className" value="blue">');
    html.push('<ul class="showColor">');
	$.each(colors,function(i,o){html.push('<li class="color '+o+(i==0?' cur':'')+'" id="'+o+'"></li>');});
	html.push('</ul></td></tr>');
	html.push('<tr><td colspan=2><input type="button" class="button" value="创建日程">&nbsp;&nbsp;<a href="javascript:void(0);" class="editC">详细信息<strong>»</strong></a></td></tr>');
	html.push('</table>');
	$('#bubbleContent1').html(html.join(''));
	bubble.css({ "visibility": "visible", left: pos.left, top: pos.top }).show();
}
//拖动完成后更新日程
function dropUpdateCalendar(event,revertFunc){
	var para={
		id:event.id,
		startTime:Public.formatDate(event.start,'%Y-%M-%D %H:%I:%S'),
		endTime:Public.formatDate(event.end||event.start,'%Y-%M-%D %H:%I:%S'),
		isAlldayevent:event.allDay?1:0
	};
	Public.ajax(web_app.name+'/personOwn/updateCalendar.ajax',para, function(data) {},revertFunc);
}
/*******************以下代码用于日程详细编辑页面*********************/
//打开日历详细编辑页面
function showParticularCalendar(){
	var id=$('#bubble-id').val(),allDay=$('#bubble-isalldayevent').val();
	var url=web_app.name+'/personOwn/getCalendarMap.load';
	var chooseTime=$('#cw_buddle-tip').data('chooseTime');
	UICtrl.showAjaxDialog({url:url,title:'日程维护',param:{id:id},top:50,width:560,
		ok:doSaveCalendar,
		close:function(){
			hideYearDayChoose();
		},init:function(){
			particularCalendar_init(chooseTime,allDay);
		}
	});
}
//日程详明编辑页面事件初始化
function particularCalendar_init(chooseTime,allDay){
	if(chooseTime){//初始化日期显示
		$('#c_startDate').val(Public.formatDate(chooseTime['startTime'],'%Y-%M-%D'));
		$('#c_endDate').val(Public.formatDate(chooseTime['endTime'],'%Y-%M-%D'));
		$('#c_startTime').val(Public.formatDate(chooseTime['startTime'],'%H:%I'));
		$('#c_endTime').val(Public.formatDate(chooseTime['endTime'],'%H:%I'));
		if($('#temp_startDate').val()=='')
			$('#temp_startDate').val(Public.formatDate(chooseTime['startTime'],'%Y-%M-%D'));
	}
	//颜色选择初始化
	var html=[],className=$('#c_className').val();className=className==''?'system':className;
	$.each(colors,function(i,o){html.push('<li class="'+o+(className==o?' cur':'')+'" id="'+o+'"></li>');});
	$('#showColorUl').html(html.join('')).click('click',function(e){
		var el=$(e.target || e.srcElement);
		if(el.is('li')){
			var color=el.attr('id');
			$(this).find('li.cur').removeClass('cur');
			el.addClass('cur');
			$('#c_className').val(color=='system'?'':color);
			return false;
		}
	});
	//是否是全天日程复选框初始化
	$('#c_isalldayevent').bind('click',function(){
		if($(this).is(':checked')){
			$('#showStartTime').hide();
			$('#showEndTime').hide();
		}else{
			$('#showStartTime').show();
			$('#showEndTime').show();
		}
	});
	if(allDay==1){$('#c_isalldayevent').attr('checked',true).triggerHandler('click');}
	//重复日程复选框
	$('#c_isrepeat').bind('click',function(){
		var tr=$('#showRepeatTr');
		if($(this).is(':checked')){
			tr.show();
		}else{
			tr.hide();
		}
	}).triggerHandler('click');
	//时间选择器
	var d,hs=[];
	for(var i=0;i<24;i++){
		d=i<10?'0'+i:i;
		hs.push({value:d+':00',text:d+':00'});
		hs.push({value:d+':30',text:d+':30'});
	}
	$.each(['c_startTime','c_endTime'],function(i,o){
		var objTime=$('#'+o);
		objTime.combox({height:120,width:60,data:hs,back:{text:objTime,value:objTime}});
	});
	//初始化重复日程中事件
	repeatInfoinit();
}
//重复日程中事件初始化
function repeatInfoinit(){
	var frequencys=[],frequency=$('#c_frequency');
	for(var i = 1; i <= 30; i++){
		frequencys.push({value:i,text:i});
	}
	if(frequency.val()=='') frequency.val(1);
	frequency.combox({height:120,data:frequencys,back:{text:frequency,value:frequency}});
	//获取重复类型及重复数据
	var repeattype=$('#c_repeattype').val(),schedules=$('#c_schedules').val();
	//按周重复数据初始化
	var weeks=[],checked='';
	$.each(["日", "一", "二", "三", "四", "五", "六"],function(i,o){
		checked='';
		if(repeattype=='2'&&schedules.indexOf(i+',')!=-1) checked='checked';
		weeks.push('<input value="'+i+'" type="checkbox" '+checked+'>&nbsp;<label>',o,'</label>');
	});
	$('#showWeek_repeat').html(weeks.join(''));
	//按月循环初始化
	var monthDay=[],month_repeat=$('#showMonth_repeat');
	for(var i=1;i<=31;i++){
		monthDay.push({value:i<10?'0'+i:i,text:(i<10?'0'+i:i)+'日'});
	}
	$('#addDay_month').combox({height:120,width:50,data:monthDay,getOffset:function(){
			var of=$('#addDay_month').offset(),_height=$('#addDay_month').outerHeight();
			return {top:of.top+_height+1,left:of.left};
		},
		onClick:function(obj){
			if(obj.is('li')){
				var id=obj.attr('key');
				if(month_repeat.find('span[id="'+id+'"]').length>0){$.closePicker(); return;}
				month_repeat.append('<span class="days" id="'+id+'">'+obj.text()+'</span>');
				$.closePicker();
			}
		}
	});
    if(repeattype=='3'&&schedules!=''){
		$.each(schedules.split(','),function(i,o){
			if(o!=''){
				month_repeat.append('<span class="days" id="'+o+'">'+o+'日'+'</span>');
			}
		});
	}
	//按年循环初始化
	$('#addDay_year').bind('click',function(e){
		showYearDayChoose();
		e.preventDefault();
		e.stopPropagation();
	});
	if(repeattype=='4'&&schedules!=''){
		$.each(schedules.split(','),function(i,o){
			if(o!=''&&o.length==4){
				var m=o.substring(0,2),d=o.substring(3,4);
				$('#showYear_repeat').append('<span class="days" id="'+o+'">'+m+'月'+d+'日'+'</span>');
			}
		});
	}
	//选中按月,按年循环时日期清除事件
	$('#showRepeatTr').bind('dblclick',function(e){
		var el=$(e.target || e.srcElement);
		if(el.is('span.days')){
			el.remove();
		}
	});
	//结束日期选择
	$('#repeatendDate').bind('click',function(){
		$('#repeatendtype3').attr('checked',true);
	});
	var changeRepeattype=function(type){
		 $('#showRepeatTr').find('div.hide').hide();
		 intrepeattype(type);
		 $('#repeatPoint_'+type).show();
	};
	 //重复类型 改变
    $('#c_repeattype').combox({onChange:function(values){ 
    	changeRepeattype(values.value);
    }}); 
    changeRepeattype($('#c_repeattype').val());
	$('#endCount').bind('blur',function(){
		$('#repeatendtype2').attr('checked',true);
	});
}
//初始循环重复日期数据
function intrepeattype(type){
	var str=$('#c_startDate').val(),date=Public.parseDate(str,'%Y-%M-%D');
	var d=date.getDate(),m=date.getMonth()+1,day=date.getDay(),div;
	switch (type) {
	   case '2' :
		   div=$('#showWeek_repeat');
	       if(div.find('input:checked').length==0){
			   div.find('input').get(day).checked=true;
		   }
		   break;
	   case '3' :
		   div=$('#showMonth_repeat');
		   if(div.find('span').length==0){
			   d=d<10?'0'+d:d;
			   div.append('<span class="days" id="'+d+'">'+d+'日'+'</span>');
		   }
		   break;
	   case '4' :
		   div=$('#showYear_repeat');
		   if(div.find('span').length==0){
			   d=d<10?'0'+d:d;
			   m=m<10?'0'+m:m;
			   div.append('<span class="days" id="'+(m+d)+'">'+m+'月'+d+'日'+'</span>');
		   }
		   break;
	   default :
		   break;
	} 

}
//按年重复日期选择框
function showYearDayChoose(){
	var str=$('#c_startDate').val(),date=Public.parseDate(str,'%Y-%M-%D');
	var month=date.getMonth(),monthDays=[31,29,31,30,31,30,31,31,30,31,30,31];
	var choose=$('#showYear_choose'),add=$('#addDay_year'),of=add.offset();
	if(choose.length==0){
		choose=$("<div id='showYear_choose'><div><a class='left' href='#'></a><span id='showyear_choose_month'></span><a class='right' href='#'></a></div><ul id='showYear_choose_content'></ul></div>").appendTo(document.body);;
	    choose.bind('click',function(e){
			var el=$(e.target || e.srcElement);
			e.preventDefault();
			e.stopPropagation();
			if(el.is('a.left')){//减少一个月
				var m=$('#showyear_choose_month').attr('month');
				m=parseInt(m,10);
				m=isNaN(m)?month-1:m-1;
				if(m==-1)m=11;
				showDates(m);
			}
			if(el.is('a.right')){//增加一个月
				var m=$('#showyear_choose_month').attr('month');
				m=parseInt(m,10);
				m=isNaN(m)?month+1:m+1;
				if(m==12)m=0;
				showDates(m);
			}
			if(el.is('li')){//日期选择
				var m=$('#showyear_choose_month').attr('month'),d=el.attr('id');
				m=parseInt(m,10);
				m=isNaN(m)?month+1:m+1;
				m=m<10?'0'+m:m;
				d=d.length==1?'0'+d:d;
				if($('#showYear_repeat').find('span[id="'+(m+d)+'"]').length>0){hideYearDayChoose(); return;}
				$('#showYear_repeat').append('<span class="days" id="'+(m+d)+'">'+m+'月'+d+'日'+'</span>');
				hideYearDayChoose();
			}
		});
	}
	function showDates(m){
		var htmls=[];
		$('#showyear_choose_month').text(fullcalendarOption.monthNames[m]).attr('month',m);
		for(var i=1;i<=monthDays[m];i++){
			htmls.push('<li id="'+i+'">',i,'</li>');
		}
		$('#showYear_choose_content').html(htmls.join(''));
	}
	showDates(month);
	choose.css({top:of.top+add.height()+3,left:of.left}).show();
	$(document).bind('click.showYearDayChoose',hideYearDayChoose);
}
function hideYearDayChoose(){
	$('#showYear_choose').hide();
	$(document).unbind('click.showYearDayChoose');
}
//日期大小比较
function compareForDate(start,end){
	start=start.replace(/-/g,'').replace(/ /g,'').replace(/:/g,'');
	end=end.replace(/-/g,'').replace(/ /g,'').replace(/:/g,'');
	start=parseInt(start,10);
	end=parseInt(end,10);
	if(isNaN(start)||isNaN(end)){
		Public.tip('日期不能正确解析!');
		return false;
	}
	if(start>end){
		Public.tip('开始时间不能大于结束时间!');
		return false;
	}
	return true;
}
//执行日历保存
function doSaveCalendar(){
	var className=$('#c_className').val();
	className=className==''?'system':className;
	var data={
		id:$('#c_id').val(),
		subject:encodeURI($('#c_subject').val()),
		calendarType:$('#c_calendartype').val(),
		description:encodeURI($('#c_description').val()),
		className:className
	};
	if(data['subject']==''){
		Public.tip('请输入日程内容!');
		$('#c_subject').focus();
		return false;
	}
	var isalldayevent=$('#c_isalldayevent').is(':checked')?1:0;
	data['isAlldayevent']=isalldayevent;
	if(isalldayevent===1){
		data['startTime']=$('#c_startDate').val();
		data['endTime']=$('#c_endDate').val();
	}else{
		data['startTime']=$('#c_startDate').val()+' '+$('#c_startTime').val();
		data['endTime']=$('#c_endDate').val()+' '+$('#c_endTime').val();
	}
	if(!compareForDate(data['startTime'],data['endTime'])) return false;
	var isrepeat=$('#c_isrepeat').is(':checked')?1:0;
	data['isRepeat']=isrepeat;
	if(isrepeat===1){//重复
		data['repeatType']=$('#c_repeattype').val();
		data['frequency']=$('#c_frequency').val();
		data['repeatStartDate']=$('#temp_startDate').val();
		if(data['repeatStartDate']==''){
			Public.tip('重复开始日期不能为空!');
			setTimeout(function(){$('#temp_startDate').triggerHandler('click');},0);
			return false;
		}
		if($('#repeatendtype1').is(':checked')){
			data['repeatEndType']=1;
		}
		if($('#repeatendtype2').is(':checked')){
			if($('#endCount').val()==''){
				Public.tip('请输入重复次数!');
				$('#endCount').focus();
				return false;
			}
			data['repeatEndType']=2;
			data['amount']=$('#endCount').val();
		}
		if($('#repeatendtype3').is(':checked')){
			if($('#repeatendDate').val()==''){
				Public.tip('请输入结束日期!');
				setTimeout(function(){$('#repeatendDate').triggerHandler('click');},0);
				return false;
			}
			data['repeatEndType']=3;
			data['repeatEndDate']=$('#repeatendDate').val();
		}
		
		var schedules=[];
		if(data['repeatType']=='2'){//按周重复
			$('#showWeek_repeat').find('input:checked').each(function(){
				schedules.push($(this).val());
			});
		}
		if(data['repeatType']=='3'){
			$('#showMonth_repeat').find('span.days').each(function(){
				schedules.push($(this).attr('id'));
			});
		}
		if(data['repeatType']=='4'){
			$('#showYear_repeat').find('span.days').each(function(){
				schedules.push($(this).attr('id'));
			});
		}
		if(data['repeatType']!='1'&&!schedules.length){
			Public.tip('请选择重复日期!');
			return false;
		}
		data['schedules']=schedules.join(',');
	}
	var _self=this;
	Public.ajax(web_app.name+'/personOwn/saveParticularCalendar.ajax', data, function(msg) {
		$('#calendar').fullCalendar('refetchEvents');
		_self.close();
	});
}