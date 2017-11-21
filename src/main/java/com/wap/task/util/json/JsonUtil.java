package com.wap.task.util.json;

import java.util.List;

public class JsonUtil {

	public static String getJsonArray(String name, List<String> values) {
		String jsonArray = "\"" + name + "\"" + ":" + "[";

		for (int i = 0; i < values.size(); i++) {
			jsonArray += "\"" + values.get(i) + "\"";
			if (i < values.size() - 1) {
				jsonArray += ",";
			}
		}
		
		return jsonArray + "]";
	}
}
