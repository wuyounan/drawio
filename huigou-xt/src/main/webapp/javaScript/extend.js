/*************************************
 * Javascript 基础类扩展
 * 
 * String 对象扩展
 * 		  endsWith 
 *        startsWith
 *        replaceAll
 *        beforeSupplement 左补齐字符串
 *        afterSupplement 右补齐字符串
 *        movePoint 移动小数点位置 用于数学计算
 *        
 * Number 扩展解决浮点数数学运算时结果异常的问题
 * 	      toFixed  解决浏览器兼容问题
 *        div(num) 除法
 *        mul(num) 乘法
 *        add(num) 加法
 *        sub(num) 减法
 ************************************/

/**********String 对象扩展***********/
$.extend(String.prototype, {
    endsWith: function (f) {
        var reg = new RegExp("\\s*" + f + "($)", "i");
        if (reg.test(this))
            return true;
        return false;
    },
    startsWith: function (f) {
        var reg = new RegExp("(^)" + f + "\\s*", "i");
        if (reg.test(this))
            return true;
        return false;
    },
    /**实现英文首字母大写**/
    ucFirst: function(str) {
    	return this.replace(/\b\w+\b/g, function(word){
    	  return word.substring(0,1).toUpperCase()+word.substring(1);
    	});
    },
    replaceAll: function(findText, repText) { 
    	var reg = new RegExp(findText, "g"); 
    	return this.replace(reg, repText); 
    },
    /**左补齐字符串**/
    beforeSupplement:function(digit){
    	var _value=this;
        if (_value.length < digit) {
        	_value = (Array(digit+1).join(0) + _value).slice(digit*-1);
        }
        return _value;
    },
    /**右补齐字符串**/
    afterSupplement:function(digit){
    	var _value=this;
    	if (_value.length < digit) {
    		_value = (_value + Array(digit+1).join(0)).slice(0, digit);
    	}
        return _value;
    },
    /**
     * 移动小数点位置（用于数学计算，相当于（乘以/除以）Math.pow(10,scale)）
     * 
     * @param scale 要移位的刻度（正数表示向右移；负数表示向左移动；0返回原值）
     * @return
     */
    movePoint:function(scale){
    	var _value=this;
    	if (_value==null||_value.length==0) {
    		return '';
    	}
    	if (scale == 0){
    		return _value;
    	}
    	var _scale=Math.abs(scale);
    	var _ch='.', _ps=_value.split(_ch),_sign='';
    	var _int=_ps[0],_dec = _ps[1] ? _ps[1] : '';
    	if(scale > 0){//右移
    		if (_dec.length <= _scale) {
    			_ch = '';
    			_dec = _dec.afterSupplement(_scale);
    		}
    		return _int + _dec.slice(0, _scale) + _ch + _dec.slice(_scale, _dec.length);
    	}else{//左移
    		if (_int.slice(0, 1) == '-') {
    			_int = _int.slice(1);
    			_sign = '-';
    		}
    		if (_int.length <= _scale) {
    			_ch = "0.";
    			_int = _int.beforeSupplement(_scale);
    		}
    		return _sign + _int.slice(0, _scale*-1) + _ch + _int.slice(_scale*-1) + _dec;
    	}
    },
    numberAbs:function(){
    	var _value=this;
    	if (_value==null||_value.length==0) {
    		return '';
    	}
    	var val=parseFloat(_value,10);
		if(isNaN(val)) return '';
		return Math.abs(val)+'';
    }
});

var MathUtil = MathUtil || {};

MathUtil.checkNumber=function(value){
	if (Public.isBlank(value)) {
		return "0";
	}
	value=value.toString();
	value=value.replace(/[,]/g, '');
	value=value.replace(/[_]/g, '');
	var num=parseFloat(value,10);
	if(isNaN(num)){
		return "0";
	}
    return num.toString();
};
/**
 * 获取小数位长度
 */
MathUtil.decimalLength=function(arg){
	if (arg==null||arg.length==0) {
		return 0;
	}
	var _s=arg.split(".");
	if(_s.length>1){
		return _s[1].length;
	}
	return 0;
};
/**
 * 除法函数，用来得到精确的除法结果
 */
