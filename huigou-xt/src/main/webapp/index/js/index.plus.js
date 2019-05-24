function navToggle() {
    $(".navbar-minimalize").trigger("click");
}
function smoothlyMenu() {
    $("body").hasClass("mini-navbar") ? $("body").hasClass("fixed-sidebar") ? ($("#side-menu").hide(), setTimeout(function() {
        $("#side-menu").fadeIn(500)
    },300)) : $("#side-menu").removeAttr("style") : ($("#side-menu").hide(), setTimeout(function() {
        $("#side-menu").fadeIn(500)
    },100));
}
function localStorageSupport() {
    return "localStorage" in window && null !== window.localStorage
}
function contentMainResize(){
	var contentMain=$('#content-main');
    var _heigth=$(window).height();
    var _diff=$("body").hasClass('fixed-nav')?80:140;
    if($(window).width() < 769){
    	 _diff=$("body").hasClass('fixed-nav')?2:64;
    }
    contentMain.height(_heigth-_diff);
}
$(document).ready(function() {
    function e() {
        var e = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", e + "px");
    }
    
    $(".right-sidebar-toggle").click(function() {
        $("#right-sidebar").toggleClass("sidebar-open")
    });
    $(".sidebar-container").slimScroll({
        height: "100%",
        railOpacity:0.4,
        wheelStep: 10,
        size: '12px'
    });
    if ($.support.leadingWhitespace){
    	$('.navbar-right').show();
    }else{
    	setTimeout(function(){ $('.navbar-right').show();},500);
    }
   
    //页面最下 图标
    $(".open-small-chat").click(function() {
        $(this).children().toggleClass("fa-comments").toggleClass("fa-remove"),
        $(".small-chat-box").toggleClass("active")
    });
    
    $(function() {
        $(".sidebar-collapse").slimScroll({
            height: "100%",
            railOpacity: 0.9,
            size: '12px',
            color:'#f0f0f0',
            alwaysVisible: !1
        })
    });
    $(".navbar-minimalize").click(function() {
        $("body").toggleClass("mini-navbar");
        smoothlyMenu();
    });
    e();
    $(window).bind("load resize click scroll",function() {
        $("body").hasClass("body-small") || e();
    });
    $(window).scroll(function() {
        $(window).scrollTop() > 0 && !$("body").hasClass("fixed-nav") ? $("#right-sidebar").addClass("sidebar-top") : $("#right-sidebar").removeClass("sidebar-top");
    });
    
    setTimeout(function(){contentMainResize();},0);
	$.WinReszier.register(contentMainResize);
	
    $(".full-height-scroll").slimScroll({
        height: "100%"
    });
    $(".nav-close").click(navToggle);
    //解决：iphone iframe内的页面不能滑动问题
    /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent) && $("#content-main").css("overflow-y", "auto");
});
$(window).bind("load resize",function() {
    $(this).width() < 769 && ($("body").addClass("mini-navbar").addClass("collapse-menu"), $(".navbar-static-side").fadeIn());
});
$(function() {
    if ($("#fixednavbar").click(function() {
        $("#fixednavbar").is(":checked") ? ($(".navbar-static-top").removeClass("navbar-static-top").addClass("navbar-fixed-top"), $("body").removeClass("boxed-layout"), $("body").addClass("fixed-nav"), $("#boxedlayout").prop("checked", !1), localStorageSupport && localStorage.setItem("boxedlayout", "off"), localStorageSupport && localStorage.setItem("fixednavbar", "on")) : ($(".navbar-fixed-top").removeClass("navbar-fixed-top").addClass("navbar-static-top"), $("body").removeClass("fixed-nav"), localStorageSupport && localStorage.setItem("fixednavbar", "off"))
    }), $("#collapsemenu").click(function() {
        $("#collapsemenu").is(":checked") ? ($("body").addClass("mini-navbar").addClass("collapse-menu"), smoothlyMenu(), localStorageSupport && localStorage.setItem("collapse_menu", "on")) : ($("body").removeClass("mini-navbar").removeClass("collapse-menu"), smoothlyMenu(), localStorageSupport && localStorage.setItem("collapse_menu", "off"))
    }), $("#boxedlayout").click(function() {
        $("#boxedlayout").is(":checked") ? ($("body").addClass("boxed-layout"), $("#fixednavbar").prop("checked", !1), $(".navbar-fixed-top").removeClass("navbar-fixed-top").addClass("navbar-static-top"), $("body").removeClass("fixed-nav"), localStorageSupport && localStorage.setItem("fixednavbar", "off"), localStorageSupport && localStorage.setItem("boxedlayout", "on")) : ($("body").removeClass("boxed-layout"), localStorageSupport && localStorage.setItem("boxedlayout", "off"))
    }), $(".s-skin-0").click(function() {
    	$(".sidebar-collapse").parent().find('.slimScrollBar').css({background:'#f0f0f0'});
    	try{localStorageSupport && localStorage.setItem("bodySkin", "");}catch(e){}
        return $("body").removeClass("skin-1"),
        $("body").removeClass("skin-2"),
        $("body").removeClass("skin-3"),
        !1
    }), $(".s-skin-1").click(function() {
    	$(".sidebar-collapse").parent().find('.slimScrollBar').css({background:'#000000'});
    	try{localStorageSupport && localStorage.setItem("bodySkin", "skin-1");}catch(e){}
        return $("body").removeClass("skin-2"),
        $("body").removeClass("skin-3"),
        $("body").addClass("skin-1"),
        !1
    }), $(".s-skin-3").click(function() {
    	$(".sidebar-collapse").parent().find('.slimScrollBar').css({background:'#000000'});
    	try{localStorageSupport && localStorage.setItem("bodySkin", "skin-3");}catch(e){}
        return $("body").removeClass("skin-1"),
        $("body").removeClass("skin-2"),
        $("body").addClass("skin-3"),
        !1
    }), localStorageSupport) {
        var e = localStorage.getItem("collapse_menu"),
        a = localStorage.getItem("fixednavbar"),
        o = localStorage.getItem("boxedlayout");
        "on" == e && $("#collapsemenu").prop("checked", "checked"),
        "on" == a && $("#fixednavbar").prop("checked", "checked"),
        "on" == o && $("#boxedlayout").prop("checked", "checked")
    }
    if (localStorageSupport) {
        var e = localStorage.getItem("collapse_menu"),
        a = localStorage.getItem("fixednavbar"),
        o = localStorage.getItem("boxedlayout"),
        s = localStorage.getItem("bodySkin"),
        l = $("body");
        "on" == e && (l.hasClass("body-small") || l.addClass("mini-navbar").addClass("collapse-menu")),
        "on" == a && ($(".navbar-static-top").removeClass("navbar-static-top").addClass("navbar-fixed-top"), l.addClass("fixed-nav")),
        "on" == o && l.addClass("boxed-layout");
        if(s!=null&&"" !=s){
        	l.addClass(s);
        	setTimeout(function(){
        		$(".sidebar-collapse").parent().find('.slimScrollBar').css({background:'#000000'});
        	},100);
        }  
    }
});