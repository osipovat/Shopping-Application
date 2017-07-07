package application;
import java.util.*;


public class ShoppingCart{

	private final static int LIMIT = 20;
	private static int orderSequence;
	private static int newOrderSequence;
	private Shopper shopper;
	private double total;
	private HashMap<Product, Integer> cart;
	 
	/**
	 * Initialize the shopping cart given shopper
	 * @param shopper -> shopper which is connected to this shopping cart
	 */
	public ShoppingCart(Shopper shopper){
		this.shopper = shopper;
		this.cart = new HashMap<Product, Integer>();
		total = 0;
	}
	
	/**
	 * This method returns the total price in shopping cart
	 * @return -> The total price of this shopping cart
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * This method sets the total price in shopping cart
	 * @param total -> new total amount of shopping cart 
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * Returns the shopping cart
	 * @return -> HashMap where the key is a product and the value is the quantity that shopper added to the cart
	 */
	public HashMap<Product, Integer> getCart() {
		return this.cart;
	}

	/**
	 * Returns the shopper connected to this shopping cart
	 * @return -> shopper
	 */
	public Shopper getShopper() {
		return shopper;
	}
	
	/**
	 * Returns the customer that is connected to this shopping cart
	 * @return -> customer
	 */
	public Customer getCustomer() {
		
		return shopper.getCustomer();
	}
	
	/**
	 * Adds product with a given quantity to a shopping cart
	 * @param product -> product that we want to add to the shopping cart
	 * @param quantity -> the quantity of product that we want to add to the shopping cart
	 */
	public void add(Product product, int quantity) throws OutOfStockException {	
		if (product.getQuantity() - product.getReservedQuantity() >= quantity){
			this.cart.put(product, quantity);
			total+=product.getPrice()*quantity;
			this.persistQuantity(product, quantity);
		}
		else {
			throw new OutOfStockException("Oops this product is out of stock. Check back later.");
		}
	}
	
	/**
	 * Changes the quantity in the shopping cart of a given product to newQuantity
	 * @param product -> product that we want to modify
	 * @param qnewQuantity -> the new quantity that we want to set of a given product 
	 */
	public void changeQuantity(Product product, int newQuantity) {
		product.setReservedQuantity(product.getReservedQuantity() - this.cart.get(product));
		total-=product.getPrice()*this.cart.get(product);
		this.cart.put(product, newQuantity);
		total+=product.getPrice()*newQuantity;
		product.setReservedQuantity(product.getReservedQuantity() + this.cart.get(product));
	}
	
	/**
	 * Remove product from a shopping cart
	 * @param product -> product that we want to remove from the shopping cart
	 */
	public void remove(Product product){
		//product removed from cart
		if (this.cart.containsKey(product)){
			total-=product.getPrice()*this.cart.get(product);
			product.setReservedQuantity(product.getReservedQuantity() - this.cart.get(product));
			this.cart.remove(product);
		}
	}
	
	/**
	 * Quantity of a product become available to other shoppers
	 * @param product -> product for which we modify reserved quantity
	 */
	public void publicQuantity(Product product){
		//when logs out quantity becomes available to others
		product.setReservedQuantity(product.getReservedQuantity() - this.cart.get(product));
	}
	
	/**
	 * Quantity of a product is reserved for this shopping cart
	 * @param product -> product for which we modify the reserved quantity
	 * @param quantity -> quantity we want to reserve
	 */
	public void persistQuantity(Product product, int quantity) {
		//during the session, a quantity is not available for others
		product.setReservedQuantity(product.getReservedQuantity() + quantity);
	}
	
	/**
	 * Review if the quantity is available in the inventory,
	 * if not then say that the item is out of stock
	 * @param product -> product for which we check the availability
	 */
	public void reviewQuantity(Product product) throws OutOfStockException{
		//when logs back in review the quantity in the cart
		//if the quantity is not available say that it is out of stock
		if (product.getQuantity() < cart.get(product)) {
			this.remove(product);
			throw new OutOfStockException("Oops the product was sold out");
		}
		else {
			this.persistQuantity(product, cart.get(product));
		}
	}
	
	/**
	 * The customer with customerID places order for a single product with quantity with the sessionID which belonged to the authentically user
	  * @param customerID -> The customer ID
	 * @param product -> The product 
	 * @param quantity -> The desired quantity
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The orderSequence if successful.
	 */
	public int checkOut(){
		newOrderSequence++;
		ShippingInvoice invoice = new ShippingInvoice(this);
		invoice.setTrackNum(newOrderSequence);
		for (Product p: this.cart.keySet()) {
			invoice.updateInvoices(p, this.cart.get(p));
		}
		invoice.addInvoices();
		return newOrderSequence;
	}
	
	/**
	 * The customer with customerID places order for a single product with quantity with the sessionID which belonged to the authentically user
	  * @param customerID -> The customer ID
	 * @param product -> The product 
	 * @param quantity -> The desired quantity
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The orderSequence if successful.
	 */
	public int placeOrder(int customerID, Product product, int quantity, int sessionID){
		orderSequence++;
		ShippingInvoice ship = new ShippingInvoice(this);
		
		ship.setTrackNum(orderSequence);
		
		ship.updateInvoices(product, quantity);
		ship.addInvoices();
		total = 0;

		return ship.getTrackNum();
	}
	
	/**
	 * Gives a string representation of the shopping cart
	 * @return -> the string representation of shopping cart
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		 Set set = cart.entrySet();
		 Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         sb.append((mentry.getKey() +" "+"customer ordered "+ mentry.getValue()) +"\n            ");
	      }
		
		return sb.toString() + "total price is $" + String.format("%.2f", total) +"\n";
	}
		
}
