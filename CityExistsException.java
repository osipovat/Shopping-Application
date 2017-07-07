package application;

/**
 * This class is thrown when application attempts to add a City instance that already exists in the system
 * @author group_0549
 *
 */
public class CityExistsException extends Exception{

		/**
		 * Constructs a CityExistsException with no detail message 
		 */
		public CityExistsException() {
			super("The vertex alredy exist");
		}
		
		/**
		 * Constructs a CityExistsException with the specified detail message
		 * @param message
		 */
		public CityExistsException(String message) {
			super(message);
		}
}