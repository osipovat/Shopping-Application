package application;

import java.util.*;

/**
 * The Shipping Invoice class represents shoppers' shipping invoices
 * @author group_0549
 *
 */
public class ShippingInvoice {
	private int orderSequence;
	private double deliveryCost;
	private ShoppingCart shoppingCart;
	private double totalPrice;
	private double finalprice;
	private HashMap<Product, Integer> productList;
	private HashMap<Product, Double> prices;
	
	/**
	 * Initialize the shipping invoice with a given shopping cart
	 * @param shoppingCart -> the shopping cart which is connected to this invoice
	 */
	public ShippingInvoice(ShoppingCart shoppingCart){		
		this.shoppingCart = shoppingCart;
		this.productList = shoppingCart.getCart(); 
		this.prices= new HashMap<Product, Double> ();
		this.totalPrice = this.getTotalPrice();
	}
	
	/**
	 * Gets the shipping invoice's tracking number
	 * @return -> The order's tracking number
	 */
	public int getTrackNum() {
		return orderSequence;
	}
	
	/**
	 * Sets the tracking number to the orderSequence
	 * @param orderSequence -> orderSequence which keeps track of the invoices
	 */
	public void setTrackNum(int orderSequence) {
		this.orderSequence = orderSequence;
	}
	
	/**
	 * Returns the Product list
	 * @return -> the product list
	 */
	public HashMap<Product, Integer> getProductList() {
		// TODO Auto-generated method stub
		return productList;
	}
	
	/**
	 * Gets the total price of the order
	 * @return -> The total price as shown on the shipping invoice
	 */
	public double getTotalPrice() {
		this.calculateTotalPrice();
		return totalPrice;
	}
	
	/**
	 * Sets the total price of the order
	 * @param price -> The price to be set for the shipping invoice
	 */
	public void setTotalPrice(double price) {
		this.totalPrice = price;
	}

	/**
	 * Returns the final price of shipping invoice
	 * @return finalprice -> The final price of shipping invoice
	 */
	public double getFinalprice() {
		return finalprice;
	}

	/**
	 * Sets the final price of the order
	 * @return -> The final price of the order
	 */ 
	public void setFinalprice() {
		this.finalprice=totalPrice;
	}
	
	/**
	 * Returns a hashmap of Product and Double which is product that shopper bought with its price 
	 * @return -> list of products with their price 
	 */
	public HashMap<Product, Double> getPrices() {
		return prices;
	}

	/**
	 * Calculates total price of the order before shipping charges
	 */
	public void calculateTotalPrice(){
		double result = 0;
		System.out.println(this.productList);
		for (Product p : this.productList.keySet()){
			System.out.println(prices);
			prices.put(p, p.getPrice() *  this.productList.get(p));
			result += p.getPrice() * this.productList.get(p);
			
		}
		this.totalPrice = result + this.deliveryCost;
		this.setFinalprice();
	}
	
	/**
	 * Adds an invoice to customer's shipping invoice list
	 */
	public void addInvoices(){
		this.shoppingCart.getCustomer().addInvoice(this);

	}
	/**
	 * Updates the lists of shipping invoices for both shopper and admin
	 * @param p -> Product that needed to be in hashmap productList
	 * @param q -> The quantity that shopper bought
	 */
	public void updateInvoices(Product p, int q){
		this.productList.put(p, q);
	}
	
	/**
	 * Calculates and sets the cost of delivery for the order
	 * @param cost -> new delivery cost
	 */
	public void setdeliveryCost(double cost){
		this.deliveryCost =cost*0.01;
	}
	
	/**
	 * Returns the delivery cost of shopper
	 * @return -> the delivery cost of the order
	 */
	public double getdeliveryCost(){
		return this.deliveryCost;
	}
	
	/**
	 * Returns the string representation of the shipping invoice
	 * @return -> string representation of the shipping invoice
	 */
	@Override
	public String toString(){
		int counter = 0;
		StringBuilder sb = new StringBuilder();
		Set set = this.getProductList().entrySet();
		Iterator iterator = set.iterator();
		sb.append("Tracking Number: " + orderSequence +  "\n");
		while(iterator.hasNext()) {

			Map.Entry mentry = (Map.Entry)iterator.next();
			sb.append(("Product Name: "+ mentry.getKey() +"   "  +"quantity: "+ mentry.getValue()) +"  "  +"\n");
			counter ++;
		}
		sb.append("shipping cost: " + String.format("%.2f", this.getdeliveryCost()) + "\n");
		return sb.toString() + "total price is $"  + String.format("%.2f", this.getTotalPrice()); 

	}
}
