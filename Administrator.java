package application;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * The Administrator class represents the admin of the application
 * @author group_0549
 *
 */
public class Administrator extends User{
	private static int session;
	private int sessionID;
	private String userID;
	private String password;
	private final boolean admin = true;
	private ProductManager productMan;
	private UserManager shopMan;
	private DistributionManager distributionMan;
	private Inventory inventory;
	private ArrayList<City> city;
	private String pathname;
	
	/**
	 * Constructs an Administrator instance with the specified user ID and password
	 * @param userID -> The admin's userID
	 * @param pw -> The admin's password
	 */
	public Administrator(String userID, String pw, Inventory inventory) {
		this.userID = userID;
		this.password = pw;
		this.inventory = inventory;
		this.city = new ArrayList<City>();
		this.pathname = "src/DataFiles/";
		//this.productMan = new ProductManager(this.inventory);
		//this.shopMan = new UserManager(this.inventory);
		//this.distributionMan = new DistributionManager(this.inventory);
		session+=2;
		this.sessionID=session;
	}

	/**
	 * Gets the admin's user ID
	 * @return -> The admin's user ID
	 */
	public String getUsername() {
		return this.userID;
	}
	
	/**
	 * Sets the admin's user ID
	 * @param userID -> The user ID to be set to the admin
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	/**
	 * Gets the admin's password
	 * @return -> The admin's password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Sets the admin's password
	 * @param password -> The password to be set to the admin
	 */
	public void setUserPassword(String password) {
		this.password = password;
	}
	
	@Override
	public int logIn(String username, String pw) {
		//call log in from user
		session+=2;
		sessionID = session;
		return session;	
	}

	@Override
	public void logOut() {
		sessionID = -2;
	}
	
	/**
	 * Adds a product to the inventory
	 * @param p -> The product to be added to the inventory
	 */
	public void addProduct(Product p){
		productMan.addProduct(p, p.getDescription());
	}

	/**
	 * Changes the quantity of the specified product
	 * @param product -> The desired product to be modified
	 * @param quantity -> The quantity to be set for the product
	 */
	public void changeProductQuantity(Product product, int quantity, boolean add){
		this.productMan.changeProductQuantity(product, quantity, add);
	}
	
	/**
	 * Changes the description of the specified product
	 * @param product -> The desired product to be modified
	 * @param description -> The description to be set for the product
	 */
	public void changeProductDescription(Product product, String description){
		this.productMan.changeProductDescription(product, description);
	}
	
	/**
	 * Changes the price of the specified product.
	 * @param product -> The desired product to be modified
	 * @param price -> The price to be set for the product
	 */
	public void changeProductPrice(Product product, int price){
		this.productMan.changeProductPrice(product, price);
	}
	
	/**
	 * Changes the image of the specified product
	 * @param product -> The desired product to be modified
	 * @param image -> The image to be set for the product
	 */
	public void changeProductImage(Product product, String image){
		this.productMan.changeProductPicture(product, image);
	}
	
	/**
	 * Adds a distribution center to the list of distribution centers.
	 * @param dc -> The distribution center to be added
	 */
	public void addDistributionCenter(DistributionCenter dc){
		this.inventory.addDistributionCenter(dc);
		
	}
	
	/**
	 * Adds a new product category to the list of product categories.
	 * @param pc -> The product category to be added
	 */
	public void addProductCategory(ProductCategory pc){
		this.inventory.addProductCategory(pc);
		//should save to product category to file
	}
	
	/**
	 * Gets the admin's distribution manager
	 * @return -> The admin's distribution manager
	 */
	public DistributionManager getDistributionManager() {
		return distributionMan;
	}

	/**
	 * Gets the store's inventory
	 * @return -> The store's inventory
	 */
	public Inventory getInvenory() {
		return this.inventory;
	}
	
	/**
	 * Gets the admin's product manager
	 * @return -> The admin's product manager
	 */
	public ProductManager getProductManager(){
		return this.productMan;
	}
	
	/**
	 * Gets the list of cities in which distribution centers and shoppers reside
	 * @return -> The list of cities
	 */
	public ArrayList<City> getCity() {
		return city;
	}

	/**
	 * Adds a city to the list of cities
	 * @param city -> The city to be added to the list
	 */
	public void AddCity(String city) {
		boolean check = true;
		this.loadCityFile();
		if (this.city == null){
			City temp = new City(city, 0);
			this.city.add(temp);
			this.addCityFile(temp);
		}
		else {
			for (City c : this.city) {
				if (c.getCity().toLowerCase().equals(city.toLowerCase())){
					check = false;
				}
			}
			if (check) {
				City temp = new City(city, 0);
				this.city.add(temp);
				this.addCityFile(temp);
			}
		}
	}
	
	/**
	 * This method adds a city to the file
	 * @param c -> the name of the city to be added to the file
	 */
	public void addCityFile(City c) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(this.pathname + "City.csv", true));
			bw.write(c.getCity());
			bw.newLine();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			
	    }catch(IOException e){
	    	e.printStackTrace();
	    }finally {
	    	//close file
	    	if(bw != null) {
	    		try {
	    			bw.close();
	    		}catch (IOException e){
	    			e.printStackTrace();
	    		}
	    	}
	    }	
	}

	/**
	 * This method loads the city file
	 */
	public void loadCityFile(){		
		Path filename = FileSystems.getDefault().getPath(pathname+"City.csv");
		List<String> fileContent;
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			for (int i = 0; i<fileContent.size(); i++) {
				this.city.add(new City(fileContent.get(i), 0));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	/**
	 * This method returns the administrator's sessionID
	 */
	public int getSessionID() {
		return sessionID;
	}
	
	@Override
	public String toString(){
		return this.userID;
	}
	
}
