package application;

import java.util.HashMap;

/**
 * This class represents a single distribution center that contains products
 * @author group_0549
 *
 */
public class DistributionCenter {
	private String city;
	private HashMap<Product, Integer> quantity;
	private Inventory inventory;
	private City cityO;

	/**
	 * This method constructs a new Distribution Center with the specified city and Inventory
	 * @param city -> name of the city that the Distribution Center is based in
	 * @param inventory -> the name of the inventory
	 */
	public DistributionCenter(String city, Inventory inventory){
		this.city = city;
		this.quantity = new HashMap<Product, Integer>();
		this.inventory = inventory;
	}

	/**
	 * This method adds a product with the specified quantity
	 * @param p -> name of the product to be added
	 * @param q -> the quantity of that product to be added
	 */
	public void addProducts(Product p, int q){
		if(this.inventory.getProductList() != null) 
			quantity.put(p, q);
	}
	
	/**
	 * This method returns the name of the city that the Distribution Center is based in
	 * @return -> the name of the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * This method sets the name of the city to the one specified
	 * @param city -> the name of the city that it is set to
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * This method sets the City variable to the one specified
	 * @param city -> name of the City variable that city0 is set to
	 */
	public void setCityO(City city) {
		this.cityO = city;
	}
	
	/**
	 * This method returns the HashMap of the products and their quantities present in the center
	 * @return -> HashMap of the Products and their quantities in the center
	 */
	public HashMap<Product, Integer> getQuantity() {
		return quantity;
	}

	/**
	 * This method places the product and its' quantity into the HashMap of product stock
	 * @param product -> the name of the Product to be added
	 * @param newQuantity -> amount of that product to be added to the stock
	 */
	public void setQuantity(Product product, int newQuantity) {
		quantity.put(product, newQuantity);
	}
	
	/**
	 * This method sorts the quantity by sessionID
	 */
	public void sortQuantity(){
		for (int i = 0; i < this.quantity.size(); i++){
			
		}
	}
	
	/**
	 * This method adds the Product specified and gives it a quantity of 0
	 * @param p -> Product to be added to the center
	 */
	protected void addProduct(Product p) {
		this.quantity.put(p, 0);
	}
	
	/**
	 * This method returns a string representation of the Distribution Center
	 * @return -> the name of the city that the center is based in and the stock of all its products
	 */
	@Override
	public String toString(){
		return this.city + this.quantity;
	}
	
}
