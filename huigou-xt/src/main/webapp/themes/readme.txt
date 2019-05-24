解决IE8出现图标字体渲染失败的问题:

IE8下，自定义的字体，也就是font-face那部分，只需要在主框架，就是top的页面里面加载一次，其他所有页面都可以调用这里定义的字体，iframe里面就不要再次加载了，会造成问题。但是高版本和其他非IE浏览器，是需要每个iframe单独加载。

增加文件: font-face.css(浏览器字体加载)

删除文件中@font-face 的定义,涉及文件:
 font-awesome.css font-awesome.min.css 
 bootstrap.css bootstrap.min.css 

具体实现:
Index.jsp 中默认引入font-face.css

在main.jsp 及 job.jsp中 使用 
<!--[if !IE]><!--> 除IE外都可识别 <!--<![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><link href='<c:url value="/themes/font-face.css"/>' rel="stylesheet" type="text/css" /> <!--<![endif]-->