MathUtil.div=function(arg1, arg2) {
	var s1 = MathUtil.checkNumber(arg1), s2 = MathUtil.checkNumber(arg2);
	var t1=MathUtil.decimalLength(s1),t2=MathUtil.decimalLength(s2);
	var r1 = Number(s1.replace(".", "")),r2 = Number(s2.replace(".", ""));
	return Number((r1 / r2).toString().movePoint(t2 - t1));	
};

/**
 * 乘法函数，用来得到精确的乘法结果
 */
MathUtil.mul=function(arg1, arg2) {
	var s1 = MathUtil.checkNumber(arg1), s2 = MathUtil.checkNumber(arg2);
	var m=MathUtil.decimalLength(s1) + MathUtil.decimalLength(s2);
	var r1 = Number(s1.replace(".", "")),r2 = Number(s2.replace(".", ""));	
	return Number((r1 * r2).toString().movePoint(-m));
};

/**
 * 加法函数，用来得到精确的加法结果
 */
MathUtil.add=function(arg1, arg2) {
	var s1 = MathUtil.checkNumber(arg1), s2 = MathUtil.checkNumber(arg2);
	var t1=MathUtil.decimalLength(s1),t2=MathUtil.decimalLength(s2);
	var m = Math.max(t1, t2);
	return Number((Number(s1.movePoint(m))+Number(s2.movePoint(m))).toString().movePoint(-m));	
};

/**
 * 减法函数，用来得到精确的加法结果
 */
MathUtil.sub=function(arg1, arg2) {
	var s1 = MathUtil.checkNumber(arg1), s2 = MathUtil.checkNumber(arg2);
	var t1=MathUtil.decimalLength(s1),t2=MathUtil.decimalLength(s2);
	var m = Math.max(t1, t2);
	return Number((Number(s1.movePoint(m))-Number(s2.movePoint(m))).toString().movePoint(-m));
};
/**大小比较**/
MathUtil.compare=function(max,min){
	var _max = MathUtil.checkNumber(max), _min = MathUtil.checkNumber(min);
	var f1=parseFloat(_max),f2=parseFloat(_min);
	if(f1>f2){
		return 1;
	}
	if(f1==f2){
		return 0;
	}
	if(f1<f2){
		return -1;
	}
};
MathUtil.floor=function(num,scale){
	var _num = MathUtil.checkNumber(num);
	var _n=Number(_num.toString().movePoint(scale));
	_n=Math.floor(_n);
	return Number(_n.toString().movePoint(-scale));
};
/**********Number 对象扩展***********/
$.extend(Number.prototype, {
	/**
	 * 重写toFixed方法，解决浏览器兼容问题
	 */
	toFixed:function(scale){
		var s, s1=this + "", s2, start=s1.indexOf(".");  
	    s = s1.movePoint(scale);
	    if (start >= 0){  
	        s2 = Number(s1.substr(start + scale + 1, 1));  
	        if (s2 >= 5 && this >= 0 || s2 < 5 && this < 0){  
	            s = Math.ceil(s);  
	        }else{  
	            s = Math.floor(s);  
	        }  
	    }  
	    return s.toString().movePoint(-scale); 
	},
	toFloor:function(scale){
		return MathUtil.floor(this,scale);
	},
	div:function(arg){
		return MathUtil.div(this,arg);
	},
	mul:function(arg){
		return MathUtil.mul(this,arg);
	},
	add:function(arg){
		return MathUtil.add(this,arg);
	},
	sub:function(arg){
		return MathUtil.sub(this,arg);
	}
});


/************日期 date 扩展***************/
/**
 * 日期工具
 * **/
var DateUtil = DateUtil || {};
/**
 * 获取当前月的第一天
 */
DateUtil.getCurrentMonthFirstDay = function () {
    var date = new Date();
    date.setDate(1);
    return date;
};
/**
 * 获取当前月的最后一天
 */
DateUtil.getCurrentMonthLastDay = function () {
    var date = new Date();
    var currentMonth = date.getMonth();
    var nextMonth = ++currentMonth;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    var oneDay = 1000 * 60 * 60 * 24;
    return new Date(nextMonthFirstDay - oneDay);
};
/**
 *
 * 获取下月第一天
 */
DateUtil.getLastMonthFirstDay = function () {
    var date = new Date();
    var currentMonth = date.getMonth();
    var nextMonth = ++currentMonth;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    return new Date(nextMonthFirstDay);
};
/**
 *
 * 获取下月最后一天
 */
