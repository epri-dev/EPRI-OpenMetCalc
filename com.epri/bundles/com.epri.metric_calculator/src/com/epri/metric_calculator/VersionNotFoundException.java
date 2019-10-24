package com.epri.metric_calculator;

public class VersionNotFoundException extends Exception {

	private static final long serialVersionUID = 373166191209302459L;
	private static final String message = "Unable to determine the version. Please check the configuration file.";

	public VersionNotFoundException() {
		super(message);
	}
}