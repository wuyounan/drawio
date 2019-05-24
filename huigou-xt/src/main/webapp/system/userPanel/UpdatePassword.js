
function initPasswordEvent(){
	var input=$('input[name="new"]',$('#updatePasswordForm'));
	input.bind('blur',function(){
		pswStrength($(this).val());
	}).bind('keyup',function(){
		pswStrength($(this).val());
	});
}

//测试某个字符是属于哪一类. 
function charMode(iN) {
  if (iN >= 48 && iN <= 57) //数字 
  return 1;
  if (iN >= 65 && iN <= 90) //大写字母 
  return 2;
  if (iN >= 97 && iN <= 122) //小写 
  return 4;
  else return 8; //特殊字符 
}

//计算出当前密码当中一共有多少种模式 
function bitTotal(num) {
  var modes = 0;
  for (var i = 0; i < 4; i++) {
      if (num & 1) modes++;
      num >>>= 1;
  }
  return modes;
}

//返回密码的强度级别 
function checkStrong(psw) {
  if (psw.length <= 3) return 0; //密码太短 
  var modes = 0;
  for (var i = 0; i < psw.length; i++) {
      //测试每一个字符的类别并统计一共有多少种模式. 
	  modes |= charMode(psw.charCodeAt(i));
  }
  return bitTotal(modes);
}

//当用户放开键盘或密码输入框失去焦点时,根据不同的级别显示不同的颜色 
function pswStrength(pwd) {
  var o_color = "#e0f0ff",l_color = "#FF0000",m_color = "#FF9900",h_color = "#33CC00";
  if (Public.isBlank(pwd)) {
	  $.each([1,2,3],function(i,o){
		  $('#passwordStrength'+o).css({backgroundColor:o_color});
	  });
      return;
  } else {
	  var level = checkStrong(pwd);
      switch (level) {
      case 0:
      case 1:
    	  $('#passwordStrength1').css({backgroundColor:l_color});
    	  $('#passwordStrength2').css({backgroundColor:o_color});
    	  $('#passwordStrength3').css({backgroundColor:o_color});
          break;
      case 2:
    	  $('#passwordStrength1').css({backgroundColor:o_color});
    	  $('#passwordStrength2').css({backgroundColor:m_color});
    	  $('#passwordStrength3').css({backgroundColor:o_color});
          break;
      default:
    	  $('#passwordStrength1').css({backgroundColor:o_color});
      	  $('#passwordStrength2').css({backgroundColor:o_color});
    	  $('#passwordStrength3').css({backgroundColor:h_color});
      }
  }
  return;
}

function doUpdatePassword(fn) {
	var _form=$('#updatePasswordForm');
    var oldPassword = $.trim($('input[name="old"]',_form).val());
    if (oldPassword == "") {
        Public.tip('原密码不能空。');
        return;
    }
    var newPassword = $.trim($('input[name="new"]',_form).val());
    if (newPassword == "") {
        Public.tip('新密码不能空。');
        return;
    }
    if (newPassword.length < 6) {
        Public.tip('密码长度应大于等于6位。');
        return;
    }
    var confirmPassword = $.trim($('input[name="confirm"]',_form).val());
    if (newPassword != confirmPassword) {
        Public.tip('两次输入的密码不一致。');
        return;
    }
    $('#updatePasswordForm').ajaxSubmit({
        url: web_app.name + '/org/updatePassword.ajax',
        param: {psw: $.base64.btoa(oldPassword), newPsw: $.base64.btoa(newPassword)},
        success: function () {
        	if($.isFunction(fn)){
        		fn.call(window);
        	}
        }
    });
}
