package application;

/**
 * This method is thrown when application attempts to add another Product to a ShoppingCart that is full
 * 
 * @author group_0549
 *
 */

public class CartFullException extends Exception{
	
	/**
	 * Constructs a CartFullException with no detail message
	 */
	public CartFullException(){
		super();
	}

	/**
	 * Constructs a CartFullException with the specified detail message
	 * @param message
	 */
	public CartFullException(String message){
		super(message);
	}
}