package com.epri.metric_calculator.views.dashboard;

import java.util.ArrayList;

class JSArray extends ArrayList<Object> {

	private static final long serialVersionUID = 3157373284106630562L;

	private String name;
	
	public JSArray() {
	}

	public JSArray(String name) {
		this.name = name;
	}
	
	public void addString(Object element) {
		super.add("\"" + element + "\"");
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		if (name != null) {
			result.append(name);
			result.append(":");
		}
		
		result.append("[");
		for (Object item : this) {
			result.append(item);
			result.append(",");
		}
		result.append("]");
		return result.toString();
	}
}