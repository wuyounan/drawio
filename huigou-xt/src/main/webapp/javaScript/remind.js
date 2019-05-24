/*消息提醒通用显示*/
function bindRemindEvent() {
    $("#messageRemindContent").on('click', function (e) {
        var $clicked = $(e.target || e.srcElement);
        if ($clicked.is('a.aLink')) {
            showRemindTab($clicked.attr('code'), $clicked.attr('name'), $clicked.attr('openKind'), $clicked.attr('url'));
            return false;
        }else if($clicked.parent().is('a.aLink')){
        	var parent=$clicked.parent();
        	showRemindTab(parent.attr('code'), parent.attr('name'), parent.attr('openKind'), parent.attr('url'));
        	return false;
        }
    });
    $('#messageRemindRefresh').on('click', function (e) {
    	loadRemindByPerson();
        return false;
    });
    loadRemindByPerson();
}

function loadRemindByPerson() {
    Public.ajax(web_app.name + "/messageRemind/loadRemindByPerson.ajax", {}, function (data) {
        var html = [];
        $.each(data, function (i, o) {
            html.push('<div class="height-auto-list">');
            html.push('<a href="javascript:void(null);" class="aLink" ');
            html.push('code="', o.code, '" ');
            html.push('openKind="', o.openKind, '" ');
            html.push('name="', o.name, '" ');
            html.push('url="', o.remindUrl, '">');
            html.push(o.remindTitle, '</a>');
            html.push('</div>');
        });
        $("#messageRemindContent").html(html.join(""));
    });
}

function showRemindTab(code, name, openKind, action) {
    if (Public.isBlank(action)) return;
    if(action.startsWith('http')){
    	window.open(action);
    	return;
    }
    var url = web_app.name + '/' + action;
    var kind = parseInt(openKind, 10);
    if (kind == 0) {
    	//shortcutFunJump()
        UICtrl.addTabItem({tabid: code, text: name, url: url});
    } else {
        UICtrl.showFrameDialog({
            title: name,
            url: url,
            resize: true,
            ok: false,
            cancelVal: 'common.button.close',
            cancel: true
        });
    }
}