package application;

/**
 * Thrown when application attempts to manipulate an empty heap
 * @author group_0549
 *
 */
public class HeapEmptyException extends Exception {
	
	/**
	 * Constructs a HeapEmptyException with no detail message
	 */
	public HeapEmptyException() {
		super("Heap is empty!");
	}
	
	/**
	 * Constructs a HeapEmptyException with the specified detail message
	 * @param message -> The desired message
	 */
	public HeapEmptyException(String message) {
		super(message);
	}

}