package application;

/**
 * Thrown when application attempts to add a product that is out of stock to a shopping cart
 *
 */
public class OutOfStockException extends Exception {
	
	/**
	 * Constructs an OutOfStockException with no detail message
	 */
	public OutOfStockException(){
		super();
	}

	/**
	 * Constructs an OutOfStockException with the specified detail message
	 * @param message
	 */
	public OutOfStockException(String message){
		super(message);
	}
}
