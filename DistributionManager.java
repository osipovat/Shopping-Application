package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * This class is intended to manage Distribution Centers
 * and is accessed by Administrator and Inventory
 * 
 * @author group_0549
 *
 */
public class DistributionManager{
	private Inventory inventory;
	private ArrayList<DistributionCenter> distributionCentres;
	private String filename;
	
	/**
	 * This method initializes a DistributionManager
	 * @param inventory
	 */
	public DistributionManager(Inventory inventory){
		this.distributionCentres = new ArrayList<DistributionCenter>();
		this.filename = "src/DataFiles/DistributionCentres.csv";
		this.inventory = inventory;
	}
	
	/**
	 * This method returns all the Distribution Centers in a list
	 * @return -> ArrayList of all Distribution Centers
	 */
	public ArrayList<DistributionCenter> getDistributionCentres() {
		return distributionCentres;
	}

	/**
	 * This method sets the list of Distributions Centers to the one specified 
	 * @param distributionCentres -> ArrayList of all Distribution Centers
	 */
	public void setDistributionCentres(ArrayList<DistributionCenter> distributionCentres) {
		this.distributionCentres = distributionCentres;
	}

	/**
	 * This method adds a Distribution Center to the list of Distribution Centers
	 * @param dc -> the name of the Distribution Center to be added
	 */
	 public void addDistributionCenter(DistributionCenter dc, String name){
		 boolean check = true;
		 for (DistributionCenter d : this.distributionCentres) {
			 if (d.getCity().equals(name)) 
				 check = false;
		 }
		 if (check) {
			 this.distributionCentres.add(dc);
			 this.addDistributionCenerFile(dc);
		 }
		
	}
	
	
	/**
	 * This method loads the file, by creating a new Distribution Center and 
	 * then finding the products in the product list by their descriptions and 
	 * then adding the product and it's quantity to the HashMap in each Distribution Center
	 */
	 public void loadFile() {
		 BufferedReader br = null;
		 String line = "";
		 String cvsSplitBy = ",";
		 DistributionCenter DC;
		 String[] cities;
		 Product p;
		 int counter=0;
		 try{

			 br = new BufferedReader(new FileReader(this.filename));


			 while((line = br.readLine()) != null ){

				 if(line.trim().isEmpty())break;

				 String[] product = line.split(cvsSplitBy);
				 String pcat = product[0].replaceAll(", ^\"|\"$", "").trim();

				 if (pcat.equals("PRODUCT")) {
					 cities = product;
					 for (int i = 1; i < cities.length; i++) {
						 //create new DistributionCenter
						 String city = cities[i].replaceAll("^\"|\"$", "").trim();
						 DC = new DistributionCenter(city, this.inventory);

						 //add distribution center to list
						 if(!this.distributionCentres.contains(DC)){
							 this.distributionCentres.add(DC);


						 }

					 }

				 }

				 else if(pcat.matches("^[0-9]+$")){
					 String id = product[0].replaceAll("^\"|\"$", "").trim();
					 //get the product from product file by description
					 p = this.inventory.getProduct(Integer.parseInt(id));

					 for (int i = 0 ; i < this.distributionCentres.size(); i++){
						 String quantity = product[i + 1].replaceAll("^\"|\"$", "").trim();
						 //set the quantity hashmap of each distribution centre
						 this.distributionCentres.get(i).getQuantity().put(p, Integer.parseInt(quantity));
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
	 * Changes the quantity of the product in particular Distribution Center
	 * @param product -> The product's quantity to be updated
	 * @param quantity -> Quantity to set instead of existing quantity
	 * @param DC -> Distribution Center (in effect a city name)
	 * If currently the product with ID 112 has quantity 100 in Toronto,
	 * after the statement changeProductQuantity(product, "Toronto", 51)
	 * same product must have quantity 51 in the Toronto distribution center. 
	 */
	public void changeProductQuantity(Product product, int quantity, DistributionCenter DC){
		Path filename = FileSystems.getDefault().getPath("src/DataFiles/DistributionCentres.csv");
		List<String> fileContent;
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			String[] cities = fileContent.get(0).split(",");
			int index = 0;
			for (int i = 0; i < cities.length; i++){
				if (cities[i].equals(DC.getCity()))
					index = i;
			}
			for (int i = 1; i < fileContent.size(); i++) {
				String[] prod = fileContent.get(i).split(",");
				
				if (Integer.parseInt(prod[0]) == product.getID()){
					String[] newProduct = prod;
					newProduct[index] = String.valueOf(quantity);
					String np = String.join(",", newProduct); //join error
					
					fileContent.set(i, np);
					break;
				}	
			}
			Files.write(filename, fileContent, StandardCharsets.UTF_8);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Adds a distribution center to the Distribution Centers file
	 * @param DC -> The Distribution Center with a variable city.
	 */
	public void addDistributionCenerFile(DistributionCenter DC){
		//add Distribution center to a file
		Path filename = FileSystems.getDefault().getPath("src/DataFiles/DistributionCentres.csv");
		List<String> fileContent;
		
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			//get all the Distribution Centers and add a new DC
			String[] cities = fileContent.get(0).split(",");
			cities  = Arrays.copyOf(cities, cities.length + 1);
		    cities[cities.length - 1] = DC.getCity();
		    String citiesString = String.join(",", cities);
		    fileContent.set(0, citiesString);
		    
		    //sets the quantity of DC products to 0 in a file
			for (int i = 1; i < fileContent.size(); i++) {
				String[] products = fileContent.get(i).split(",");
				products  = Arrays.copyOf(products, products.length + 1);
			    products[products.length - 1] = "" + 0;
			    String productsString = String.join(",", products);
			    fileContent.set(i, productsString);
			}  
			
			Files.write(filename, fileContent, StandardCharsets.UTF_8);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Adds the product specified to the list and sets the quantity to 0
	 * If the given distribution center exists, or sesionID invalid, do nothing.
	 * @param city -> The city where distribution center must be based.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 */
	public void addProductToFile(Product product){
		Path filename = FileSystems.getDefault().getPath("src/DataFiles/DistributionCentres.csv");
		List<String> fileContent= null;
		String[] cities = null;
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			cities = fileContent.get(0).split(",");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		FileWriter fw = null;
				
		try{
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			fw = new FileWriter(this.filename, true);	
			fw.append("" + product.getID());
			String line = "";
			int quantity =0;
	
				
			for(int i = 1; i < this.distributionCentres.size(); i++){
				quantity= this.inventory.getDistributionCenters().get(i).getQuantity().get(product);
				line += "," +  quantity;	
			}
			line = ("," + quantity) + line;
			fw.append(line);
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
}