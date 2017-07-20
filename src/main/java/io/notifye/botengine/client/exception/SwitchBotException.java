package io.notifye.botengine.client.exception;

public class SwitchBotException extends BotException {
	private static final long serialVersionUID = 1L;

	public SwitchBotException() {
		super();
	}

	public SwitchBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SwitchBotException(String message, Throwable cause) {
		super(message, cause);
	}

	public SwitchBotException(String message) {
		super(message);
	}

	public SwitchBotException(Throwable cause) {
		super(cause);
	}
	
}
