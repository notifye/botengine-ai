/**
 * 
 */
package io.notifye.botengine.exception;

/**
 * @author Adriano Santos
 *
 */
public class EntityParserException extends Exception {
	private static final long serialVersionUID = 1L;

	public EntityParserException() {
		super();
	}

	public EntityParserException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public EntityParserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EntityParserException(String arg0) {
		super(arg0);
	}

	public EntityParserException(Throwable arg0) {
		super(arg0);
	}
	
	

}
