package com.huigou.uasp.log.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志状态
 * 
 * @author yuanwf
 *
 */
public enum LogStatus {
	
	SUCCESS(1, "成功"), FAILURE(0, "失败");

	private int id;

	private String name;

	private LogStatus(int id, String displayName) {
		this.id = id;
		this.name = displayName;
	}

	public static LogStatus fromId(int id) {
		switch (id) {
		case 1:
			return SUCCESS;
		case 0:
			return FAILURE;
		}
		throw new RuntimeException(String.format("无效日志状态类型“%s”。", new Object[] { Integer.valueOf(id) }));
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public static Map<String, String> getData() {
		Map<String, String> map = new HashMap<String, String>(LogStatus.values().length);
		for (LogStatus c : LogStatus.values()) {
			map.put(String.valueOf(c.getId()), c.getName());
		}
		return map;
	}
}
