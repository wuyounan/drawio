package com.huigou.uasp.bpm.monitor.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 询问明细状态枚举
 * @author renxs
 *
 */
public enum AskDetailStatus {
	UNREPLY(0, "未回复"), REPLY(1, "已回复"),  ABORTED(2, "已终结");

	private int id;
	private String displayName;

	private AskDetailStatus(int id, String displayName) {
		this.id = id;
		this.displayName = displayName;		
	}

	public static AskDetailStatus fromId(int id) {
		switch (id) {
		case 0:
			return UNREPLY;
		case 1:
			return REPLY; 	
			case 2:
				return ABORTED; 
		}
		throw new RuntimeException(String.format("无效的询问明细状态“%s”!", new Object[] { Integer.valueOf(id) }));
	}
	
	public static void main(String[] args) {
		System.out.println(AskDetailStatus.fromId(1).getDisplayName());
	}

	public int getId() {
		return this.id;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public static Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>(AskDetailStatus.values().length);
		for (AskDetailStatus c : AskDetailStatus.values()) {
			map.put(String.valueOf(c.getId()), c.getDisplayName());
		}
		return map;
	}
}
