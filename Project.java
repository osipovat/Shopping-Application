package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class is intended to be your API
 * @author Ilir
 *
 */
public class Project {
	private static int sessionSequence;
	private User user;
	public Inventory inventory = new Inventory();
	public DeliveryRoute del = new DeliveryRoute();
	public int cost;
	
	
	public Project(){ 
		this.inventory.setDistributionManager(this.inventory);
	}
	 
	public User getUser(){
		return user;
	}
	
	/**
	 * This method must add a new shopper user or administrator user.
	 * @param userID
	 * @param password
	 * @param admin -> if true, add an administrator user, otherwise add a shopper user
	 * @return -> true if operation successful, false otherwise
	 */
	public boolean addUser(String userID, String password, boolean admin) {
		if (!this.inventory.searchUser(userID, password, admin)) {
			if (!admin) {
				user = new Shopper(userID, password);
				((Shopper)user).setInventory(this.inventory);
				this.inventory.addShopper((Shopper)user);
				
			}
			else if (admin) {
				user = new Administrator(userID, password, this.inventory);
				this.inventory.addAdmin((Administrator)user);
			}
			return true;
		}
		else return false;	// duplicate user
	}
	
	/**
	 * Authenticates a user and creates an active work session
	 * @param userID 
	 * @param password
	 * @return -> SessionID if authentication successful, -1 otherwise.
	 */
	public int login(String userID, String password) {
		for (Shopper s: this.inventory.getShopperList()) {
			if (s.getUsername().equals(userID) && s.getPassword().equals(password)) {
				user = s;
				return s.logIn(userID, password);
			}
		}
		for (Administrator admin: this.inventory.getAdminList()) {
			if (admin.getUsername().equals(userID) && admin.getPassword().equals(password))
				user = admin;
				return admin.logIn(userID, password);
		}
		return -1;
	}
	
	/**
	 * Makes sessionID unavailable for connection
	 * @param sessionID
	 */
	public void logout(int sessionID) {
		if(sessionID % 2 == 1){
			for (Shopper shopper : this.inventory.getShopperList()){
				if (shopper.getSessionID() == sessionID){
					this.inventory.saveShopper(shopper);
					shopper.logOut();
				}
			}
		}else{
			for (Administrator administrator : this.inventory.getAdminList()){
				if (administrator.getSessionID() == sessionID){
					user = administrator;
					administrator.logOut();
				}
			}
		}
	}
	
	/**
	 * This method must add a new category in your application.
	 * @param catName -> The name of the category to be added.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @return -> The ID of the category you created if successful, -1 if not successful.
	 */
	public int addCategory(String catName, int sessionID) {
		ProductCategory pc;
		boolean exists = false;
		
		for (Administrator administrator : this.inventory.getAdminList()){
			if (administrator.getSessionID() == sessionID){
				if(this.inventory.getCategoryList().isEmpty()){
					pc = new ProductCategory(catName);
					this.inventory.addProductCategory(pc);
					
					return pc.getCategoryCode();
				}
				for(ProductCategory pcat : this.inventory.getCategoryList()){
					if(pcat.getDescription().equals(catName)){
						exists = true;
					}
				}
				if(!exists){
					
					pc = new ProductCategory(catName);
					this.inventory.addProductCategory(pc);
				
					return pc.getCategoryCode();
				}
			}
		}
		return -1;
	}
	
	/**
	 * Adds a distribution center to your application.
	 * If the given distribution center exists, or sesionID invalid, do nothing.
	 * @param city -> The city where distribution center must be based.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 */
	public void addDistributionCenter(String city, int sessionID) {
		for (Administrator administrator : this.inventory.getAdminList()){
			if (administrator.getSessionID() == sessionID){
				user = administrator;
				DistributionCenter newDist = new DistributionCenter(city,((Administrator) user).getInvenory());
				this.inventory.addDistributionCenter(newDist);
				del.addCity(city);
				((Administrator)user).addCityFile(del.getCity(city));
			}
		}	
	}
	
