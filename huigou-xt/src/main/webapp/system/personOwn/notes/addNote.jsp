<%@ page contentType="text/html; charset=utf-8" language="java"%>
<div id='showmsg'></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="posttable" style="width:400px;">
	<COLGROUP><COL width="15%"><COL width="85%"></COLGROUP>
	<tr>
		<td style='text-align:left;padding-left:10px;'>背景色:</td>
		<td style='text-align:left;'>	
			<input type="hidden" id="add_color" name="color" value="yellow" />
			<ul id="showColor">
				<li class="yellow cur"></li>
				<li class="blue"></li>
				<li class="green"></li>
				<li class="red"></li>
			</ul>
		</td>
	</tr>
	<tr>
		<td style='text-align:left;padding-left:10px;'>内容:</td>
		<td style='text-align:left;'>
			<textarea name="content" class="inputarea" id='add_content'></textarea>
		</td>
	</tr>
</table>