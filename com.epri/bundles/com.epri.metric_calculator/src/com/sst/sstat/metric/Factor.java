package com.sst.sstat.metric;

import java.io.Serializable;

public class Factor implements Serializable {
	private static final long serialVersionUID = -4504250641409871258L;

	private String name;

	private String description;

	private Float value;
	
	private boolean isMetric;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public void setValue(String value) throws NumberFormatException {
		if (value.isEmpty()) {
			this.value = Float.valueOf(0);
		} else {
			this.value = Float.valueOf(value);
		}
	}

	public boolean isMetric() {
		return isMetric;
	}

	public void setMetric(boolean isMetric) {
		this.isMetric = isMetric;
	}
}