DateUtil.getLastMonthLastDay = function () {
    var date = new Date();
    var currentMonth = date.getMonth();
    var nextMonth = currentMonth + 2;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    var oneDay = 1000 * 60 * 60 * 24;
    return new Date(nextMonthFirstDay - oneDay);
};
/**
 * 扩展日志对象
 */
DateUtil._date=function(dateValue){
	var _date = dateValue;
    if (typeof dateValue == 'string') {
        if (dateValue.length > 11) {
        	_date = Public.parseDate(dateValue, '%Y-%M-%D %H:%I');
        } else {
        	_date = Public.parseDate(dateValue);
        }
    }
    this.date = _date;
    this.year = _date.getFullYear();
    this.month = _date.getMonth() + 1;
    this.day = _date.getDate();
    this.hour = _date.getHours();
    this.minute = _date.getMinutes();
    this.second = _date.getSeconds();
    this.msecond = _date.getMilliseconds();
    this.week = _date.getDay();
};

/**
 * 取时间差
 */
DateUtil.diff = function (interval, date1, date2) {
    var _date1 = new DateUtil._date(date1),
    	_date2 = new DateUtil._date(date2),
    	result =null;
    switch (String(interval).toLowerCase()) {
        case "y":
        case "year":
            result = _date1.year - _date2.year;
            break;
        case "n":
        case "month":
            result = (_date1.year - _date2.year) * 12 + (_date1.month - _date2.month);
            break;
        case "d":
        case "day":
            result = Math.round((Date.UTC(_date1.year, _date1.month - 1, _date1.day) - Date.UTC(_date2.year, _date2.month - 1, _date2.day)) / (1000 * 60 * 60 * 24));
            break;
        case "h":
        case "hour":
            result = Math.round((Date.UTC(_date1.year, _date1.month - 1, _date1.day, _date1.hour) - Date.UTC(_date2.year, _date2.month - 1, _date2.day, _date2.hour)) / (1000 * 60 * 60));
            break;
        case "m":
        case "minute":
            result = Math.round((Date.UTC(_date1.year, _date1.month - 1, _date1.day, _date1.hour, _date1.minute) - Date.UTC(_date2.year, _date2.month - 1, _date2.day, _date2.hour, _date2.minute)) / (1000 * 60));
            break;
        case "s":
        case "second":
            result = Math.round((Date.UTC(_date1.year, _date1.month - 1, _date1.day, _date1.hour, _date1.minute, _date1.second) - Date.UTC(_date2.year, _date2.month - 1, _date2.day, _date2.hour, _date2.minute, _date2.second)) / 1000);
            break;
        case "ms":
        case "msecond":
            result = Date.UTC(_date1.year, _date1.month - 1, _date1.day, _date1.hour, _date1.minute, _date1.second, _date1.msecond) - Date.UTC(_date2.year, _date2.month - 1, _date2.day, _date2.hour, _date2.minute, _date2.second, _date1.msecond);
            break;
        case "w":
        case "week":
            result = Math.round((Date.UTC(_date1.year, _date1.month - 1, _date1.day) - Date.UTC(_date2.year, _date2.month - 1, _date2.day)) / (1000 * 60 * 60 * 24)) % 7;
            break;
        default:
            result = "invalid";
    }
    return (result);
};

/**
 * 时间加减
 */
DateUtil.add = function (interval, num, dateValue) {
	var _date = new DateUtil._date(dateValue);
    switch (String(interval).toLowerCase()) {
        case "y":
        case "year":
            _date.year += num;
            break;
        case "n":
        case "month":
            _date.month += num;
            break;
        case "d":
        case "day":
            _date.day += num;
            break;
        case "h":
        case "hour":
            _date.hour += num;
            break;
        case "m":
        case "i":
        case "minute":
            _date.minute += num;
            break;
        case "s":
        case "second":
            _date.second += num;
            break;
        case "ms":
        case "msecond":
            _date.msecond += num;
            break;
        case "w":
        case "week":
            _date.day += num * 7;
            break;
        default:
            return ("invalid");
    }
    var _now=[_date.year,'/'];
    _now.push(_date.month,'/');
    _now.push(_date.day,' ');
    _now.push(_date.hour,':');
    _now.push(_date.minute,':');
    _now.push(_date.second);
    return (new Date(_now.join('')));
};