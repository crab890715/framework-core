package com.beacon.wechat.app.exception;

public class DatabaseExecuteFailedException extends Exception{

	private static final long serialVersionUID = 2923448617600350474L;

	public DatabaseExecuteFailedException() {
		super();
	}

	public DatabaseExecuteFailedException(String message) {
		super(message);
	}

	public DatabaseExecuteFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseExecuteFailedException(Throwable cause) {
		super(cause);
	}
}
