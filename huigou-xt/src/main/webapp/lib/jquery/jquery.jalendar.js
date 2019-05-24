/********************************
title:      系统日程安排相关
Author:      xiexin
Date :      2016-05-14
*********************************/
$(function () {    
    (function ($) {
    	 $.fn.appendArray = function () {
    		var args = arguments;
    		return this.each(function() {
    			var _self=$(this);
    			$.each(args,function(i,o){
    				if($.isArray(o)){
    					_self.appendArray.apply(_self,o);
    				}else{
    					_self.append(o);
    				}
    			});
    		});
    	 };
    	
        $.fn.jalendar = function (options) {
            var settings = $.extend({
                customDay: new Date(),
                color: '#65c2c0',
                titleColor:'#65c2c0',
                viewPersons:[],
                workDateDuty:[],
                lang: 'EN'
            }, options);
            
            var workDateDuty=settings.workDateDuty||[];
            // Languages            
            var dayNames = {};
            var monthNames = {};
            var lAddEvent = {};
            var lAllDay = {};
            var lTotalEvents = {};
            var lEvent = {};
            dayNames['EN'] = new Array('Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun');
            dayNames['TR'] = new Array('Pzt', 'Sal', 'Çar', 'Per', 'Cum', 'Cmt', 'Pzr');
            dayNames['ES'] = new Array('Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Såb', 'Dom');
			dayNames['CN'] = new Array( "周一", "周二", "周三", "周四", "周五", "周六","周日");
            monthNames['EN'] = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'); 
            monthNames['TR'] = new Array('Ocak', 'Şubat', 'Mart', 'Nisan', 'Mayıs', 'Haziran', 'Temmuz', 'Ağustos', 'Eylül', 'Ekim', 'Kasım', 'Aralık'); 
            monthNames['ES'] = new Array('Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'); 
			monthNames['CN'] = new Array("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"); 
            lAddEvent['EN'] = 'Add New Event';
            lAddEvent['TR'] = 'Yeni Etkinlik Ekle';
            lAddEvent['ES'] = 'Agregar Un Nuevo Evento';
			lAddEvent['CN'] = '';
            lAllDay['EN'] = 'All Day';
            lAllDay['TR'] = 'Tüm Gün';
            lAllDay['ES'] = 'Todo El Día';
			lAllDay['CN'] = '全天';
            lTotalEvents['EN'] = 'Total Events in This Month: ';
            lTotalEvents['TR'] = 'Bu Ayki Etkinlik Sayısı: ';
            lTotalEvents['ES'] = 'Total De Eventos En Este Mes: ';
			lTotalEvents['CN'] = '本月日程总数: ';
            lEvent['EN'] = 'Event(s)';
            lEvent['TR'] = 'Etkinlik';
            lEvent['ES'] = 'Evento(s)';
			lEvent['CN'] = '日程(数)';
            
            
            var $this = $(this);
            var div = function (e, classN) {
                return $(document.createElement(e)).addClass(classN);
            };
            
            var clockHour = [];
            var clockMin = [];
            var tmpi='';
            for (var i=0;i<24;i++ ){
            	tmpi= (i < 10) ? ("0" +i) : i;
                clockHour.push(div('div', 'option day-time-option').text(tmpi))
            }
            for (var i=0;i<59;i+=5 ){
            	tmpi= (i < 10) ? ("0" +i) : i;
                clockMin.push(div('div', 'option day-time-option').text(tmpi))
            }
            var persons = [],firstPersonId,firstPersonName;
            $.each(settings.viewPersons,function(i,o){
            	if(i==0){
            		firstPersonId=o['personId'];
            		firstPersonName=o['personName'];
            	}
            	persons.push(div('div', 'option show-person-option').attr('id',o['personId']).text(o['personName']));
            });
            // HTML Tree
            $this.appendArray(
                //div('div', 'wood-bottom'), 
                div('div', 'jalendar-wood').appendArray(
                    div('div', 'close-button').attr('title','关闭'),
                    div('div', 'jalendar-pages').appendArray(
                        //div('div', 'pages-bottom'),
                        div('div', 'header').css('background-color', settings.titleColor).appendArray(
                            div('a', 'prv-m'),
                            div('h1'),
                            div('a', 'nxt-m'),
                            div('div', 'day-names')
                        ),
						div('div', 'show-person').appendArray(
                            div('div', 'select').addClass('person').css('background-color', settings.color).appendArray(
                                 div('span','show-person-span').attr('id','showPersonSpan').attr('personId',firstPersonId).text(firstPersonName+'的日程'),
                                 div('div', 'dropdown').appendArray(persons)
                            )
                        ),
                        div('div', 'total-bar').html( lTotalEvents[settings.lang] + '<b style="color: '+settings.color+'"></b>'),
                        div('div', 'days')
                    ),
                    div('div', 'add-event').appendArray(
                        div('div', 'add-new').appendArray(
                            '<input type="text" placeholder="' + lAddEvent[settings.lang] + '" value="' + lAddEvent[settings.lang] + '" />',
                            div('div', 'submit').attr('title','保存'),
                            div('div', 'clear'),
                            div('div', 'add-time').appendArray(
                                div('div', 'disabled'),
                                div('div', 'select').addClass('hour').css('background-color', settings.color).appendArray(
                                    div('span','day-time-choose').text('00'),
                                    div('div', 'dropdown').appendArray(clockHour)
                                ),
                                div('div', 'left').appendArray(':'),
                                div('div', 'select').addClass('min').css('background-color', settings.color).appendArray(
                                    div('span','day-time-choose').text('00'),
                                    div('div', 'dropdown').appendArray(clockMin)
                                )
                            ),
                            div('div', 'all-day').appendArray(
                                div('fieldset').attr('data-type','disabled').appendArray(
                                    div('div', 'check').appendArray(
                                        div('span', '')
                                    ),
                                    div('label').text(lAllDay[settings.lang])
                                )
                            ),
                            div('div', 'clear')
                        ),
                        div('div', 'events').appendArray(
                            div('h3','').appendArray(
                            	div('span', '').html('<b class="date-view"></b> '),
                                div('span', '').html('<b class="num-view"></b> ' + lEvent[settings.lang])
                            ),
                            div('div', 'gradient-wood'),
                            div('div', 'events-list')
                        )
                    )
                )
            );
            
            // Adding day boxes
            for (var i = 0; i < 42; i++) {
                $this.find('.days').appendArray(div('div', 'day'));
            }
            
            // Adding day names fields
            for (var i = 0; i < 7; i++) {
                $this.find('.day-names').appendArray(div('h2').text(dayNames[settings.lang][i]));
            }

            var d = settings.customDay;
            var year = d.getFullYear();
            var date = d.getDate();
            var month = d.getMonth();
           
            
            var isLeapYear = function(year1) {
                var f = new Date();
                f.setYear(year1);
                f.setMonth(1);
                f.setDate(29);
                return f.getDate() == 29;
            };
        
            var feb;
            var febCalc = function(feb) { 
                if (isLeapYear(year) === true) { feb = 29; } else { feb = 28; } 
                return feb;
            };
            
            var monthDays = new Array(31, febCalc(feb), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
            
            var getFirstDateOfMonth=function(){
            	var ms=(month+1);
            	ms=(ms < 10) ? ("0" +ms) : ms;
            	return year+'-'+ms+'-' + '01';
            };
            
            var getLastDateOfMonth=function(){
            	var ms=(month+1);
            	ms=(ms < 10) ? ("0" +ms) : ms;
            	var ds=monthDays[month];
            	return year+'-'+ms+'-' + ds;
            };
            
            var getPersonId=function(){
            	var personId=$('#showPersonSpan').attr('personId');
            	return personId;
            };
            
            //重新加载数据
            function reLoadEventData(){
            	var personId=getPersonId();
                prevAddEvent();
                //重新加载数据
                WorkCalendar.queryNewCalendarByPerson($this,personId,getFirstDateOfMonth(),getLastDateOfMonth(),function(data){
                	 workDateDuty=data['companyWorkCalendar'];
                	 calcMonth();
                });
            };
            

            function calcMonth() {

                monthDays[1] = febCalc(feb);
                
                var weekStart = new Date();
                weekStart.setFullYear(year, month, 0);
                var startDay = weekStart.getDay();  
                
                $this.find('.header h1').html(monthNames[settings.lang][month] + ' ' + year);
        
                $this.find('.day').html('&nbsp;');
                $this.find('.day').removeClass('this-month');
                var ms,ds;
                for (var i = 1; i <= monthDays[month]; i++) {
                	ms=(month+1);
                	ms=(ms < 10) ? ("0" +ms) : ms;
                	ds= (i < 10) ? ("0" +i) : i;
                    startDay++;
                    $this.find('.day').eq(startDay-1).addClass('this-month').attr('data-date', year+'-'+ms+'-' + ds).html(i);
                }
                if ( month == d.getMonth() ) {
                    $this.find('.day.this-month').removeClass('today').eq(date-1).addClass('today');
                } else {
                	$this.find('.today').removeClass('today');
                    $this.find('.day.this-month').attr('style', '');
                }
                
                // added event
                $this.find('div.added-event').each(function(i){
					var id= $(this).attr('data-id'),isAllday=$(this).attr('is-allday');
					var thisDay=$(this).attr('data-date'),time=$(this).attr('data-time');
					var dataTitle=$(this).attr('data-title'),linkBillUrl=$(this).attr('link-bill-url');
                    var $thisDiv=$this.find('.this-month[data-date="' + thisDay + '"]').appendArray(
                        div('div','event-single').attr({'data-date':thisDay, 'data-time': time, 'data-id': id,'is-allday':isAllday}).appendArray(
                            div('p','').text(dataTitle),
                            div('div','details').appendArray(
                                div('div', 'clock').text(isAllday==1?lAllDay[settings.lang]:time),
                                div('div', 'erase').attr('title','删除日程'),
                                div('div', 'event-edit').attr('title','修改日程')
                            )
                        )
                    );
                    //添加业务单据链接
                    if(!Public.isBlank(linkBillUrl)){
                    	if(parent&&parent['addTabItem']){
                    		$thisDiv.find('div.details').appendArray(div('div', 'detail-link').attr('linkBillUrl',linkBillUrl).text('[查看详细]').attr('title','查看日程详细信息'));
                    	}
                    }
                });
				//存在日程打标记
                $this.find('.day').has('.event-single').addClass('have-event').prepend(div('i',''));
                calcTotalDayAgain();  
                _setDayStyle();
            }
            
            function _setDayStyle(){
            	 //样式
                $this.find('.day:nth-child(7n+1)').css({borderLeft:'1px solid #e0e0e0'});
                $this.find('.day:gt(34)').css({borderBottom:'1px solid #e0e0e0'});
                $this.find('div.day').find('span.holiday,span.statutory-holiday').remove();
                if(!$.isArray(workDateDuty)){
                	return;
                }
                $.each(workDateDuty,function(i,o){
                	var workDate=o['workDate'],workType=o['workType'];
                	var className='';
                	if(workType==1){//休
                		className='holiday';
                	}else if(workType==2){//法定假
                		className='statutory-holiday';
                	}
                	$this.find('.this-month[data-date="' + workDate + '"]').prepend(div('span',className));
                });
            }
            
            //构建日历
            calcMonth();
            
            var $close = $this.find('.jalendar-wood > .close-button');
            var $addNew=$this.find('.add-new');
            var jalendarWood=$this.children('.jalendar-wood');
            var jalendarWoodH =100;
            setTimeout(function(){
            	 jalendarWoodH = $this.find('.jalendar-wood').height();
            	 //showToday();
            },0);
           // var woodBottomH = $this.find('.wood-bottom').height();
           
            //默认显示今天
            function  showToday() {
            	var today=settings.customDay;
            	var _self=$this.find('.this-month[data-date="' + today + '"]');
            	if(!_self.hasClass('have-event')){//没事件不自动弹出
            		return;
            	}
                var eventSingle = _self.find('.event-single')
                $this.find('.events .event-single').remove();
                _self.addClass('selected').css({'background-color': settings.color});
                $addNew.hide();
				var top=$this.find('div.jalendar-pages').height();
				 $this.find('.add-event').attr('data-date',today);
				$this.find('.add-event').css({left:0-(jalendarWood.width()+10)}).show().find('.events-list').html(eventSingle.clone());
				$this.find('.add-new input').select();
				calcTotalDayAgain();
				calcScroll();
				$close.css({top:-70,left:-15}).show();
               
            };
            
            
            // calculate for scroll
            function calcScroll() {
                if ( $this.find('.events-list').height() < $this.find('.events').height() ) {
					$this.find('.gradient-wood').hide(); $this.find('.events-list').css('border', 'none') 
				} else {
					$this.find('.gradient-wood').show(); 
				}
            }
            
            // Calculate total event again
            function calcTotalDayAgain() {
                var eventCount = $this.find('.this-month .event-single').length;
                $this.find('.total-bar b').text(eventCount);
                $this.find('.events h3 span b.num-view').text($this.find('.events .event-single').length);
                $this.find('.events h3 span b.date-view').text($this.find('.add-event').attr('data-date'));
            }
            
            function prevAddEvent() {
                $this.find('.day').removeClass('selected').removeAttr('style');
                _setDayStyle();
                $this.find('.add-event').hide();
                //$this.children('.jalendar-wood').animate({'height' : jalendarWoodH}, 200);
                //$this.children('.wood-bottom').animate({'height' : woodBottomH}, 200);
                $close.hide();
                $this.find('div.dropdown').hide(0); 
            }
           
            //事件控制
			$('html').click(function(){
				prevAddEvent();
            });
            
            //统一事件处理，减少注册事件数量
            $this.on('click', function (e) {
            	var $clicked = $(e.target || e.srcElement);
            	if($clicked.hasClass('prv-m')){//月减少
            		minMonth();
            		return;
            	}
            	if($clicked.hasClass('nxt-m')){//月增加
            		addMonth();
            		return;
            	}
            	if($clicked.hasClass('this-month')){//点击日期
            		thisMonthDay($clicked,e);
            		return;
            	}
            	if($clicked.hasClass('day-time-choose')){//点击时间选择
            		dayTimeChoose($clicked,e);
            		return;
            	}
            	if($clicked.hasClass('day-time-option')){//点击时间选择
            		onDayTimeChoose($clicked,e);
            		return;
            	}
            	if($clicked.hasClass('show-person-span')){//打开人员选择
            		personChoose($clicked,e);
            		return;
            	}
            	if($clicked.hasClass('show-person-option')){//人员选择
            		onPersonChoose($clicked,e);
            		return;
            	}
            	if($clicked.hasClass('submit')){//添加新日查
            		addNewWorkData($clicked,e);
            		return;
            	}
            	if($clicked.hasClass('close-button')){//关闭
            		prevAddEvent(); 
            		return;
            	} 
            	if($clicked.hasClass('erase')){//删除日程
            		deleteWorkEvent($clicked,e); 
            		return;
            	} 
            	if($clicked.hasClass('event-edit')){//修改日程
            		editWorkEvent($clicked,e); 
            		return;
            	} 
            	if($clicked.hasClass('detail-link')){//查看日程详细信息
            		openLinkBillUrl($clicked,e); 
            		return;
            	} 
            	
            	var hasAllDay=$clicked.parents('div.all-day');
            	if(hasAllDay.length>0){
            		allDayChoose(hasAllDay,e);
            		return;
            	}
            	//阻止事件起泡
            	var hasAddEvent=$clicked.parents('div.add-event');
            	if($clicked.hasClass('add-event')||hasAddEvent.length>0){
            		add_time_select.children('.dropdown').hide(0);
            		e.stopPropagation(); 
            		return;
            	}
            });
            
           //月增加
           var addMonth=function () {
                if ( month >= 11 ) {
                    month = 0;
                    year++;
                } else {
                    month++;   
                }
                reLoadEventData();
           };
          //月减少
          var minMonth=function () {
                if ( month === 0 ) {
                    month = 11;
                    year--;
                } else {
                    month--;   
                }
                reLoadEventData();
            };
           //点击日期
           var thisMonthDay=function (_self,event) {
                var eventSingle = _self.find('.event-single')
                $this.find('.events .event-single').remove();
                prevAddEvent();
                _self.addClass('selected').css({'background-color': settings.color});
                $addNew.show();
                //jalendarWood.css({height:300});
                var top=$this.find('div.jalendar-pages').height();
                $this.find('.add-event').attr('data-date',_self.attr('data-date'));
				$this.find('.add-event').css({left:0-(jalendarWood.width()+10)}).show().find('.events-list').html(eventSingle.clone());
				$this.find('.add-new input').select();
				calcTotalDayAgain();
				calcScroll();
				$close.css({top:-70,left:-15}).show();
				event.stopPropagation(); 
            };
            
            //日期选择
           var  add_time_select= $this.find('.add-time .select');
           var dayTimeChoose=function(_self,event){
        	   add_time_select.children('.dropdown').hide(0);
                _self.next('.dropdown').css({overflowX:'hidden',overflowY:'auto'}).show(0);
                event.stopPropagation(); 
           };
           var onDayTimeChoose=function(_self,event){
        	   _self.parent().parent().children('span').text(_self.text());
                add_time_select.children('.dropdown').hide(0);
                event.stopPropagation(); 
            };

			//人员选择
            var person_choose_select= $this.find('.show-person .select');
			var personChoose=function(_self,event){
				person_choose_select.children('.dropdown').hide(0);
                _self.next('.dropdown').show(0);
                event.stopPropagation(); 
            };
            var onPersonChoose=function(_self,event){
            	var parentSpan=_self.parent().parent().children('span');
            	var personId=_self.attr('id'),nowPersonId=parentSpan.attr('personId');
            	if(personId!=nowPersonId){
            		parentSpan.text(_self.text()+'的日程').attr('personId',personId);
            		reLoadEventData();
            	}
            	person_choose_select.children('.dropdown').hide(0);
            };
			
            //全天选择标志
			var allDayChoose=function(_self,event){
				var fieldset=_self.find('fieldset'),dataType=fieldset.attr('data-type');
				doChooseAllDay(fieldset,dataType);
				event.stopPropagation(); 
			}
			var doChooseAllDay=function(fieldset,dataType){
				if(dataType=='disabled'){
					fieldset.removeAttr('data-type').attr('data-type', 'enabled').children('.check').children().css('background-color', settings.color);
					fieldset.parents('.all-day').prev('.add-time').css('opacity', '0.4').children('.disabled').css('z-index', '10');
				    person_choose_select.children('.dropdown').hide(0);
				}else{
					fieldset.removeAttr('data-type').attr('data-type', 'disabled').children('.check').children().css('background-color', 'transparent');
					fieldset.parents('.all-day').prev('.add-time').css('opacity', '1').children('.disabled').css('z-index', '-1');
				}
			};
			//添加新日程
            var addNewWorkData=function(_self,event){
            	var oldDataId=$addNew.attr('event-data-id');
                var title = _self.prev('input').val();
                var hour = $addNew.find('.hour > span').text();
                var min = $addNew.find('.min > span').text();
                var isAllDay = $addNew.find('.all-day fieldset').attr('data-type');
                var isAllDayText = $addNew.find('.all-day fieldset label').text();
                var thisDay = $this.find('.day.this-month.selected').attr('data-date');
                var time,isAllDayFlag=false;
                if ( isAllDay == 'disabled' ) {
                    time = hour + ':' + min;
                    isAllDayFlag=false;
                } else {
                    time = isAllDayText;
                    isAllDayFlag=true;
                }
                var personId=getPersonId();
                if(Public.isBlank(title)){
                	Public.tip('请输入日程数据!');
                	_self.prev('input').focus();
                	event.stopPropagation(); 
                	return false;
                }
                //调用后台保存
                WorkCalendar.doSaveNewCalendar(oldDataId,personId,title,isAllDayFlag,thisDay,time,function(dataId){
                	if(!Public.isBlank(oldDataId)){
                		$this.find('div[data-id=' + oldDataId + ']').remove();
                	}
                	var selectedDay=$this.find('.day.this-month.selected');
                    $this.prepend(div('div', 'added-event').attr({'data-date':thisDay, 'data-time': time, 'data-title': title, 'data-id': dataId,'is-allday':isAllDayFlag?1:0}));
                    selectedDay.prepend(
                        div('div','event-single').attr({'data-date':thisDay, 'data-time': time, 'data-id': dataId,'is-allday':isAllDayFlag?1:0}).appendArray(
                            div('p','').text(title),
                            div('div','details').appendArray(
                                div('div', 'clock').text(time),
                                div('div', 'erase').attr('title','删除日程'),
                                div('div', 'event-edit').attr('title','修改日程')
                            )
                        )
                    );
                    //按日期排序
                    var rows=selectedDay.find('div.event-single').get();
                    rows.sort(function(a,b){
                    	var t1=$(a).attr('data-time'),i1=$(a).attr('is-allday');
                    	var t2=$(b).attr('data-time'),i2=$(b).attr('is-allday');
                    	var startTime1=(i1==1?'00:00':t1);
                    	var startTime2=(i2==1?'00:00':t2);
                    	var flag=Public.dateCompare(startTime2,startTime1);
                    	return flag
                    });
                    $.each(rows,function(i,o){
                    	selectedDay.prepend(o);
                    });
                    $this.find('.day').has('.event-single').addClass('have-event').prepend(div('i',''));
                    $this.find('.events-list').html($this.find('.day.this-month.selected .event-single').clone())
                    $this.find('.events-list .event-single').eq(0).hide().slideDown();
                    calcTotalDayAgain();
                    calcScroll();
                    // scrolltop after adding new event   form reset
                    resetAddNew();
                });
                event.stopPropagation(); 
            };
            
            function resetAddNew(){
            	 $this.find('.events-list').scrollTop(0);
            	 $this.find('.add-new > input[type="text"]').val(lAddEvent[settings.lang]).select();
                 $addNew.removeAttr('event-data-id');
            };
            // delete event
            var deleteWorkEvent=function(_self,event){
				var id=_self.parents(".event-single").attr("data-id");
				//调用后台删除
				WorkCalendar.deleteNewCalendar(id,function(){
					$addNew.show();
					$this.find('div[data-id=' + id + ']').remove();
	                calcMonth();
	                calcScroll();
	                resetAddNew();
				});
				event.stopPropagation(); 
             };
             //edit event
             var editWorkEvent=function(_self,event){
            	 //日程信息
            	 var eventSingle=_self.parents(".event-single");
            	 var id=eventSingle.attr("data-id");
            	 var time=eventSingle.attr("data-time");
            	 var isAllDayFlag=eventSingle.attr("is-allday");
            	 var title=eventSingle.find('p').text();
            	 //初始化添加框
            	 var fieldset= $addNew.find('div.all-day').find('fieldset');
            	 $addNew.show().find('input[type="text"]').val(title).select();
            	 if(isAllDayFlag==1){
            		 doChooseAllDay(fieldset,'disabled');
            	 }else{
            		  var hour='00',min='00';
            		  try{
            			  hour=time.split(':')[0];
                		  min=time.split(':')[1];
            		  }catch(e){}
            		  $addNew.find('.hour > span').text(hour);
            		  $addNew.find('.min > span').text(min);
            		  doChooseAllDay(fieldset,'enabled');
            	 }
            	 $addNew.attr('event-data-id',id);
            	 event.stopPropagation(); 
             };
           //打开关联单据业务信息链接
             var openLinkBillUrl=function(_self,event){
            	 var id=_self.parents(".event-single").attr("data-id");
            	 var linkBillUrl=_self.attr('linkBillUrl');
 				WorkCalendar.openLinkBillUrl(id,linkBillUrl);
 				event.stopPropagation(); 
             };
         };

    }(jQuery));

});
var WorkCalendar = WorkCalendar || {};

WorkCalendar.parseEventHtml=function(o){
	var html=[];
	// <div class="added-event" data-id="5" data-date="17/12/2017" data-time="22:00" data-title="Lorem ipsum dolor sit amet"></div>
	html.push('<div class="added-event" data-id="',o['id'],'" data-date="',o['dataDate'],'" data-time="',o['dataTime'],'"');
	html.push(' data-title="',o['subject'],'" is-allday="',o['isAlldayevent'],'" link-bill-url="',(o['linkBillUrl']||''),'"></div>');
	return html.join('');
};
//初始化日历
WorkCalendar.initPersonWorkCalendar=function(el,options,fn){
	var op=options||{},$this=$(el);
	op['lang']='CN';
	var url=web_app.name+'/personOwn/intPersonCalendar.ajax';
	Public.ajax(url, {}, function(data) {
		op['customDay']=Public.parseDate(data['customDay']);
		op['viewPersons']=data['viewPersons'];
		var eventsEl=$('<div class="added-events"></div>');
		var html=[];
		$.each(data['calendarDatas'],function(i,o){
			html.push(WorkCalendar.parseEventHtml(o));
		});
		eventsEl.html(html.join(''));
		op['lang']='CN';
		op['workDateDuty']=data['companyWorkCalendar'];
		$this.append(eventsEl).jalendar(op);
		if($.isFunction(fn)){
			fn.call(window);
		}
	});
};
//重新查询日历数据
WorkCalendar.queryNewCalendarByPerson=function($this,personId,startDate,endDate,fn){
	var url=web_app.name+'/personOwn/queryCalendarByPerson.ajax';
	Public.ajax(url, {personId:personId,startDate:startDate,endDate:endDate}, function(data) {
		var html=[],eventsEl=$this.find('div.added-events');
		$.each(data['calendarDatas'],function(i,o){
			html.push(WorkCalendar.parseEventHtml(o));
		});
		eventsEl.html(html.join(''));
		fn.call(window,data);
	});
};

//保存新日程
WorkCalendar.doSaveNewCalendar=function(dataId,personId,subject,isAllDay,dataDate,dataTime,fn){
	var url=web_app.name+'/personOwn/updatePersoncalendar.ajax';
	if(Public.isBlank(dataId)){
		url=web_app.name+'/personOwn/insertPersoncalendar.ajax';
	}
	Public.ajax(url, {id:dataId,personId:personId,AAA:'AAA',subject:encodeURI(subject),isAlldayevent:isAllDay?1:0,dataDate:dataDate,dataTime:dataTime}, function(id) {
		fn.call(window,id);
	});
};

//删除日程
WorkCalendar.deleteNewCalendar=function(dataId,fn){
	if(!window.confirm("您确定删除选择的日程吗?")){
		return;
	}
	var url=web_app.name+'/personOwn/deletePersoncalendar.ajax';
	Public.ajax(url, {id:dataId}, function(data) {
		fn.call(window);
	});
};
//打开关联单据业务信息链接
WorkCalendar.openLinkBillUrl=function(dataId,linkBillUrl){
	var url=web_app.name +'/'+linkBillUrl;
	if(UICtrl.addTabItem){
		UICtrl.addTabItem({ tabid: 'linkBillUrl'+dataId, text: '日程详细信息', url:url});
	}
};
