package com.huigou.uasp.bpm.monitor.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 询问状态枚举
 * @author renxs
 *
 */
public enum AskStatus {
	UNASK(1, "未询问"), ASK(2, "已询问"), ABORTED(3, "已终结");

	private int id;
	private String displayName;

	private AskStatus(int id, String displayName) {
		this.id = id;
		this.displayName = displayName;		
	}

	public static AskStatus fromId(int id) {
		switch (id) {
		case 1:
			return UNASK;
		case 2:
			return ASK; 
		case 3:
			return ABORTED; 
		}
		throw new RuntimeException(String.format("无效的询问状态“%s”!", new Object[] { Integer.valueOf(id) }));
	}
	
	public static void main(String[] args) {
		System.out.println(AskStatus.fromId(1).getDisplayName());
	}

	public int getId() {
		return this.id;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public static Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>(AskStatus.values().length);
		for (AskStatus c : AskStatus.values()) {
			map.put(String.valueOf(c.getId()), c.getDisplayName());
		}
		return map;
	}
}
