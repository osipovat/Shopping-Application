package application;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is intended to manage Products and Product Categories 
 * and is accessed by Administrator and Inventory
 * 
 * @author group_0549
 *
 */
public class ProductManager{
	private HashMap<ProductCategory, ArrayList<Product>> categoryProductList;
	private ArrayList<Product> productList;
	private ArrayList<ProductCategory> categoryList;
	private String filename;
	private ArrayList<DistributionCenter> distributionCenters;
	private Inventory inventory;
	private HashMap<Product, Integer> sales;
	
	/**
	 * This method initializes a new ProductManager and loads the file of products
	 * @param inventory
	 */
	public ProductManager(Inventory inventory){
		this.inventory = inventory;
		this.categoryProductList = new HashMap<ProductCategory, ArrayList<Product>>();
		this.categoryList = new ArrayList<ProductCategory>();
		this.productList = new ArrayList<Product>();
		this.sales = new HashMap<Product, Integer>();
		this.filename = "src/DataFiles/Products.csv";
	}
	
	/**
	 * This method adds a product to the list of products and the product file
	 * It also adds the product with 0 quantity to all the distribution centers
	 * @param p -> the name of the product to be added
	 */
	public void addProduct(Product p, String name){ 
		//add product to product list 
		boolean check = true;
		for (Product pr : this.productList)
			if (pr.getDescription().equals(name))
				check = false;
		if (check) {
			this.productList.add(p);
			for(DistributionCenter d : this.inventory.getDistributionCenters() ){
				d.addProduct(p);
			}
			this.saveFile(p.toString()); //add product to file
		}
	}
	
	/**
	 * This method adds a product to every distribution center and sets the quantity to 0 
	 * @param p -> the name of product to be added
	 */
	public void addtoDistributionCenters(Product p){
		for(DistributionCenter dc : this.inventory.getDistributionCenters()){
			dc.addProduct(p);
			this.inventory.addProductDistributionCenter(p);
		}
	}
	
	/**
	 * This method adds a product category to the list of product category
	 * @param pc -> the name of product category to be added
	 */
	public void addProductCategory(ProductCategory pcat){
		if(!this.categoryList.contains(pcat)){
			this.categoryList.add(pcat);
			//add product category to file
			this.addCategoryToFile(pcat); 
		}
	}

	/**
	 * This method returns all the product categories in a list
	 * @return -> ArrayList of all the product categories
	 */
	public ArrayList<ProductCategory> getCategoryList(){
		return this.categoryList;
	}
	
	/**
	 * This method returns all the products in a list
	 * @return -> ArrayList of all products
	 */
	public ArrayList<Product> getProductList(){
		return this.productList;
	}
	
	/**
	 * This method returns a HashMap of all the Product Categories as keys and an ArrayList 
	 * of all their products as the value
	 * @return -> HashMap of Product Categories and the ArrayList of Products in each category
	 */
	public HashMap<ProductCategory, ArrayList<Product>> getCategoryProductList(){
		this.categoryProductList();
		return this.categoryProductList;
	}
	
	/**
	 * This method creates the Category and Product HashMap by iterating through the list of 
	 * product categories and setting them and their value as the product list in each category 
	 */
	public void categoryProductList(){
		for(ProductCategory pc: this.categoryList){
			this.categoryProductList.put(pc, pc.getProducts());
		}
	}
	
	/**
	 * This method changes the photo of the product to the parameter photo
	 * @param p -> name of the product that is altered
	 * @param photo -> name(reference) of the photo that the product will be set to 
	 */
	public void changeProductPicture(Product p, String photo){ 
		p.setImage(photo);
	}

	/**
	 * This method increases or decreases the product quantity by the one specified according to add
	 * and changes the product quantity in the file
	 * @param p -> name of the product that is altered
	 * @param quantity -> the number of quantity that the product quantity is set to
	 * @param add -> true if we add quantity, false if we subtract
	 */
	public void changeProductQuantity(Product p, int quantity, boolean add){ 
		String oldProduct = p.toString();
		//p.setQuantity(quantity);
		if (add)
			p.addQuantity(quantity);
		else
			p.setQuantity(p.getQuantity() - quantity);
		String newProduct = p.toString();
		this.changeFile(oldProduct, newProduct);
	}
	
	/**
	 * This method changes the product quantity to the one specified
	 * and changes the product quantity in the file
	 * @param p -> name of the product that is altered
	 * @param quantity -> the number of quantity that the product quantity is set to
	 */
	public void changeProductQuantityGui(Product p, int quantity){
		String oldProduct = p.toString();
		p.setQuantity(quantity);
		String newProduct = p.toString();
		this.changeFile(oldProduct, newProduct);
	}
	
	/**
	 * This method changes the product description to the one specified
	 * and changes the product description in the file
	 * @param p -> name of the product that is altered
	 * @param description -> the description that the product description is set to
	 */
	public void changeProductDescription(Product p, String description){ 
		String oldProduct = p.toString();
		System.out.println("pm : " + p);
		p.setDescription(description);
		System.out.println("pm : " + p);
		String newProduct = p.toString();
		this.changeFile(oldProduct, newProduct);
		System.out.println("pm : " + p);
	}
	
