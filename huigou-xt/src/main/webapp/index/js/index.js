var forceCloseTabid=null;

$(document).ready(function() {
	//注册 tabs
	var _tabs=$('#content-tabs').contentTabs({
		iframeContentExpr:'#content-main',
		onBeforeRemoveTabItem:function(tabItem,iframe){
	    	//实现页面离开时检测当前页面是否被修改，修改则提示
	    	return onBeforeRemoveTabItem.call(this,tabItem,iframe);
	    }
	});
	
	//注册功能菜单
	$("#side-menu").functionMenus({
		onClick:function(id,code,description,action){
			if(Public.isBlank(action)){
				return;
			}
			var url = '';
			if(action.startsWith('http')){
				url=action;
			}else{
				url=web_app.name+ "/" +action;
				if(Public.isNotBlank(id)){
			        if (url.indexOf("?") >= 0){
			            url += "&functionId=" + id;
			        }else{
			            url += "?functionId=" + id;
			        }
				}
			}
	        addTabItem({ tabid:code, text:description,url:url});  
		}
	});
	
	//小屏幕显示的tab菜单栏目
	$('#page-tabs-menu').on('mousedown',function(e){
		var $el = $(e.target || e.srcElement);
		if($el.is('i.fa-times-circle')){
			var _menu=$el.parent();
			closeTabItem(_menu.data('id'));
			_menu.remove();
		}
		if($el.is('i.fa')) $el=$el.parent();
		if($el.is('span.label')) $el=$el.parent();
		if($el.is('a.count-info')){
			var _menu=$(this).find('.dropdown-menu').empty();
			$(_tabs).find('a.ui_menuTab').each(function(){
				var tabId=$(this).data('id'),text=$(this).text();
				if(tabId=='homepage'){
					_menu.append('<li><a href="javascript:void(0);" class="tab-menu" data-id="'+tabId+'">'+text+'</a></li>');
				}else{
					_menu.append('<li><a href="javascript:void(0);" class="tab-menu" data-id="'+tabId+'">'+text+'&nbsp;&nbsp;<i class="fa fa-times-circle"></i></a></li>');
				}
			});
		}
		if($el.is('a.tab-menu')){
			selectTabItem($el.data('id'));
		}
	});
	//语言切换
	$('.switch-language').on('click',function(e){
		var $el = $(e.target || e.srcElement);
		if($el.is('a')){
			var language=$el.data('language'),lang='',country='';
			language=language.split('_');
			lang=language[0];
			country=language.length>1?language[1]:'';
			Public.ajax( web_app.name +'/switchLanguage.ajax', {lang:lang,country:country}, function (data) {
				window.location.reload();
		    });
		}
	});
	
	//显示照片
	showPersonPicture();
	//密码过期校验
	applySecurityPrompt();
});

function switchFullScreen(){
	if (!screenfull.enabled){
		return false;
	}
	screenfull.toggle();
}

function showPersonPicture(){
	var _img=$('#showPersonPicture'),url = web_app.name + '/personOwn/loadPersonPicture.ajax';
    Public.ajax(url, {}, function (data) {
    	if (!Public.isBlank(data)) {
    		_img[0].src = $.getCSRFUrl('attachment/downFileBySavePath.ajax',{file:data});
    		_img[0].onload = function(){
				setTimeout(function(){_img.show();},0);
			};
    	}else{
    		_img.show();
    	}
    });
}

//实现页面离开时检测当前页面是否被修改，修改则提示
function onBeforeRemoveTabItem(tabItem,iframe){
	var _self = this,tabid=$(tabItem).data('id');
	//判断强制关闭标志
	if(forceCloseTabid==tabid){
		forceCloseTabid=null;
		return true;
	}
	try{
        var jframe = $(iframe)[0]; 
        var fn=jframe.contentWindow['checkDefaultValueModified'];
        if($.isFunction(fn)){
        	var flag=fn.call(window);
        	if(flag===true){
        		UICtrl.confirm('您修改的数据尚未保存，确定离开此页面吗？',function(){
        			//设置强制关闭标志
        			forceCloseTabid=tabid;
        			_self.closeTab(tabItem);
        		});
        		return false;
        	}else{
        		return true;
        	}
        }else{
        	return true;
        }
	}catch(e){
		return true;
	}
}

//添加标签页
function addTabItem(options){
	$('#content-tabs').contentTabs('addTabItem',options);
}

function selectTabItem(tabId){
	$('#content-tabs').contentTabs('selectTabItem',tabId);
}

function getCurrentTabId() {
     return $('#content-tabs').contentTabs('getActiveTabId');
}

