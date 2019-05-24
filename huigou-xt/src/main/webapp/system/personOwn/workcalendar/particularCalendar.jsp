<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="particularTable">
	<COLGROUP><COL width="15%"><COL width="85%"></COLGROUP>
	<input type='hidden' id='c_id' value='<c:out value="${particularMap.id}"/>'/>
	<tr>
		<td style='text-align:right;'>内容:</td>
		<td style='text-align:left;'>	
			<input type="text" id="c_subject" class="text" style="width:400px;" maxlength="100" value='<c:out value="${particularMap.subject}"/>'>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td style='text-align:left;'>
			<input type="text" id="c_startDate" class="text textDate" date="true" style="width:100px;float:left;">
			<div class="ui-combox-wrap" id="showStartTime"  style="width:60px;display:inline-block;margin-left:3px;float:left;">
				<input type="text" id="c_startTime" class="text" mask="nn:nn">
				<span class="combo"></span>
			</div>
			<span style="float:left;margin-left:3px;margin-right:3px;line-height:25px;">&nbsp;到&nbsp;</span>
			<input type="text" id="c_endDate" class="text textDate" date="true" style="width:100px;float:left;">
			<div class="ui-combox-wrap" id='showEndTime' style="width:60px;display:inline-block;margin-left:3px;float:left;">
				<input type="text" id="c_endTime" class="text" mask="nn:nn">
				<span class="combo"></span>
			</div>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td style='text-align:left;'>
			<label><input type="checkbox" id="c_isalldayevent" value="1">&nbsp;全天&nbsp;&nbsp;</label> 
			<label><input type="checkbox" id="c_isrepeat" value="1" <c:if test="${particularMap.isRepeat=='1'}">checked</c:if> >&nbsp;重复&nbsp;&nbsp;</label> 
		</td>
	</tr>
	<tr id='showRepeatTr' style="position: relative;">
		<td><input type='hidden' id='c_schedules' value='<c:out value="${particularMap.schedulesString}"/>'/></td>
		<td style='text-align:left;'>
			 <fieldset style='width:410px;margin:3px;'> 
			  <legend>重复日程设置</legend>
			 <div style="height:28px;">  
			 	<span style="float:left;line-height:25px;">重复:&nbsp;&nbsp;</span>
				<div style='width:70px;margin:0px;float:left;'>
					<select id="c_repeattype">
						<option value="1" <c:if test="${particularMap.repeat.repeatType=='1'}">selected</c:if>>每天</option> 
						<option value="2" <c:if test="${particularMap.repeat.repeatType=='2'}">selected</c:if>>每周</option>
	                    <option value="3" <c:if test="${particularMap.repeat.repeatType=='3'}">selected</c:if>>每月</option>  
	                    <option value="4" <c:if test="${particularMap.repeat.repeatType=='4'}">selected</c:if>>每年</option> 
					</select>
				</div>
				<span style="float:left;margin-left:3px;line-height:25px;">&nbsp;&nbsp;重复频率:&nbsp;&nbsp;</span>
				<div class="ui-combox-wrap" style="width:60px;float:left;">
					<input type="text" id="c_frequency" class="text" value='<c:out value="${particularMap.repeat.frequency}"/>'>
					<span class="combo"></span>
				</div>
				<div class="clear" style="margin:0;"></div>
             </div>
			 <div id="repeatPoint_2" class='hide'> 
				<label>重复时间:</label>
				<span id='showWeek_repeat'></span>
             </div> 
			 <div id="repeatPoint_3" class='hide'> 
				<label>重复时间:</label>&nbsp;<a href='#' class='combox_button' id='addDay_month'>添加...</a>
				<span id='showMonth_repeat' title='双击可删除选中日期'></span>
             </div> 
			 <div id="repeatPoint_4" class='hide'> 
				<label>重复时间:</label>&nbsp;<a href='#' id='addDay_year'>添加...</a>
				<span id='showYear_repeat' title='双击可删除选中日期'></span>
             </div> 
			 <div>  
				 <label>开始时间:</label> 
				 <input type="text" id="temp_startDate" class="text textDate" date="true" style="width:100px;" value='<c:out value="${particularMap.startDateString}"/>'/>
             </div>  
			 <div>
                  <label>结束日期:</label>  
                  <label class="radio">
                      <input name="repeatendtype" id="repeatendtype1" type="radio" value='1' <c:if test="${particularMap==null||particularMap.repeat.repeatEndType=='1'}">checked</c:if>/>&nbsp;从不
                  </label> 
                  <label class="radio">  
                      <input name="repeatendtype" id="repeatendtype2" type="radio" value='2' <c:if test="${particularMap.repeat.repeatEndType=='2'}">checked</c:if>/>&nbsp;发生
					  <input id="endCount" type="text" class="text" mask="nnn" style="width:30px;" value='<c:out value="${particularMap.repeat.amount}"/>'/>&nbsp;次后
                  </label>  
                  <label class="radio"> 
                      <input name="repeatendtype" id="repeatendtype3" type="radio" value='3' <c:if test="${particularMap.repeat.repeatEndType=='3'}">checked</c:if>/>&nbsp;在
					  <input id="repeatendDate"  style="width:100px;" class='text textDate' date="true" value='<c:out value="${particularMap.endDateString}"/>'/>
                  </label> 
             </div>
			 </fieldset>
		</td>
	</tr>
	<tr>
		<td style='text-align:right;'>类型:</td>
		<td style='text-align:left;'>
		    <div style='width:120px;padding: 0px;'>
				<select id='c_calendartype' >
					<option value='1' <c:if test="${particularMap.calendarType=='1'}">checked</c:if>>个人日程</option>
					<option value='2' <c:if test="${particularMap.calendarType=='2'}">checked</c:if>>部门日程</option>
					<option value='3' <c:if test="${particularMap.calendarType=='2'}">checked</c:if>>机构日程</option>
					<option value='4' <c:if test="${particularMap.calendarType=='2'}">checked</c:if>>岗位日程</option>
				</select>
			</div>
		</td>
	</tr>
	<tr>
		<td style='text-align:right;'>说明:</td>
		<td style='text-align:left;'>
			<textarea name="content" class="inputarea" id='c_description'><c:out value="${particularMap.description}"/></textarea>
		</td>
	</tr>
	<tr>
		<input type='hidden' id='c_className' value='<c:out value="${particularMap.className}"/>'/>
		<td style='text-align:right;'>颜色:</td>
		<td style='text-align:left;'>
			<ul class="showColor" id='showColorUl'></ul>
		</td>
	</tr>
</table>