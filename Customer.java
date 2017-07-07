package application;

import java.util.ArrayList;

/**
 * The Customer class represents a Shopper that makes a purchase
 * @author group_0549
 *
 */
public class Customer extends Shopper{

	private int sessionID;
	private int customerID; 
	private String userID;
	private String pw;
	protected Inventory inventory;
	private String firstName;
	private String lastName;
	private String city;
	private String street;
	private String email;
	private String creditCardNumber;
	private String creditCardExpiryDate;
	private String phoneNumber;
	public ArrayList<ShippingInvoice> invoices; 
	private ShippingInvoice invoice;
	private ShoppingCart shoppingCart; 
	
	/**
	 * Constructs a Customer instance with the specified ID, password, name, address,
	 * email, phone number, and credit card info
	 * @param userID -> The customer's username
	 * @param pw -> The customer's password
	 * @param firstN -> The customer's first name
	 * @param lastN -> The customer's last name
	 * @param street -> The customer's street number and name
	 * @param city -> The customer's city
	 * @param email -> The customer's email
	 * @param phoneNumber -> The customer's phone number
	 * @param inventory -> The inventory from which customer is purchasing
	 * @param creditCardNumber -> The customer's credit card number
	 * @param creditCardExpiryDate -> The customer's credit card's expiration date
	 */
	public Customer(String userID, String pw, String firstN, String lastN, String street, String city, String email, String phoneNumber, String creditCardNumber, String creditCardExpiryDate) {
		super(userID, pw);	// shopping cart made in super class
		this.invoices = new ArrayList<ShippingInvoice>();
		this.firstName = firstN;
		this.lastName = lastN;	
		this.street = street;
		this.city = city;	
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.inventory = super.inventory;
		this.creditCardExpiryDate = creditCardExpiryDate;
		this.creditCardNumber = creditCardNumber;
		this.sessionID = this.getSessionID();
	}
	
	/**
	 * This alternate constructor, constructs a Customer instance with the specified  userID, password, customerName, city, and street
	 * @param userID -> the customer's user name
	 * @param pw -> the customer's password
	 * @param custName -> the customer's name
	 * @param city -> the customer's city
	 * @param street -> the customer's street
	 */
	public Customer(String userID, String pw, String custName, String city, String street) {
		
		super(userID, pw);
		this.shoppingCart = super.getShoppingCart();
		this.inventory=super.inventory;
		this.invoices = new ArrayList<ShippingInvoice>();
		this.firstName = custName;
		this.city = city;
		this.street = street;
		this.sessionID = this.getSessionID();
	}
	
	/**
	 * Gets the customer's userID
	 * @return -> The customer's userID
	 */
	protected String getUserID() {
		return userID;
	}
	
	/**
	 * This method returns the customer's customerID
	 * @return -> customerID
	 */
	protected int getCustomerID() {
		customerID = super.getShopperID();
		return customerID;
	}

	/**
	 * Sets the customer's customerID
	 * @param id -> The ID that will be set to the customer
	 */
	protected void setCustomerID(int id) {
		this.customerID=id;
	}
	
	/**
	 * This method sets the customer's user name to the one specified
	 * @param userID -> the new user name that the customer's userID is set to
	 */
	protected void setUsername(String userID) {
		this.userID = userID;
	}

	/**
	 * Gets the customer's password
	 * @return -> The customer's password
	 */
	protected String getPw() {
		return pw;
	}

	/**
	 * Sets the customer's password
	 * @param pw -> The password that will be set for the customer
	 */
	protected void setPw(String pw) {
		this.pw = pw;
	}

	/**
	 * Gets the customer's first name
	 * @return -> The customer's first name
	 */
	protected String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the customer's first name
	 * @param firstName -> The first name to be set for customer
	 */
	protected void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	

	/**
	 * Gets the customer's street number and name
	 * @return -> The customer's street number and name
	 */
	protected String getStreet() {
		return street;
	}

