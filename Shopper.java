package application;

/**
 * The Shopper class represents the shoppers on the application
 * @author group_0549
 *
 */
public class Shopper extends User{
	private static int session = 1;
	private String username;
	private String pw;
	private int sessionID;
	private final boolean admin = false;
	private static int shopperSequence=1;
	private int shopperID;
	protected Inventory inventory;
	private ShoppingCart shoppingCart;
	private Customer customer;
	
	/**
	 * Constructs a Shopper instance with the specified username and password
	 * @param username -> The shopper's username
	 * @param pw -> The shopper's password
	 */
	public Shopper(String username, String pw) {
		this.username = username;
		this.pw = pw;
		shoppingCart = new ShoppingCart(this);
		shopperSequence+=2;
		shopperID = shopperSequence; 	
	}

	/**
	 * Sets the shopper as a customer (i.e., a shopper who has made a purchase)
	 * @param customer -> The Customer instance to be set to the shopper
	 */
	public void setCustomer(Customer customer){
		this.customer = customer;
	}
	
	/**
	 * Sets new inventory
	 * @param inventory -> Inventory object
	 */
	public void setInventory( Inventory inventory){
		this.inventory = inventory;
	}
	
	/**
	 * Gets the shopper's shopping cart
	 * @return -> The shopper's shopping cart
	 */
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}
	
	/**
	 * Gets the shopper's shopper ID
	 * @return -> The shopper's shopper ID
	 */
	public int getShopperID() {
		return shopperID;
	}

	/**
	 * Sets the shopper's shopper ID
	 * @param shopperID -> The shopper ID to be set to the shopper
	 */
	public void setShopperID(int shopperID) {
		this.shopperID = shopperID;
	}

	/**
	 * Return the shopper's username
	 * @return -> username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the shopper's username to the username
	 * @param username -> The shopper's username
	 */
	protected void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the shopper's password
	 * @return -> password of shopper
	 */
	public String getPassword() {
		return this.pw;
	}

	/**
	 * Sets the shopper's password to the pw
	 * @param pw -> The shopper's password
	 */
	protected void setPassword(String pw) {
		this.pw = pw;
	}
	
	/**
	 * Returns the shopper's sessionID
	 * @return -> the shopper's sessionID
	 */
	public int getSessionID() {
		return this.sessionID;
	}
	
	/**
	 * Gets the shopper's Customer instance
	 * @return -> The shopper's Customer instance
	 */
	public Customer getCustomer() {
		
		return this.customer;
	}
	

	/**
	 * Adds the specified product and quantity to the shopper's shopping cart
	 * @param p -> The product to be added to the shopper's shopping cart
	 * @param quantity -> The quantity of the product to be added to the shopper's
	 * shopping cart
	 * @throws OutOfStockException if the product is out of stock
	 */
	public void addToCart(Product p, int quantity) throws OutOfStockException{
		this.shoppingCart.add(p, quantity);
	}

	/**
	 * Empties the shopper's shopping cart and makes it available to other shoppers
	 */
	public void cancelCart(){
		this.shoppingCart = new ShoppingCart(this);
		for (Product product : this.shoppingCart.getCart().keySet()){
			product.setReservedQuantity(product.getReservedQuantity() - this.shoppingCart.getCart().get(product));
		}
	}
	
	/**
	 * Returns the string representation of the shopper
	 * @return -> the string representation of shopper
	 */
	@Override
	public String toString() {
		return username + " Cart: " + this.shoppingCart;// + "Invoices: " invoices.toString();  	
	}
	
	/**
	 * Checks if the shopper is equal to another shopper
	 * @param p -> another shopper
	 * @return -> true if the shopper is equal to another shopper and false otherwise.
	 */
	@Override 
	public boolean equals(Object p){
		if (p == null) return false;
		if (this.getClass() != p.getClass()) return false;
		if (p == this) return true;
		Shopper s = (Shopper) p;
		return this.username.equals(s.username) && this.pw.equals(s.pw);
	}

	/**
	 * Logs in the shopper
	 * @param username -> the shopper's username
	 * @param password -> the shopper's password
	 * @return -> the sessionID of shopper if login is successful, -1 otherwise.
	 */
	@Override
	public int logIn(String username, String password) {
		//call user login
		session+=2;
		sessionID = session;
		// load file 
		//inventory.getShopperManager().loadFile(this.username); // if login successes, need to load file for that user
		
		//this.inventory.getFile(this); this file does not exist at this point 
		for (Product product : this.shoppingCart.getCart().keySet()){
			try {
				this.shoppingCart.reviewQuantity(product);
			} catch (OutOfStockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sessionID;
	}
	
	/**
	 * The shopper logs out
	 */
	@Override
	public void logOut() {
		//this.saveCart();
		sessionID = -1;//1
		//resets the reserved quantity of product so it becomes available to others
		for (Product product : this.shoppingCart.getCart().keySet()){
			this.shoppingCart.publicQuantity(product);
		}
		
	}
}
