package com.sst.sstat.metric;

public class MetricSaveFailException extends Exception {

	private static final long serialVersionUID = -790506825309936206L;

	public MetricSaveFailException(String message) {
		super(message);
	}

	public MetricSaveFailException(String message, Throwable cause) {
		super(message, cause);
	}

}
