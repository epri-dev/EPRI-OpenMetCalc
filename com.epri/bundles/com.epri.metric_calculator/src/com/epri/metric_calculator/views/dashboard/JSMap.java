package com.epri.metric_calculator.views.dashboard;

import java.util.LinkedHashMap;

class JSMap extends LinkedHashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6282421663570351493L;

	private String name;

	public JSMap() {
	}

	public JSMap(String name) {
		this.name = name;
	}

	public Object put(String key, Object value, boolean isString) {
		return super.put(key, "\"" + value + "\"");
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (name != null) {
			result.append(name);
			result.append(":");
		}

		result.append("{");
		for (java.util.Map.Entry<String, Object> entry : entrySet()) {
			result.append(entry.getKey());

			result.append(":");

			result.append(entry.getValue());

			result.append(",");
		}
		result.append("}");

		return result.toString();
	}
}