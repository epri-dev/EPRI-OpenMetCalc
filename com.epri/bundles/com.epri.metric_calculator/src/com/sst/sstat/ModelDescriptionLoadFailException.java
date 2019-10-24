package com.sst.sstat;

public class ModelDescriptionLoadFailException extends Exception {

	private static final long serialVersionUID = 178012554225623865L;

	public ModelDescriptionLoadFailException() {
		super("Cannot load model descriptions.");
	}

	public ModelDescriptionLoadFailException(String message) {
		super(message);
	}
}
