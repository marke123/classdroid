package com.mclear.classdroid.exceptions;

public class ClassdroidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2712983359102325419L;

	public ClassdroidException() {
		super();
	}

	public ClassdroidException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ClassdroidException(String detailMessage) {
		super(detailMessage);
	}

	public ClassdroidException(Throwable throwable) {
		super(throwable);
	}

}
