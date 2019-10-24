package com.epri.metric_calculator;

public class ProjectFileSaveFailException extends Exception {

	private static final long serialVersionUID = 1433951657831126899L;

	public ProjectFileSaveFailException(String filePath) {
		super("Cannot save the project to " + filePath);
	}

}