function closeTabItem(tabId) {
    if (!tabId||tabId=='') {
    	tabId = getCurrentTabId();
    }
    if(!tabId||tabId=='homepage') return;
    $('#content-tabs').contentTabs('removeTabItem',tabId);
}

function reloadParentTab(parentTabId){
	try {
		selectTabItem(parentTabId);
		reloadTabById(parentTabId);
    }catch (e) { }
}

function closeTabItemAndReloadParentTab(parentTabId){
	closeTabItem();
	reloadParentTab(parentTabId);
}

function reloadTabById(tabId){
	var iframe = $('#content-tabs').contentTabs('getIFrame',tabId);
	if(iframe){
		var _fn=iframe[0].contentWindow.reloadGrid;
		if($.isFunction(_fn)){
			_fn.call(window);
		}else{
			$('#content-tabs').contentTabs('reload',tabId);
		}
	}else{
		$('#content-tabs').contentTabs('reload',tabId);
	}
}

//登出
function doLogout(){
	window.location.href = web_app.name +'/logout.do';
}

function switchOperator() {
	UICtrl.switchOperator(function(){
		window.location.href = web_app.name +'/Index.jsp';
	});
}

function switchTenant() {
    var url = web_app.name + '/tenant/findSubordinationTenants.ajax';
    Public.ajax(url, {}, function (data) {
        showSwitchTenant(data);
    });
}

function showSwitchTenant(data) {
	if(!data.length){
		return;
	}
    var html = ['<table width="100%" border=0 cellspacing=0 cellpadding=0 >'];
    $.each(data, function (i, o) {
        html.push('<tr class="list" id="', o['id'], '">');
        html.push('<td>', o['name'], '</td>');
        html.push('</tr>');
    });
    html.push('</table>');
    var options = {
        title: '切换租户',
        content: html.join(''),
        width: 300,
        opacity: 0.1,
        onClick: function ($el) {
            if ($el.is('td')) {
                var tenantId = $el.parent().attr('id');
                Public.ajax(web_app.name + '/authentication/switchTenant.ajax', {tenantId: tenantId}, function (data) {
                	window.location.href = web_app.name +'/Index.jsp';
                });
            }
        },
        onMousemove: function ($el) {
            var $tr = $el.parent('tr'), table = $tr.parent();
            if ($tr.length == 0 || !$tr.hasClass('list') || $tr.hasClass('over') || $tr.hasClass('seleced')) return false;
            table.find('tr.over').removeClass('over');
            $tr.addClass('over');
        }
    };
    Public.dialog(options);
}


//打开任务中心
function showTaskCenter(taskKind){
	var tabId='TaskCenter';
	try {
		var iframe = $('#content-tabs').contentTabs('getIFrame',tabId);
		if(iframe){
			selectTabItem(tabId);
			var _fn=iframe[0].contentWindow.reloadTaskGrid;
			if($.isFunction(_fn)){
				_fn.call(window,taskKind);
			}
		}else{
			var url=web_app.name+'/workflow/forwardTaskCenter.do?viewTaskKind='+taskKind;
			addTabItem({ tabid: tabId, text: '任务中心 ', url: url});
		} 
    } catch (e) { }
}

function applySecurityPrompt(){
    if (SecurityPrompt.passwordExpired == "true") {
    	Public.load(web_app.name + '/org/showUpdatePassword.load', {}, function (data) {
    		var options = {title: '修改密码',content: data, width: 340,
    			onClick: function ($el) {
    				if($el.hasClass('btn-success')){
    					var _self = this;
    					doUpdatePassword(function(){
    						_self.close();
    					});
    				}
    		    }
    		};
    		var div=Public.dialog(options);
    		div.css({zIndex:2005}).find('a.ui-public-dialog-close').hide();
    		$('#jquery-screen-over').css('zIndex',2004);
    		$('#updatePasswordForm').after('<div class="clearfix text-align-right p-r-xs"><button type="button" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;确定</button></div>');
    		initPasswordEvent();
        });
        return;
    }
    if (SecurityPrompt.updatePasswordPrompt == 'true'){
		UICtrl.alert( '&nbsp;您的密码将会在' + SecurityPrompt.passwordRemainderDays + '天后过期，请修改密码。');
	}
}
/*******iframe内容页属性 contentMainScrollTop*********/
function contentScrollTop(){
	return $('#content-main').scrollTop();
}

function contentScrollLeft(){
	return $('#content-main').scrollLeft();
}

function contentHeight(){
	return $('#content-main').height();
}

function contentWidth(){
	return $('#content-main').width();
}
