package application;

public class RouteFullException extends Exception {
	/**
	 * Constructs a RouteFullException with no detail message
	 */
	public RouteFullException() {
		super("The grpah is full");
	}
	
	/**
	 * Constructs a RouteFullException with the specified detail message
	 * @param message
	 */
	public RouteFullException(String message) {
		super(message);
	}
}
