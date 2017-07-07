 package application;

import java.util.HashMap;

/**
 * This class is intended to represent a single product
 * @author group_0549
 *
 */
public class Product implements Comparable<Product>{
	private static int IDNum;
	private int ID;
	private String image;
	private String description;
	private ProductCategory category;
	private double price;
	private int quantity;
	private int reservedQuantity;
	
	/**
	 * This method initializes a Product and adds itself to the list of products in its ProductCategory
	 * @param image -> the reference string of the image
	 * @param description -> written description of the product
	 * @param category -> the Product Category the product is in
	 * @param price 
	 * @param quantity -> the total amount of stock of the product
	 */
	public Product(String image, String description, ProductCategory category, double price, int quantity){
		this.image = image;
		this.IDNum++;
		this.ID = IDNum;
		this.description = description;
		this.category = category;
		category.addProduct(this);
		this.price = price;
		this.quantity = quantity;
		this.reservedQuantity = 0;
	}
	
	/**
	 * This method is another constructor that initializes the Product, without an image
	 * @param description -> written description of the product
	 * @param category -> the Product Category that the product is in
	 * @param price
	 * @param quantity -> total amount of stock of the product
	 */
	public Product(String description, ProductCategory category, double price, int quantity){
		this.description = description;
		this.price = price;
		this.category = category;
		this.category.addProduct(this);
		this.quantity = quantity;
		this.IDNum++;
		this.ID = IDNum;
		this.reservedQuantity = 0;
	}
	
	/**
	 * This method returns the Product ID
	 * @return -> ID of the product
	 */
	public int getID(){
		return this.ID;
	}
	
	/**
	 * This method returns the quantity of the product that is reserved
	 * because it is in a shopper's cart
	 * @return -> quantity of the product that is reserved
	 */
	public int getReservedQuantity() {
		return reservedQuantity;
	}

	/**
	 * This method sets the reserved quantity to the one specified
	 * @param reservedQuantity -> the amount that is reserved
	 */
	public void setReservedQuantity(int reservedQuantity) {
		this.reservedQuantity = reservedQuantity;
	}

	/**
	 * This method returns the description of the product
	 * @return -> the written description of the product
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * This method returns the Product Category
	 * @return -> Product Category that the product is in
	 */
	public ProductCategory getCategory() {
		return this.category;
	}
	
	/**
	 * This method returns the price of the product
	 * @return -> double that represents the price  
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * This method returns the quantity of the product
	 * @return -> the total amount of product that is in stock
	 */
	public int getQuantity(){
		return this.quantity;
	}

	/**
	 * This method sets the image of the product to the one specified
	 * @param image -> string (reference) of the image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * This method sets the description of the product to the one specified
	 * @param description -> (string) description of the product
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This method sets the price of the product to the one specified
	 * @param price -> (double) price of the product
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * This method sets the total quantity of the product to the one specified
	 * @param quantity -> total amount of the product in stock/available
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * This method adds quantity to the existing product
	 * @param quantity -> new amount needed to add
	 */
	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}
	
	/**
	 * Compares one product to another by availability
	 * @param other -> product
	 * @return -> -1 if this product's quantity is less than the other product's quantity,
	 * 1 if this product's quantity is more than the other product's quantity
	 * 0 if the products are equal.
	 */
	public int compareTo(Product other){
		if (this.quantity - other.quantity < 0)
			 return -1;
		 if (this.quantity - other.quantity > 0)
			 return 1;
		
		return 0; 
	}
	
	/**
	 * Checks if this product is equal to another product
	 * @param p -> product
	 * @return -> true if the products are equal, flse otherwise.
	 */
	@Override
	public boolean equals(Object p){
		if (p == null) return false;
		if (this.getClass() != p.getClass()) return false;
		if (p == this) return true;
		return this.description == ((Product)p).description &&
				this.price == ((Product)p).price &&
				this.category == ((Product)p).category;
	}
	
	/**
	 * Returns the string representation of the product
	 * @return -> the string representation of the product
	 */
	@Override
	public String toString(){
		String category = this.category.getDescription();
		return this.description + "," + String.format("%.2f", this.price) + "," + this.quantity + "," + category + "," + this.ID;
	}
	
	
}