package io.notifye.botengine.exception;

public class QueryExecutionBotException extends BotException {
	private static final long serialVersionUID = 1L;

	public QueryExecutionBotException() {
		super();
	}

	public QueryExecutionBotException(String message, Throwable throwable, boolean arg2, boolean arg3) {
		super(message, throwable, arg2, arg3);
	}

	public QueryExecutionBotException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public QueryExecutionBotException(String message) {
		super(message);
	}

	public QueryExecutionBotException(Throwable throwable) {
		super(throwable);
	}
	
	

}