	/**
	 * Adds a new Customer to your application; the customer record that belongs 
	 * to a newly added shopper user that has no customer record on the system.
	 * @param custName -> The name of the customer
	 * @param city -> The city of the customer address
	 * @param street -> The street address of the customer
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The added customer ID
	 */
	public int addCustomer(String custName, String city, String street, int sessionID) {
		//check the list of shoppers and get the shopper with the same sessionID
		if (sessionID % 2 == 1) {
			int customerId = 0;
			for(Shopper s : this.inventory.getShopperList()){
				if(s.getSessionID() == sessionID){
					//then change the shopper to a customer and set the session iD
					s.setCustomer(new Customer (s.getUsername(), s.getPassword(), custName, city, street));
					customerId = s.getShopperID();
					this.inventory.addCustomer(s);	
					del.addCity(city);
					return customerId;
				}
			}
		} 
		return -1;
	
	}
	
	/**
	 * Adds a new Product to your application
	 * @param prodName -> The product name
	 * @param category -> The product category.
	 * @param price -> The product sales price
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @return -> Product ID if successful, -1 otherwise.
	 */
	public int addProduct(String prodName, int category, double price, int sessionID) {
		for (Administrator administrator : this.inventory.getAdminList()){
			if (administrator.getSessionID() == sessionID){ // check for log out
				user = administrator;
				Product p;
				
				for (ProductCategory pc : this.inventory.getCategoryList()) {					
					if (pc.getCategoryCode() == category) {
						p = this.inventory.getProductByDescription(prodName);
						if(p==null){
							p = new Product(prodName, pc, price, 0);
							this.inventory.addProduct(p);
							this.inventory.addProductDistributionCenter(p);
					
							return p.getID();
						}
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * Computes the available quantity of prodID in a specific distribution center.
	 * @param prodID
	 * @param center
	 * @return -> Available quantity or -1 if prodID or center does not exist in the database
	 */
	public int prodInquiry(int prodID, String center) {
		return this.inventory.getProductNumber(prodID, center);
	}
	
	/**
	 * Updates the stock quantity of the product identified by prodID
	 * @param prodID -> The product ID to be updated
	 * @param distCentre -> Distribution Center (in effect a city name)
	 * @param quantity -> Quantity to add to the existing quantity
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * If currently the product 112 has quantity 100 in Toronto,
	 * after the statement updateQuantity(112, "Toronto", 51)
	 * same product must have quantity 151 in the Toronto distribution center. 
	 * @return -> true if the operation could be performed, false otherwise.
	 */
	public boolean updateQuantity(int prodID, String distCentre, int quantity, int sessionID) {
		Product p1 = null;
		for (Administrator administrator : this.inventory.getAdminList()){
			if (administrator.getSessionID() == sessionID){ // check for log out
				for (DistributionCenter d: this.inventory.getDistributionCenters()) {
					if (d.getCity().equals(distCentre)) {	// find given center
						for (Product p : d.getQuantity().keySet()){
							if (p.getID() == prodID) { 
								p1 = this.inventory.getProduct(prodID);	// change the quantity of product in inventory
								if (p1 != null)									
									this.inventory.changeQuantity(p1, quantity, true);
								this.inventory.changeQuantityDC(p, quantity, d);
								return true;
							}
						}
					}
				}	
			}
		}
		return false;
	}
	
	
	public boolean addToShoppingCart(int productID, int quantity, int sessionID, int custID) {
		for (Shopper s : this.inventory.getShopperList()) {
			if (s.getSessionID() == sessionID) {	// check for log out
				if (s.getShopperID() == custID) 	{// find out shopper
					Product  p = this.inventory.getProduct(productID);
					try {
						if (quantity <= p.getQuantity()) {	// there is enough quantity to add to a shopping cart
							s.getShoppingCart().add(p, quantity);
							p.setQuantity(p.getQuantity() - quantity);	// change the quantity temporary
							return true;
						}
						
					} catch (OutOfStockException e) {
						e.printStackTrace();
					}
				}
			}
			else
				System.out.println("Logged out user!");
		}
		return false;
	}
	
	/**
	 * Adds two nodes cityA, cityB to the shipping graph
	 * Adds a route (an edge to the shipping graph) from cityA to cityB with length distance
	 * If the nodes or the edge (or both) exist, does nothing
	 * @param cityA 
	 * @param cityB
	 * @param distance -> distance (in km, between cityA and cityB)
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 */
	public void addRoute(String cityA,String cityB, int distance, int sessionID) {
		// Your code goes here
		for (Administrator administrator : this.inventory.getAdminList()){
			if (administrator.getSessionID() == sessionID){ // check for log out	
				City a = null;
				City b = null;
				for(City c : del.getCities()){
					if (c == null) 
						break;
					if(cityA.equals(c.getCity())) {
						a = c; 
					}
					if(cityB.equals(c.getCity())) {
						b = c;	
					}
					if (a == null || b == null)  
						continue;

					if (a != null && b != null) {
						del.addRoute(a.getCity(), b.getCity(), distance);
					}					
				}
				
			}
		}
	}
	
	
	
	/**
	 * Attempts an order in behalf of custID for quantity units of the prodID
	 * @param custID -> The customer ID
	 * @param prodID -> The product ID
	 * @param quantity -> The desired quantity
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The orderID if successful, -1 if not.
	 */
	public int placeOrder(int custID, int prodID, int quantity, int sessionID) {
		for (Shopper shopper : this.inventory.getShopperList()){
			if (shopper.getSessionID() == sessionID){ // check for log out
				int orderID;
				Product p = null;
				for (Product product : this.inventory.getProductList()){
					if (product.getID() == prodID){
						p = product;
					}
				}
				for(Shopper customer : this.inventory.getShopperList()){
					//finds the customer from the list of customers in the inventory
					if (customer.getShopperID()== custID){
						try {
							//add product to the shopping cart
							customer.getShoppingCart().add(p, quantity);
							//generate the invoice and adds it to the list of invoices of this customer
							orderID =customer.getShoppingCart().placeOrder(custID, p, quantity, sessionID);
							this.inventory.changeQuantity(p, quantity, false);	// subtract quantity from product
							String distributionCenter = null;
							try {
								distributionCenter = getDeliveryRoute(orderID,sessionID).get(0);
							} catch (HeapEmptyException e) {
								e.printStackTrace();
							} catch (HeapFullException e) {
								e.printStackTrace();
							}
							for (DistributionCenter d : this.inventory.getDistributionCenters()){
								if(d.getCity().equals(distributionCenter)){
									this.inventory.changeQuantityDC(p, quantity, d);
								}
							}

							return orderID;
						} catch (OutOfStockException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return -1;
	}
    
	/**
	 * Returns the best (shortest) delivery route for a given order 
	 * @param orderID -> The order ID we want the delivery route
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The actual route as an array list of cities, null if not successful
	 * @throws HeapFullException 
	 * @throws HeapEmptyException 
	 */
	public ArrayList<String> getDeliveryRoute(int orderID, int sessionID) throws HeapEmptyException, HeapFullException {
		String startCity;
		String destCity;
		City start;
		City dest;
		HashMap<ArrayList<String>, Integer> distanceList = new HashMap<ArrayList<String>, Integer> ();  
		Product product = null;
		int quantity = 0;
		for(Shopper s : this.inventory.getShopperList()){
			if(s.getSessionID()==sessionID){
				destCity = s.getCustomer().getCity();
				for(ShippingInvoice s1 :s.getCustomer().getInvoices() ){
					if(s1.getTrackNum()==orderID){
						for(Product p1: s1.getProductList().keySet()){
							product=p1;
							quantity=s1.getProductList().get(p1);
						}// we found what product and quantity we need
						for(DistributionCenter d : this.inventory.getDistributionCenters()){
							startCity =d.getCity();
							start = del.getCity(startCity);
							dest = del.getCity(destCity);

							if(start!=null && dest!=null){ 
								del.Dijkstra(start, dest);
								cost=del.getCost(dest);
								distanceList.put(del.Dijkstra(start, dest), cost);					
							}

							if(d.getQuantity().get(product) < quantity) continue;
						}
						// we found distribution center distance list
						int shortestDistance = Collections.min(distanceList.values());
						if(shortestDistance< cost) cost=shortestDistance;
						for(ArrayList<String> route: distanceList.keySet()) 
							if(distanceList.get(route)==shortestDistance) return route;
					}
				}
			}
		}	
		return null;
	}
	
	/** 
	 * Computes the invoice amount for a given order.
	 * Please use the fixed price 0.01$/km to compute the shipping cost 
	 * @param orderID
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return
	 */
	public double invoiceAmount(int orderID, int sessionID) {
		for(Shopper s : this.inventory.getShopperList())
			if(s.getSessionID()==sessionID)
				for(ShippingInvoice s1 :s.getCustomer().getInvoices())
					if(s1.getTrackNum()==orderID) return s1.getFinalprice();//cost;		
		return -1;
	}

}