	/**
	 * This method changes the product price to the one specified
	 * and changes the product price in the file
	 * @param p -> the name of the product that is altered
	 * @param price -> the price that the product price is set to
	 */
	public void changeProductPrice(Product p, double price){ 
		String oldProduct = p.toString();
		p.setPrice(price);
		String newProduct = p.toString();
		this.changeFile(oldProduct, newProduct);
	}
	
	/**
	 * This method returns a Product Category from the list of all Categories that contains 
	 * the description specified and if no ProductCategory exists with that description, then returns null
	 * @param description -> the description of the ProductCategory to be found
	 * @return -> Product Category with the description specified if it exists, else returns null
	 */
	public ProductCategory getCategoryByDescription(String description){
		ProductCategory pc = null;
		for(ProductCategory cat: this.categoryList){
			if(cat.getDescription().equals(description)){
				pc = cat;
				return pc;
			}
		}
		return pc;	
	}
	
	
	/**
	 * This method loads the file, by creating a new product and product category
	 * for each one in the file and adding them to the list of products and list of
	 * product categories, respectively
	 */
	public void loadFile() {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ProductCategory pc = null;
		Product p;
		
		try{
			br = new BufferedReader(new FileReader(this.filename));
			while((line = br.readLine()) != null){
				String[] product = line.split(cvsSplitBy);
				String pcat = product[0].replaceAll(", ^\"|\"$", "").trim();
				if(pcat.equals("CATEGORY")){
					// create a new product category for each one
					if(product.length == 1){
						
					}
					for(int i = 1; i < product.length; i++){
						String cat = product[i].replaceAll(", ^\"|\"$", "").trim();
						pc = new ProductCategory(cat);
						
						//add the product category to the list
						if(!this.categoryList.contains(pc)){
							this.categoryList.add(pc);
						}
					}
				}
				else{
					//create new product
					String description = product[0].replaceAll("^\"|\"$", "").trim();
					String price = product[1].replaceAll("^\"|\"$", "").trim();
					String quantity = product[2].replaceAll("^\"|\"$", "").trim();
					String category = product[3].replaceAll("^\"|\"$", "").trim();
					
					//get Product Category by string description
					pc = this.getCategoryByDescription(category);
			
					if (pc == null){
						pc = new ProductCategory(category);
						this.categoryList.add(pc);
					}
				
					p = new Product(description, pc ,Double.parseDouble(price), Integer.parseInt(quantity));
					//add product to list of products
					if(!this.productList.contains(p)){
						this.productList.add(p);
					}
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		
	    }catch(IOException e){
	    	e.printStackTrace();
	    }finally {
	    	//close file
	    	if(br != null) {
	    		try {
	    			br.close();
	    		}catch (IOException e){
	    			e.printStackTrace();
	    		}
	    	}
	    }	
	}

	/**
	 * This method adds a product to end of the Products.csv file
	 * @param product -> String representation of a product in the form :
	 * description, price, quantity, CATEGORY  
	 */
	private void saveFile(String product) {
		FileWriter fw = null;
		
		try{
			fw = new FileWriter(this.filename, true);
			fw.append(product);
			fw.append("\n");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				fw.flush();
				fw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}	
		
	}
	
	/**
	 * This method alters the Products.csv file when a product is changed
	 * (i.e when a price is changed using the method changeProductPrice then 
	 * this method should change the price of that product in the file to the new price) 
	 * @param oldProduct -> String representation of the Product before its altered
	 * @param newProduct -> String representation of the Product after its altered 
	 */
	private void changeFile(String oldProduct, String newProduct){
		Path filename = FileSystems.getDefault().getPath("src/DataFiles/Products.csv");
		List<String> fileContent;
		
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			
			for (int i = 0; i < fileContent.size(); i++) {
				if (fileContent.get(i).contains(oldProduct.substring(oldProduct.length() - 1))) {
			        fileContent.set(i, newProduct);
			        break;
			    }
			}
			Files.write(filename, fileContent, StandardCharsets.UTF_8);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * This method adds a ProductCategory to first line of the file, even if there is not 
	 * product in that category.
	 * @param pc -> name of the ProductCategory that is added to the file
	 */
	public void addCategoryToFile(ProductCategory pc){
		Path filename = FileSystems.getDefault().getPath("src/DataFiles/Products.csv");
		List<String> fileContent;
		
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			String categories = fileContent.get(0) + "," + pc.getDescription();
			fileContent.set(0, categories);
	
			Files.write(filename, fileContent, StandardCharsets.UTF_8);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * This method creates a sale for the product, adds the product to list of sales
	 * @param p -> the product that is sold
	 * @param quantity -> the amount of the product sold
	 */
	public void addToSales(Product p, Integer quantity) {
		sales.put(p, quantity);
	}
	
	/**
	 * This method returns the list of products that are sold
	 * @return -> HashMap of products and the amount of the product sold
	 */
	public HashMap<Product, Integer> getSales() {
		return sales;
	}
}
