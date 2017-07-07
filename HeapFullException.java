package application;
/**
 * Thrown when application attempts to manipulate a full heap
 * @author group_0549
 */

public class HeapFullException extends Exception {
	
	/**
	 * Constructs a HeapFullException with no detail message
	 */
	public HeapFullException() {
		super("Heap is full!");
	}
	
	/**
	 * Constructs a HeapFullException with the specified detail message
	 * @param message
	 */
	public HeapFullException(String message) {
		super(message);
	}

}