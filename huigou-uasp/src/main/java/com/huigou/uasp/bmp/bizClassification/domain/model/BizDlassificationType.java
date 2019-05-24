package com.huigou.uasp.bmp.bizClassification.domain.model;

import java.util.HashMap;
import java.util.Map;

public enum BizDlassificationType {
	BIZ_TABLE("bizTable", "业务表"), BIZ_FIELD_GROUP("fieldGroup", "字段分组");

	private String id;
	private String displayName;

	private BizDlassificationType(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public static BizDlassificationType fromId(String id) {
		switch (id) {
		case "bizTable":
			return BIZ_TABLE;
		case "fieldGroup":
			return BIZ_FIELD_GROUP;
		}
		throw new RuntimeException(String.format("无效的业务类型“%s”。", new Object[] { Integer.valueOf(id) }));
	}

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public static Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>(BizDlassificationType.values().length);
		for (BizDlassificationType c : BizDlassificationType.values()) {
			map.put(String.valueOf(c.getId()), c.getDisplayName());
		}
		return map;

	}

}