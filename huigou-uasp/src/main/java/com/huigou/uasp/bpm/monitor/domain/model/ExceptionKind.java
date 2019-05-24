package com.huigou.uasp.bpm.monitor.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常类型枚举
 * 
 * @author renxs
 * 
 */
public enum ExceptionKind {
	NONE(0, "抽检"), WARN(1, "临近办理限期预警"), EXPIRE(2, "业务办理过期"), ABNORMAL(3, "异常流程");

	private int id;
	private String displayName;

	private ExceptionKind(int id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public static ExceptionKind fromId(int id) {
		switch (id) {
		case 0:
			return NONE;
		case 1:
			return WARN;
		case 2:
			return EXPIRE;
		case 4:
			return ABNORMAL;
		}
		throw new RuntimeException(String.format("无效的异常类型“%s”!", new Object[] { Integer.valueOf(id) }));
	}

	public static void main(String[] args) {
		System.out.println(ExceptionKind.fromId(1).getDisplayName());
	}

	public int getId() {
		return this.id;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public static Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>(ExceptionKind.values().length);
		for (ExceptionKind c : ExceptionKind.values()) {
			map.put(String.valueOf(c.getId()), c.getDisplayName());
		}
		return map;
	}
}
