package io.notifye.botengine.client.exception;

public class BotException extends Exception {
	private static final long serialVersionUID = 1L;

	public BotException() {
		super();
	}

	public BotException(String message, Throwable throwable, boolean arg2, boolean arg3) {
		super(message, throwable, arg2, arg3);
	}

	public BotException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BotException(String message) {
		super(message);
	}

	public BotException(Throwable throwable) {
		super(throwable);
	}
	
	

}
