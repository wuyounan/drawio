__CreateJSPath = function (js) {
    var scripts = document.getElementsByTagName("script");
    var path = "";
    for (var i = 0, l = scripts.length; i < l; i++) {
        var src = scripts[i].src;
        if (src.indexOf(js) != -1) {
            var ss = src.split(js);
            path = ss[0];
            break;
        }
    }
    var href = location.href;
    href = href.split("#")[0];
    href = href.split("?")[0];
    var ss = href.split("/");
    ss.length = ss.length - 1;
    href = ss.join("/");

    if (path.indexOf("http:") == -1 && path.indexOf("file:") == -1) {
        path = href + "/" + path;
    }
    return path;
};

var bootPATH = __CreateJSPath("boot.js");

document.write('<link href="' + bootPATH + 'miniui/themes/icons.css" rel="stylesheet" type="text/css" />');

document.write('<link href="' + bootPATH + 'miniui/themes/default/miniui.css" rel="stylesheet" type="text/css" />');
document.write('<link href="' + bootPATH + 'miniui/themes/gray/skin.css" rel="stylesheet" type="text/css" />');

document.write('<script src="' + bootPATH + 'miniui/miniui.js" type="text/javascript"></script>');
document.write('<script src="' + bootPATH + 'miniui/locale/zh_CN.js" type="text/javascript"></script>');

//project
document.write('<script src="' + bootPATH + 'plusproject/CalendarWindow.js" type="text/javascript"></script>');
document.write('<script src="' + bootPATH + 'plusproject/ProjectMenu.js" type="text/javascript"></script>');
document.write('<script src="' + bootPATH + 'plusproject/StatusColumn.js" type="text/javascript"></script>');
document.write('<script src="' + bootPATH + 'plusproject/TaskWindow.js" type="text/javascript"></script>');
document.write('<script src="' + bootPATH + 'plusproject/ProjectServices.js" type="text/javascript"></script>');

document.write('<script src="' + bootPATH + 'ThirdLibs/swfobject/swfobject.js" type="text/javascript"></script>');
