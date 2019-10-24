package com.sst.sstat.metric;

public abstract class AbMetricTraceRunnable implements IMetricRunnable {

	private int traceLevel;

	public int getTraceLevel() {
		return traceLevel;
	}

	void setTraceLevel(int level) {
		this.traceLevel = level;
	}
}
