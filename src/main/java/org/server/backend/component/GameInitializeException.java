package org.server.backend.component;

/**
 * 游戏初始化异常
 * 
 * @author Hxms
 *
 */
public class GameInitializeException extends RuntimeException {

	private static final long serialVersionUID = -8473766136039915395L;

	/**
	 * {@inheritDoc}
	 * 
	 * @param message
	 * @param cause
	 */
	public GameInitializeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param message
	 */
	public GameInitializeException(String message) {
		super(message);
	}

}
