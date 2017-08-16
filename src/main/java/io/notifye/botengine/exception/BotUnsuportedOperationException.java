package io.notifye.botengine.exception;

public class BotUnsuportedOperationException extends BotException {
	private static final long serialVersionUID = 1L;

	public BotUnsuportedOperationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BotUnsuportedOperationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public BotUnsuportedOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BotUnsuportedOperationException(String arg0) {
		super(arg0);
	}

	public BotUnsuportedOperationException(Throwable arg0) {
		super(arg0);
	}
	
}
