package application;

import java.util.*;

import java.util.ArrayList;

/**
 * This method is intended to represent a single product category
 * 
 * @author group_0549
 *
 */
public class ProductCategory {
	private String description;
	private static int catCode;
	private int categoryCode = 0;
	private ArrayList<Product> products;
	
	/**
	 * This method initializes a Product Category
	 * @param description -> description of the product category converted to all capital letters
	 */
	public ProductCategory(String description) {
		this.description = description.toUpperCase();
		this.catCode++;
		this.categoryCode = catCode;
		this.products = new ArrayList<Product>();
	}
	
	/**
	 * This method adds a product with this category to its list of products
	 * @param p -> name of the product to be added
	 */
	public void addProduct(Product p){
		if(!this.products.contains(p)){
			this.products.add(p);
		}
	}

	/**
	 * This method returns the list of products with this category
	 * @return -> ArrayList of products 
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/**
	 * This method sets the list of products to the one specified
	 * @param products -> ArrayList of products 
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}	
	
	/**
	 * This method returns the category code of the product that is generated
	 * and saved every time the Product Category is initialized (using a static variable)
	 * @return -> the code for the Product Category
	 */
	public int getCategoryCode(){
		return this.categoryCode;
	}
	
	/**
	 * This method sets the category code to the one specified
	 * @param categoryCode -> code for the Product Category
	 */
	public void setCategoryCode(int categoryCode){
		this.categoryCode = categoryCode;
	}
	
	/**
	 * This method returns the written description of the Product Category
	 * @return -> description of the product category, which has been converted to all upper case letters
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Returns the string representation of the product category
	 * @return -> the string representation of the product category
	 */
	@Override
	public String toString(){
		return this.description + " (Code" + this.categoryCode + "): " + this.products;
	}
	
	/**
	 * Checks if the product category equals to another product category
	 * @return -> true if the categories are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object p){
		if (p == null) return false;
		if (this.getClass() != p.getClass()) return false;
		if (p == this) return true;
		return this.description == ((ProductCategory)p).description &&
				this.products == ((ProductCategory)p).products;
	}
}