	/**
	 * Sets the customer's street number and name
	 * @param street -> The street number and name to be set for the customer
	 */
	protected void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Sets the customer's city
	 * @param city -> The city to be set for the customer
	 */
	protected void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the customer's email
	 * @return -> The customer's email
	 */
	protected String getEmail() {
		return email;
	}

	/**
	 * Sets the customer's email
	 * @param email -> The email to be set for the customer
	 */
	protected void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the customer's credit card number
	 * @return -> The customer's credit card number
	 */
	protected String getCreditCardNumber() {
		return creditCardNumber;
	}

	/**
	 * Sets the customer's credit card number
	 * @param creditCardNumber -> The credit card number to be set for the customer
	 */
	protected void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * Gets the customer's credit card's expiration date
	 * @return -> The customer's credit card's expiration date
	 */
	protected String getCreditCardExpiryDate() {
		return creditCardExpiryDate;
	}

	/**
	 * This method sets the credit card expire date to the one specified
	 * @param creditCardExpiryDate -> The new credit card expiry date to be set for the customer
	 */
	protected void setCreditCardExpiryDate(String creditCardExpiryDate) {
		this.creditCardExpiryDate = creditCardExpiryDate;
	}

	/**
	 * This method returns the customer's phone number
	 * @return -> the phone number of the customers
	 */
	protected String getPhoneNumber() {
		return phoneNumber;
	}

	public void setCart(ShoppingCart sc){
		this.shoppingCart = sc;
	}
	
	/**
	 * This method sets the phoneNumber for the customer to the one specified
	 * @param phoneNumber -> the number that the customer's phone number is set to
	 */
	protected void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the list of the customer's shipping invoices
	 * @return -> The list of the customer's shipping invoices
	 */
	public ArrayList<ShippingInvoice> getInvoices() {
		return invoices;
	}

	/**
	 * This method returns the customer's shopping cart
	 */
	public ShoppingCart getShoppingCart() {
		return super.getShoppingCart();
	}
	
	/**
	 * Gets the customer's city
	 * @return -> The customer's city
	 */
	public String getCity(){
		return city;
	}

	/**
	 * Adds the specified product with the specified quantity to the customer's
	 * shopping cart
	 * @param p -> The product to be added to the shopping cart
	 * @param quantity -> The quantity of the product to be added to the shopping cart
	 */
	public void addToCart(Product p, int quantity) throws OutOfStockException{
		this.shoppingCart.add(p, quantity);
	}
	
	/**
	 * Adds the specified shipping invoice to the customer's list of shipping invoices
	 * @param shippingInvoice -> The shipping invoice to be added to the customer's list
	 * of shipping invoices
	 */
	public void addInvoice(ShippingInvoice shippingInvoice) {
		if (this.invoices.size() > 0) {
			if(!this.invoices.contains(shippingInvoice)){
				this.invoices.add(shippingInvoice);
			}
		}
		else {
			this.invoices.add(shippingInvoice);
		}
	}
	
	/**
	 * Returns a string representation of the Customer object.
	 * @return -> The customer's name, shopping cart and list of invoices
	 */
	@Override
	public String toString() {
		return firstName  + " Cart: " + this.shoppingCart + "Invoices: " + invoices.toString();  	
	}
	
	/**
	 * This method check whether this Customer equivalent to another object p
	 * @param p
	 * @return -> true if this Customer is equal to the given object 
	 */
	@Override 
	public boolean equals(Object p){
		if (p == null) return false;
		if (this.getClass() != p.getClass()) return false;
		if (p == this) return true;
		Customer c = (Customer) p;
		return this.firstName.equals(c.firstName) &&
				this.street.equals(c.street) &&
				this.city.equals(c.city) &&
				this.email.equals(c.email) &&
				this.creditCardNumber.equals(c.creditCardNumber) &&
				this.invoices.equals(c.invoices) &&
				this.shoppingCart.equals(c.shoppingCart);
	}
}