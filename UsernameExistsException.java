package application;

/**
 * Thrown when application attempts to create a user with a username that already exists
 * @author group_0549
 *
 */
public class UsernameExistsException extends Exception{
	
	/**
	 * Constructs a UsernameExistsException with no detail message
	 */
	public UsernameExistsException(){
		super();
	}

	/**
	 * Constructs a UsernameExistsException with the specified detail message
	 * @param message -> The desired message
	 */
	public UsernameExistsException(String message){
		super(message);
	}
}
