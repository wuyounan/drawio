package com.huigou.uasp.bmp.portal.mainpage.domain.model;

/**
 * 用户桌面功能类
 * 
 * @ClassName: UserScreenFunction
 * @Description: TODO
 * @author 
 * @date 2014-2-25 上午09:58:52
 * @version V1.0
 */
public class UserScreenFunction {
	protected Long screenId;

	protected String functionId;

	protected String title;

	protected String icon;

	protected String location;

	public Long getScreenId() {
		return screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		if (icon == null || icon.equals("")) {
			this.icon = "/desktop/images/default.png";
		} else {
			this.icon = icon;
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
