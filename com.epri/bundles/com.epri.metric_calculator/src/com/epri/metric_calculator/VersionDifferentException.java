package com.epri.metric_calculator;

public class VersionDifferentException extends Exception {

	private static final long serialVersionUID = -1772453403680049817L;

	public VersionDifferentException(String version) {
		super("This project was created in a different version(" + version
				+ "). Project created in other version can not be opened.");
	}
}
